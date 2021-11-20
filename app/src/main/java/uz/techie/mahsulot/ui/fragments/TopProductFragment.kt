package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_top_product.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.TopProductAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Resource

class TopProductFragment:Fragment(R.layout.fragment_top_product) {

    lateinit var viewModel: MahsulotViewModel
    lateinit var topProductAdapter: TopProductAdapter
    private val TAG = "TopProductFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as  MainActivity).viewModel

        topProductAdapter = TopProductAdapter(object :TopProductAdapter.TopProductListener{
            override fun onItemClick(product: Product) {
                findNavController().navigate(TopProductFragmentDirections.actionGlobalProductDetailsFragment(product))
            }

        })

        top_product_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = topProductAdapter
        }

        viewModel.topProducts.observe(viewLifecycleOwner, Observer { response->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { productsResponse ->
                        topProductAdapter.differ.submitList(productsResponse)
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {

                }
            }
        })

    }
}