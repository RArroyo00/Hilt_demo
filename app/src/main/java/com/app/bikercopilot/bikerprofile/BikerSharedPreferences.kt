package com.app.bikercopilot.bikerprofile

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE
import androidx.security.crypto.MasterKey.DEFAULT_MASTER_KEY_ALIAS
import com.app.bikercopilot.common.Constants
import com.app.bikercopilot.utils.deserialize
import com.app.bikercopilot.utils.serialize
import com.app.bikercopilot.domain.Biker
import javax.inject.Inject


class BikerSharedPreferences @Inject constructor(context: Context) : BikerProfileStore {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        val spec = KeyGenParameterSpec.Builder(DEFAULT_MASTER_KEY_ALIAS ,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREFERENCES_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        editor = sharedPreferences.edit()
    }

    companion object {
        private const val PREFERENCES_NAME = "com.app.bikercopilot.biker"
        private const val CURRENT_BIKER = "current_biker"
    }

    override fun setBikerProfile(biker: Biker) {
        Log.wtf("setBikerProfile", serialize(biker))
        editor.putString(CURRENT_BIKER, serialize(biker)).apply()
    }

    override fun clearData() {
       editor.clear().apply()
    }

    override fun getBikerProfile(): Biker? {
        return try {
            val rawBiker = sharedPreferences.getString(CURRENT_BIKER, Constants.EMPTY_STRING)
            Log.wtf("getBikerProfile", rawBiker)
            rawBiker?.let {
                deserialize(Biker::class.java, rawBiker)
            }
        } catch (x: Exception) {
            Log.wtf("getBikerProfile", x.message)
            null
        }
    }
}
