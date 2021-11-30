package uz.techie.mahsulot.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_category.category_error_tv
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_search.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.CategoryAdapter
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.adapter.SliderAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.InfoDialog
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.Category
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Resource

@AndroidEntryPoint
class ProductFragment:Fragment(R.layout.fragment_product){
    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewModel:MahsulotViewModel
    private val TAG = "CategoryFragment"
    private lateinit var gridLayoutManager:GridLayoutManager
    private var catId = 0
    private var category:Category? = null
    private var previousCatId = 0
    private lateinit var infoDialog:InfoDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        infoDialog = InfoDialog(requireContext())

        productAdapter = ProductAdapter(object :ProductAdapter.OnOpenFragment{
            override fun openFragment(viewTypeId: Int, viewType:Int) {

            }

            override fun onItemClick(product: Product) {
                findNavController().navigate(ProductFragmentDirections.actionGlobalProductDetailsFragment(product))
            }

        })


        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = gridLayoutManager
        recyclerview.adapter = productAdapter

        viewModel.productsByCategory.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: observer "+response.message)
            Log.d(TAG, "onViewCreated: observer "+response.data)
            when (response) {
                is Resource.Success -> {
                    swipe_refresh.isRefreshing = false
                    hideErrorText()
                    hideProgressbar()
                    response.data?.let { productsResponse ->
                        val productList = mutableListOf<Product>()
                        productList.addAll(productsResponse.filter { product ->
                            product.status == "True"
                        })

                        productAdapter.differ.submitList(productList)
                    }
                }
                is Resource.Error -> {
                    swipe_refresh.isRefreshing = false
                    hideProgressbar()
                    showErrorText(response.message!!)
                }
                is Resource.Loading -> {
                    swipe_refresh.isRefreshing = false
                    hideErrorText()
                    showProgressbar()
                }
            }
        })

        category = arguments?.let { ProductFragmentArgs.fromBundle(it).category }
        category?.let {
            catId = it.id!!
            Log.d(TAG, "onViewCreated: catId "+catId)
            Log.d(TAG, "onViewCreated: previousCatId "+previousCatId)
            viewModel.loadProductsByCategory(catId)
        }

        initToolbar()
        initSwipeRefresh()



    }

    private fun initSwipeRefresh(){
        swipe_refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                viewModel.loadProductsByCategory(catId)
            }

        })
    }

    private fun initToolbar(){
        category?.let {
            toolbar_title.text = it.name
            toolbar_title.visibility = View.VISIBLE
        }


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.setOnClickListener {
            findNavController().navigate(CategoryFragmentDirections.actionGlobalSearchFragment())
        }

    }



    private fun showErrorText(message: String) {
        infoDialog.show()
        infoDialog.submitData(message)
    }

    private fun hideErrorText() {
        infoDialog.dismiss()
    }



    private fun showProgressbar() {
        progressbar.visibility = View.VISIBLE
    }


    private fun hideProgressbar() {
        progressbar.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        if (catId != previousCatId){
            viewModel.productsByCategory.postValue(Resource.Success(emptyList()))
        }
        previousCatId = catId
    }



}