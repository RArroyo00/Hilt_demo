package com.app.bikercopilot.common.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class is intended to be a [MediatorLiveData] that emits then gets activated only
 *
 * Based on a map tp retain and serve the observers to be notified or to avoid notifies again
 */
open class SingleLiveEventMediator<T>:MediatorLiveData<T>() {
    private val mPendingObservers = mutableMapOf<Observer<in T>, AtomicBoolean>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val interceptor = object : Observer<T>{
            override fun onChanged(t: T?) {
                synchronized(mPendingObservers){
                    mPendingObservers[this]?.let {
                        if(it.compareAndSet(true,false)){
                            observer.onChanged(t)
                        }
                    }
                }
            }
        }
        synchronized(mPendingObservers){
            mPendingObservers[interceptor] = AtomicBoolean(false)
            super.observe(owner, observer)
        }
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        synchronized(mPendingObservers){
            mPendingObservers.remove(observer)
            super.removeObserver(observer)
        }
    }

    @MainThread
    override fun setValue(value: T) {
        synchronized(mPendingObservers){
            for (pending in mPendingObservers.values){
                pending.set(true)
            }
        }
        super.setValue(value)
    }
}