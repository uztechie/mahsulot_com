package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.adapter_body.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.adapter.SliderAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.MainModel
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Resource

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var productAdapter: ProductAdapter
    lateinit var sliderAdapter: SliderAdapter

    private lateinit var viewModel: MahsulotViewModel
    private val TAG = "HomeFragment"
    private val mainList:MutableList<MainModel> = mutableListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setSupportActionBar(toolbar)
        viewModel = (activity as MainActivity).viewModel

        productAdapter = ProductAdapter()


        mainList.add(MainModel(1, sliderList()))
        mainList.add(MainModel(1, sliderList()))
        productAdapter.differ.submitList(mainList)
        product_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = productAdapter
        }


        viewModel.products.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: message ${response.message}")
            Log.d(TAG, "onViewCreated: data ${response.data}")
            Log.d(TAG, "onViewCreated: response ${response}")

            when (response) {
                is Resource.Success -> {
                    hideErrorText()
                    hideProgressbar()
                    response.data?.let { productsResponse->
                        productsResponse.forEach { product ->
                            mainList.add(MainModel(2, null, product))
                        }
                        productAdapter.differ.submitList(mainList)




                        Log.d(TAG, "onViewCreated: size "+mainList.size)
//                        mainAdapter.submitList(mainList)
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


    private fun sliderList(): List<Banner> {
        val list = mutableListOf<Banner>()
        val img1 =
            "https://images.pexels.com/photos/2101137/pexels-photo-2101137.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img2 =
            "https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        val img3 =
            "https://images.pexels.com/photos/699122/pexels-photo-699122.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img4 =
            "https://images.pexels.com/photos/1194760/pexels-photo-1194760.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        list.add(Banner(1, img1, "title"))
        list.add(Banner(1, img2, "title"))
        list.add(Banner(1, img3, "title"))
        list.add(Banner(1, img4, "title"))

        return list
    }


}