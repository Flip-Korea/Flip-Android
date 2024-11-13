package com.team.data.paging

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.CoroutineDispatcher

suspend fun <T : Any> PagingData<T>.collectDataForTest(
    mainDispatcher: CoroutineDispatcher,
    workerDispatcher: CoroutineDispatcher,
): List<T> {
    val dcb = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }
    val items = mutableListOf<T>()

    val differ = AsyncPagingDataDiffer(
        diffCallback = dcb,
        updateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        },
        mainDispatcher = mainDispatcher,
        workerDispatcher = workerDispatcher
    )

    differ.submitData(this)

    // 모든 아이템이 로드될 때까지 대기
    if (differ.itemCount > 0) {
        for (i in 0 until differ.itemCount) {
            differ.getItem(i)?.let { items.add(it) }
        }
    }

    return items
}