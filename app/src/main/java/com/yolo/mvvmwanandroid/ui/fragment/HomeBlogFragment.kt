package com.yolo.mvvmwanandroid.ui.fragment

import androidx.lifecycle.Observer
import com.yolo.mvvm.fragment.BaseFragment
import com.yolo.mvvmwanandroid.ui.activity.DetailActivity
import com.yolo.mvvmwanandroid.R
import com.yolo.mvvmwanandroid.databinding.FragmentHomeBlogBinding
import com.yolo.mvvmwanandroid.ui.adapter.BlogAdapter
import com.yolo.mvvmwanandroid.ui.adapter.BlogDiffCallBack
import com.yolo.mvvmwanandroid.ui.loadmore.CommonLoadMoreView
import com.yolo.mvvmwanandroid.ui.loadmore.LoadMoreStatus
import com.yolo.mvvmwanandroid.ui.widget.ScrollToTop
import com.yolo.mvvmwanandroid.viewmodel.HomeBlogFragmentViewModel

/**
 * @author yolo.huang
 * @date 2020/7/2
 */
class HomeBlogFragment:BaseFragment<HomeBlogFragmentViewModel, FragmentHomeBlogBinding>(),ScrollToTop {

    companion object{
        val instance =  HomeBlogFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_home_blog

    override fun initView() {
        val adapter = BlogAdapter().apply {
            loadMoreModule.apply {
                loadMoreView = CommonLoadMoreView()
                setOnLoadMoreListener {
                    mViewModel.getMoreBlog()
                }
                setOnItemClickListener { _, _, position ->
                    DetailActivity.enterDetail(mActivity,data[position])
                }
            }
            animationEnable = true
            setDiffCallback(BlogDiffCallBack())
        }
        mDataBinding.srlBlog.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.getBlog()
            }
        }
        mDataBinding.adapter = adapter
        mViewModel.apply {
            blog.observe(viewLifecycleOwner,Observer{
                adapter.setNewInstance(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                mDataBinding.srlBlog.isRefreshing = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when(it){
                    LoadMoreStatus.ERROR -> adapter.loadMoreModule.loadMoreFail()
                    LoadMoreStatus.END -> adapter.loadMoreModule.loadMoreEnd()
                    LoadMoreStatus.COMPLETED ->adapter.loadMoreModule.loadMoreComplete()
                    else -> return@Observer
                }
            })
            getBlog()
        }

    }

    override fun scrollToTop() {
        mDataBinding.rvHomeBlog.smoothScrollToPosition(0)

    }
}