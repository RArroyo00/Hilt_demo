package com.app.bikercopilot.common.livedata

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T>: MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasObservers()){
            Log.w(TAG,"Multiple observers registered but only one will be notified")
        }

        // Observe internal MutableLiveData
        super.observe(owner, { t ->
            if (pending.compareAndSet(true,false)){
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }

    companion object{
        private const val TAG = "SingleLiveEvent"
    }
}