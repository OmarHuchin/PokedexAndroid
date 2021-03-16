package com.example.pokedex.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.properties.Delegates
import androidx.lifecycle.Observer as LifeCycleObserver
/**
 * Start activity from context with extras
 */
inline fun <reified T : Activity> Context.startActivity(block: (Intent) -> Unit) {
    this.startActivity(Intent(this, T::class.java).apply(block))
}

/**
 * Start activity from context with extras
 */
inline fun <reified T : Activity> Context.startActivityNewRoot(block: (Intent) -> Unit) {
    val intent = Intent(this, T::class.java).apply(block)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    this.startActivity(intent)
}

/**
 * Add disposable instance to a CompositeDisposable
 */
fun Disposable.addToDisposables(compositeDisposables: CompositeDisposable) = compositeDisposables.add(this)
/**
 * Observe a LiveData from a LifeCycleOwner (Activity, Fragment)
 * @param liveData the live data to observe
 * @param body lambda called when the livedata changes
 * @sample this@Activity.observe(viewModel.data) { data: LiveData ->
 *      doSomething(data)
 * }
 */
fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    this.observe(liveData, true, body)
}
/**
 * Observe a LiveData from a LifeCycleOwner (Activity, Fragment)
 * @param liveData the live data to observe
 * @param removeOldObservers if this param is true removes previous observers from this livedata (prevent redundant calls)
 * @param body lambda called when the livedata changes
 * @sample this@Activity.observe(viewModel.data) { data: LiveData ->
 *      doSomething(data)
 * }
 */
fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, removeOldObservers: Boolean, body: (T?) -> Unit) {
    if (removeOldObservers) liveData.removeObservers(this)
    liveData.observe(this, LifeCycleObserver(body))
}

fun <VH: RecyclerView.ViewHolder,T> RecyclerView.Adapter<VH>.basicDiffUtil(initialValue:List<T>,
                                                                           areItemsTheSame:(T,T)->Boolean = {o,n-> o==n},
                                                                           areContentsTheSame: (T,T)->Boolean= {o,n-> o==n})= Delegates.observable(
    initialValue){ _, old,new ->
    DiffUtil.calculateDiff(object :DiffUtil.Callback(){
        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean  = areItemsTheSame(old[oldItemPosition],new[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areContentsTheSame(old[oldItemPosition],new[newItemPosition])
    }).dispatchUpdatesTo(this)
}