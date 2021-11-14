package uz.techie.mahsulot.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Resource

@AndroidEntryPoint
class SearchFragment:Fragment(R.layout.fragment_search){

    lateinit var productAdapter: ProductAdapter
    val viewModel by viewModels<MahsulotViewModel>()
    private  val TAG = "SearchFragment"



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchview = view.findViewById<SearchView>(R.id.search_searchview)
        searchview.requestFocusFromTouch()
        searchview.isIconified = true
        searchview.isFocusable = true
        showKeyboard(searchview.findFocus())

        productAdapter = ProductAdapter(object : ProductAdapter.OnOpenFragment{
            override fun openFragment(viewTypeId: Int, viewType:Int) {

            }

            override fun onItemClick(product: Product) {
                findNavController().navigate(SearchFragmentDirections.actionGlobalProductDetailsFragment(product))
            }

        })

        search_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }

        viewModel.searchProducts.observe(viewLifecycleOwner, Observer {response ->
            Log.d(TAG, "onViewCreated: search "+response.data)
            when(response){
                is Resource.Success ->{
                    hideErrorText()
                    hideProgressbar()
                    response.data?.let { productResponse->
                        productAdapter.differ.submitList(productResponse)
                    }
                }
                is Resource.Error ->{
                    hideProgressbar()
                    showErrorText(response.message!!)
                }
                is Resource.Loading ->{
                    showProgressbar()
                    hideErrorText()
                }
            }


        })


        var job:Job? = null
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(500)
                    query?.let {
                        if (it.isNotEmpty() || it.isNotBlank()){
                            viewModel.searchProducts(it)
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(500)
                    newText?.let {
                        if (it.isNotEmpty() || it.isNotBlank()){
                            viewModel.searchProducts(it)
                        }
                    }
                }

                return false
            }

        })



        search_close_btn.setOnClickListener {
            closeKeyboard()
            findNavController().popBackStack()
        }

    }

    fun closeKeyboard() {
        val view = search_searchview
        val methodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        methodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboard(view: View?) {
        val methodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }




    private fun showErrorText(message: String) {
        search_error_tv.visibility = View.VISIBLE
        search_error_tv.text = message
    }

    private fun hideErrorText() {
        search_error_tv.visibility = View.GONE
    }

    private fun showProgressbar() {
        search_progressbar.visibility = View.VISIBLE
    }


    private fun hideProgressbar() {
        search_progressbar.visibility = View.GONE
    }



}