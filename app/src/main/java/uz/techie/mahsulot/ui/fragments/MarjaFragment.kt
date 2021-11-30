package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_stream_product.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.ProductStreamAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_stream.*
import kotlinx.android.synthetic.main.fragment_stream_product.product_stream_recyclerview
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.adapter.MarjaAdapter
import uz.techie.mahsulot.adapter.StreamAdapter
import uz.techie.mahsulot.dialog.ConfirmDialog
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_MARJA
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_PRODUCT
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_STREAM
import kotlin.math.log


@AndroidEntryPoint
class MarjaFragment:Fragment(R.layout.fragment_stream) {
    private val viewModel:MahsulotViewModel by viewModels()
    lateinit var marjaAdapter: MarjaAdapter
    var token = ""
    private val TAG = "MarjaFragment"
    lateinit var customProgressDialog: CustomProgressDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        customProgressDialog = CustomProgressDialog(requireContext())
        viewModel.loadProducts()

        viewModel.getUser().observe(viewLifecycleOwner, Observer { user->
            user?.token?.let {
                token = "Token $it"

            }
        })

        marjaAdapter = MarjaAdapter(object :MarjaAdapter.MarjaListener{
            override fun onItemClick(product: Product) {
                findNavController().navigate(MarjaFragmentDirections.actionGlobalMarjaProductDetailsFragment(product))
            }

        })

        stream_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = marjaAdapter
        }

        viewModel.products.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: "+response.data)
            when(response){
                is Resource.Loading->{
                    stream_progressbar.visibility = View.VISIBLE
                    hideErrorText()
                }
                is Resource.Error->{
                    stream_progressbar.visibility = View.GONE
                    response.message?.let {
                        Utils.toastIconError(requireActivity(), it)
                    }
                    hideErrorText()
                }
                is Resource.Success ->{
                    hideErrorText()
                    stream_progressbar.visibility = View.GONE
                    response.data?.let { streamResponse ->
                        val list = mutableListOf<Product>()
                        list.addAll(streamResponse.filter { stream ->
                            stream.marja == "True"
                        })

                        marjaAdapter.differ.submitList(list)

                        if (list.isEmpty()){
                            showErrorText()
                        }
                    }
                }
            }

        })




    }

    private fun initToolbar(){
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.marja_market)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.setOnClickListener {
            findNavController().navigate(MarjaFragmentDirections.actionGlobalSearchStreamFragment(SEARCH_MARJA))
        }

    }


    private fun showErrorText(){
        stream_textview.text = getString(R.string.hozircha_marjalar_mavjud_emas)
        stream_textview.visibility = View.VISIBLE
    }

    private fun hideErrorText(){
        stream_textview.visibility = View.GONE
    }





}