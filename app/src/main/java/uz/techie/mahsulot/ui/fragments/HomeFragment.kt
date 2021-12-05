package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.InfoDialog
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Constants
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var productAdapter: ProductAdapter
    private lateinit var viewModel: MahsulotViewModel
    private val TAG = "HomeFragment"
    private val TAG2 = "FFFFFFFFFF"
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var infoDialog: InfoDialog
    private var products = mutableListOf<Product>()
    private var hasError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG2, "onCreate: ")
        Log.d(TAG2, "onCreate bundle: " + savedInstanceState)

        Log.d(TAG, "onCreate: phoneee "+Utils.hidePhoneNumber("998999952666"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG2, "onCreateView: ")
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG2, "onViewCreated: ")
        (activity as MainActivity).setSupportActionBar(toolbar)
        viewModel = (activity as MainActivity).viewModel

        infoDialog = InfoDialog(requireContext())

        val sliderFragment = SliderFragment()
        val topProductFragment = TopProductFragment()

        productAdapter = ProductAdapter(object : ProductAdapter.OnOpenFragment {
            override fun openFragment(viewId: Int, viewType: Int) {

                if (viewType == ProductAdapter.HEADER) {
                    try {
                        Log.d(TAG, "openFragment: try")
                        val fragmentManager = parentFragmentManager
                        if (sliderFragment.isAdded) {
                            Log.d(TAG, "openFragment: isAdded")
                            fragmentManager.popBackStackImmediate(
                                SliderFragment::class.simpleName,
                                0
                            )
                        }
                        fragmentManager.beginTransaction()
                            .replace(viewId, sliderFragment)
                            .commitAllowingStateLoss()


                    } catch (e: Exception) {
                        Log.d(TAG, "openFragment: error " + e.message)
                        e.printStackTrace()
                    }
                }
                else if (viewType == ProductAdapter.TOP_PRODUCT) {
                    try {
                        Log.d(TAG, "openFragment: try")
                        val fragmentManager = parentFragmentManager
                        if (topProductFragment.isAdded) {
                            Log.d(TAG, "D")
                            fragmentManager.popBackStackImmediate(
                                TopProductFragment::class.simpleName,
                                0
                            )
                        }
                        Log.d(TAG, "openFragment: isNotAdded")
                        Log.d(TAG, "openFragment: viewId "+viewId)
                        fragmentManager.beginTransaction()
                            .replace(viewId, topProductFragment)
                            .commitAllowingStateLoss()


                    } catch (e: Exception) {
                        Log.d(TAG, "openFragment: error " + e.message)
                        e.printStackTrace()
                    }
                }


            }

            override fun onItemClick(product: Product) {
                Log.d(TAG, "onItemClick: " + product.id)
                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalProductDetailsFragment(
                        product
                    )
                )
            }

        })


        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                Log.d(TAG, "getSpanSize: " + productAdapter.getItemViewType(position))
                Log.d(TAG, "getSpanSize:  " + productAdapter.getItemViewType(position))

                if (productAdapter.getItemViewType(position) == ProductAdapter.HEADER
                    || productAdapter.getItemViewType(position) == ProductAdapter.TOP_PRODUCT
                ) {
                    return 2
                } else {
                    return 1
                }
            }

        }


        product_recyclerview.apply {
            setHasFixedSize(true)
            adapter = productAdapter
            layoutManager = gridLayoutManager
        }


        products.add(0,Product(viewType = ProductAdapter.HEADER))
        products.add(1, Product(viewType = ProductAdapter.TOP_PRODUCT))


        productAdapter.differ.submitList(products)


        viewModel.products.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: message ${response.message}")
            Log.d(TAG, "onViewCreated: data ${response.data}")
            Log.d(TAG, "onViewCreated: response ${response}")

            when (response) {
                is Resource.Success -> {
                    hasError = false
                    product_swipe_refresh.isRefreshing = false
                    hideErrorText()
                    hideProgressbar()
                    response.data?.let { productsResponse ->
                        val productList = mutableListOf<Product>()
                        productList.add(0, Product(viewType = ProductAdapter.HEADER))
                        productList.add(1,Product(viewType = ProductAdapter.TOP_PRODUCT))


//                        productsResponse.forEach {
//                            if (it.status == "True") {
//                                Log.d(TAG, "onViewCreated: status "+it)
//                                productList.add(it)
//                            }
//                        }
                        productList.addAll(productsResponse.filter { product ->
                             product.status == "True"
                        })
                        productAdapter.differ.submitList(productList)
                    }
                }
                is Resource.Error -> {
                    hasError = true
                    product_swipe_refresh.isRefreshing = false
                    hideProgressbar()
                    showErrorText(response.message!!)
                }
                is Resource.Loading -> {
                    product_swipe_refresh.isRefreshing = false
                    hideErrorText()
                    showProgressbar()
                }

            }
        })


        home_search_tv.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }

        initSwipeRefresh()


    }

    private fun initSwipeRefresh() {
        product_swipe_refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.loadBanners()
                viewModel.loadTopProducts()
                viewModel.loadProducts()
            }

        })
    }

    private fun showErrorText(message: String) {
        infoDialog.show()
        infoDialog.submitData(message)
        product_error_tv.visibility = View.GONE
        product_error_tv.text = message
    }

    private fun hideErrorText() {
        infoDialog.dismiss()
        product_error_tv.visibility = View.GONE
    }

    private fun showProgressbar() {
        product_progressbar.visibility = View.VISIBLE
    }


    private fun hideProgressbar() {
        product_progressbar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG2, "onResume: ")

        if (hasError){
            viewModel.loadBanners()
            viewModel.loadTopProducts()
            viewModel.loadProducts()
        }

//        val lastPosition = Constants.homeRecyclerPosition
//        Log.d(TAG, "onResume: lastPosition "+lastPosition)
//        Log.d(TAG, "onResume: listsize "+Constants.productList.size)
//
//        productAdapter.differ.submitList(Constants.productList)
//        product_recyclerview.layoutManager?.onRestoreInstanceState(Constants.homeRecyclerState)
//
//        if (product_recyclerview.layoutManager == null){
//            Log.d(TAG, "onResume: null")
//        }
//        else{
//            Log.d(TAG, "onResume: "+product_recyclerview.layoutManager!!.itemCount)
//        }

//        productAdapter.differ.submitList(Constants.productList)
//        product_recyclerview.scrollToPosition(Constants.homeRecyclerPosition)


    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG2, "onPause: ")
//        Constants.homeRecyclerPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
//
//        Constants.homeRecyclerState = product_recyclerview.layoutManager?.onSaveInstanceState()
//        Constants.productList = productAdapter.differ.currentList
    }


}