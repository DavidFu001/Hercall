package com.dating.lib.ui.fragment

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.dating.lib.ui.callback.SmartRefreshListener
import com.dating.lib.ui.viewmodel.MviViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout

abstract class BaseListFragment<VB : ViewBinding, VM : MviViewModel, T> :
    BaseMviFragment<VB, VM>(),
    SmartRefreshListener {
    private val pageSize: Int = 20
    var pageNumber = 1
    private lateinit var mAdapter: RecyclerView.Adapter<ViewHolder>
    private lateinit var recyclerView: RecyclerView
    private lateinit var smartRefreshLayout: SmartRefreshLayout

    /**
     * 数据源
     */
    open val dataList by lazy {
        arrayListOf<T>()
    }


    override fun initUI() {
        smartRefreshLayout = getSmartRefreshLayout()
        recyclerView = getRecycleView()
        mAdapter = getAdapter()
    }


    override fun initListener() {
        smartRefreshLayout.apply {
            setOnRefreshListener {
                //将页码设置初始位
                pageNumber = 1
                onRefresh()
            }

            setOnLoadMoreListener {
                onLoadMore()
            }
        }

    }

    override fun initData() {
        recyclerView.apply {
            layoutManager = this@BaseListFragment.getLayoutManager()
            addItemDecoration()
            adapter = mAdapter
        }
        smartRefreshLayout.setEnableLoadMore(needLoadMore())
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun updateUI(data: List<T>) {
        if (pageNumber == 1) {
            dataList.clear()
        }

        if (data.isNotEmpty()) {
            dataList.addAll(data)
            pageNumber++
            if (needLoadMore()) {
                smartRefreshLayout.setEnableLoadMore(true)
            }
        } else {
            if (needLoadMore()) {
                smartRefreshLayout.setEnableLoadMore(false)
            }
        }

        smartRefreshLayout.finishRefresh()
        smartRefreshLayout.finishLoadMore()
        mAdapter.notifyDataSetChanged()
        if (dataList.isEmpty()) {//如果历史数据位空，并且请求的新数据也为空，那么就显示空页面
            showEmpty()
        } else {
            showCustom()
        }
    }

    fun updateErrorUI() {
        if (dataList.isEmpty()) {//如果历史数据空，并且请求的新数据也为空，那么就显示空页面
            showEmpty()
            return
        } else {
            smartRefreshLayout.finishRefresh()
            smartRefreshLayout.finishLoadMore()
        }
    }

    /**
     * 是否有load more的功能
     */
    open fun needLoadMore(): Boolean {
        return true
    }

    abstract fun getRecycleView(): RecyclerView

    abstract fun getAdapter(): RecyclerView.Adapter<ViewHolder>

    abstract fun getSmartRefreshLayout(): SmartRefreshLayout

    abstract fun getLayoutManager(): LayoutManager

    open fun addItemDecoration() {
    }
}