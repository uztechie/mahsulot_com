package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.adapter.SliderAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Resource
import kotlin.math.log

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var productAdapter: ProductAdapter
    private lateinit var viewModel: MahsulotViewModel
    private val TAG = "HomeFragment"
    private val TAG2 = "FFFFFFFFFF"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG2, "onCreate: ")
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





        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {

                Log.d(TAG, "getSpanSize: "+productAdapter.getItemViewType(position))
                Log.d(TAG, "getSpanSize:  "+productAdapter.getItemViewType(position))

                if (productAdapter.getItemViewType(position)==ProductAdapter.HEADER){
                    return 2
                }
                else{
                    return 1
                }
            }

        }


        product_recyclerview.apply {
            setHasFixedSize(true)
            adapter = productAdapter
//            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager = gridLayoutManager
        }

        productAdapter.differ.submitList(listOf(Product(viewType = ProductAdapter.HEADER)))


        val sliderFragment = SliderFragment()
        productAdapter = ProductAdapter(object : ProductAdapter.OnOpenFragment{
            override fun openFragment(viewTypeId: Int) {
                try {
                    val fragmentManager = childFragmentManager
                    if (sliderFragment.isAdded){
                        fragmentManager.popBackStackImmediate(SliderFragment::class.simpleName, 0)
                    }
                    else{
                        fragmentManager.beginTransaction()
                            .replace(viewTypeId, sliderFragment)
                            .commitAllowingStateLoss()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

            override fun onItemClick(product: Product) {
                Log.d(TAG, "onItemClick: "+product.id)
//                findNavController().navigate(HomeFragmentDirections.actionGlobalProductDetailsFragment(product))
            }

        })



        viewModel.products.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: message ${response.message}")
            Log.d(TAG, "onViewCreated: data ${response.data}")
            Log.d(TAG, "onViewCreated: response ${response}")

            when (response) {
                is Resource.Success -> {
                    hideErrorText()
                    hideProgressbar()
                    response.data?.let { productsResponse->
                        val productList = mutableListOf<Product?>()
                        productList.addAll(productsResponse)
                        productList.add(0,Product(viewType = ProductAdapter.HEADER))


                        productAdapter.differ.submitList(productList)
//                        Log.d(TAG, "onViewCreated: success ${productsResponse.size}")
                    }
                }
                is Resource.Error -> {
                    hideProgressbar()
                    showErrorText(response.message!!)
                }
                is Resource.Loading -> {
                    hideErrorText()
                    showProgressbar()
                }

            }
        })


        home_search_tv.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }


    }

    private fun showErrorText(message: String) {
        product_error_tv.visibility = View.VISIBLE
        product_error_tv.text = message
    }

    private fun hideErrorText() {
        product_error_tv.visibility = View.GONE
    }

    private fun showProgressbar() {
        product_progressbar.visibility = View.VISIBLE
    }


    private fun hideProgressbar() {
        product_progressbar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG2, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG2, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG2, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG2, "onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG2, "onDestroyView: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG2, "onDetach: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG2, "onDestroy: ")
    }





}