package com.yolo.mvvmwanandroid.ui.fragment

import androidx.lifecycle.Observer
import com.yolo.mvvm.fragment.BaseFragment
import com.yolo.mvvmwanandroid.ui.activity.DetailActivity
import com.yolo.mvvmwanandroid.R
import com.yolo.mvvmwanandroid.databinding.FragmentPlazaBinding
import com.yolo.mvvmwanandroid.ui.adapter.BlogAdapter
import com.yolo.mvvmwanandroid.ui.adapter.BlogDiffCallBack
import com.yolo.mvvmwanandroid.ui.loadmore.CommonLoadMoreView
import com.yolo.mvvmwanandroid.ui.loadmore.LoadMoreStatus
import com.yolo.mvvmwanandroid.ui.widget.ScrollToTop
import com.yolo.mvvmwanandroid.viewmodel.PlazaFragmentViewModel

/**
 * @author yolo.huang
 * @date 2020/9/15
 */
class PlazaFragment :BaseFragment<PlazaFragmentViewModel,FragmentPlazaBinding>(),ScrollToTop{

    companion object{
        val instance =  PlazaFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_plaza

    override fun initView() {
        val adapter = BlogAdapter().apply {
            loadMoreModule.loadMoreView = CommonLoadMoreView()
            loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMorePlaza()
            }
            setOnItemClickListener { _, _, position ->
                DetailActivity.enterDetail(mActivity,data[position])
            }
            setDiffCallback(BlogDiffCallBack())
            animationEnable = true

        }
        mDataBinding.adapter = adapter

        mDataBinding.srlPlaza.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.getPlaza()
            }
        }

        mViewModel.apply {
            refreshStatus.observe(viewLifecycleOwner, Observer {
                mDataBinding.srlPlaza.isRefreshing = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when(it){
                    LoadMoreStatus.ERROR -> adapter.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapter.loadMoreModule.loadMoreEnd()
                    LoadMoreStatus.COMPLETED ->adapter.loadMoreModule.loadMoreComplete()
                    else -> return@Observer
                }
            })

            plaza.observe(viewLifecycleOwner, Observer {
                adapter.setNewInstance(it)
            })

            getPlaza()
        }
    }

    override fun scrollToTop() {
        mDataBinding.rvPlaza.smoothScrollToPosition(0)
    }
}