package com.appzeto.lms.ui.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appzeto.lms.R
import com.appzeto.lms.databinding.FragCategoriesBinding
import com.appzeto.lms.manager.App
import com.appzeto.lms.manager.adapter.CategoriesRvAdapter
import com.appzeto.lms.manager.adapter.TrendingCategoriesRvAdapter
import com.appzeto.lms.manager.listener.ItemClickListener
import com.appzeto.lms.manager.listener.OnItemClickListener
import com.appzeto.lms.manager.net.observer.NetworkObserverFragment
import com.appzeto.lms.model.*
import com.appzeto.lms.model.view.CategoryView
import com.appzeto.lms.presenter.Presenter
import com.appzeto.lms.presenterImpl.CategoriesPresenterImpl
import com.appzeto.lms.ui.MainActivity

class CategoriesFrag : NetworkObserverFragment(), OnItemClickListener {

    private lateinit var mBinding: FragCategoriesBinding
    private lateinit var mPresneter: Presenter.CategoriesPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragCategoriesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val toolbarOptions = ToolbarOptions()
        toolbarOptions.startIcon = ToolbarOptions.Icon.NAV

        (activity as MainActivity).showToolbar(toolbarOptions, R.string.categories)
        (activity as MainActivity).setContainer(false)

        mPresneter = CategoriesPresenterImpl(this)
        mPresneter.getTrendingCategories()
        mPresneter.getCategories()
    }

    fun onTrendingCategoriesRecevied(data: Data<Count<Category>>) {
        val items = data.data!!.items
        mBinding.categoriesTrendingProgressBar.visibility = View.GONE

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val adapter = TrendingCategoriesRvAdapter(items, requireActivity().windowManager)
        mBinding.categoriesTrendingRv.layoutManager = linearLayoutManager
        mBinding.categoriesTrendingRv.adapter = adapter
        mBinding.categoriesTrendingRv.addOnItemTouchListener(
            ItemClickListener(
                mBinding.categoriesTrendingRv,
                this
            )
        )
    }

    fun onCategoriesRecevied(data: Data<Count<Category>>) {
        val items = data.data!!.items
        mBinding.categoriesProgressBar.visibility = View.GONE

        val groups = ArrayList<CategoryView>()

        for (item in items) {
            item.subCategories.let {
                val categoryView =
                    CategoryView(item.title, it)
                categoryView.catTitle = item.title
                categoryView.count = item.count
                categoryView.icon = item.icon
                categoryView.id = item.id
                groups.add(categoryView)
            }
        }

        mBinding.categoriesExpandableRv.adapter =
            CategoriesRvAdapter(groups, activity as MainActivity)
    }

    override fun onClick(view: View?, position: Int, id: Int) {
        val category =
            (mBinding.categoriesTrendingRv.adapter as TrendingCategoriesRvAdapter).items[position] as Category

        val bundle = Bundle()
        bundle.putParcelable(App.CATEGORY, category)

        val categoryFrag = CategoryFrag()
        categoryFrag.arguments = bundle

        (activity as MainActivity).transact(categoryFrag)
    }

    override fun onLongClick(view: View?, position: Int, id: Int) {
    }
}