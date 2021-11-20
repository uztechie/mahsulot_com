package uz.techie.mahsulot.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_stream_product.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.adapter.ProductStreamAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils

@AndroidEntryPoint
class SearchStreamFragment:Fragment(R.layout.fragment_search){
    var searchType = 1

    companion object{
        const val SEARCH_PRODUCT = 1
        const val SEARCH_STREAM = 2
    }

    lateinit var productSreamAdapter: ProductStreamAdapter
    val viewModel by viewModels<MahsulotViewModel>()
    private  val TAG = "SearchFragment"



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            searchType = SearchStreamFragmentArgs.fromBundle(it).searchType
        }

        val searchview = view.findViewById<SearchView>(R.id.search_searchview)
        searchview.requestFocusFromTouch()
        searchview.isIconified = true
        searchview.isFocusable = true
        showKeyboard(searchview.findFocus())

        productSreamAdapter = ProductStreamAdapter(requireContext(), object : ProductStreamAdapter.ProductStreamListener{
            override fun onAdLink(link: String) {
                openUrl(link)
            }

            override fun onCreateStream(product: Product) {

            }

        })

        if (searchType == SEARCH_PRODUCT){
            search_recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = productSreamAdapter
            }

            viewModel.searchProducts.observe(viewLifecycleOwner, Observer {response ->
                Log.d(TAG, "onViewCreated: search "+response.data)
                when(response){
                    is Resource.Success ->{
                        hideProgressbar()
                        response.data?.let { productResponse->
                            productSreamAdapter.differ.submitList(productResponse)
                        }
                    }
                    is Resource.Error ->{
                        hideProgressbar()
                        Utils.showMessage(requireView(), response.message!!)
                    }
                    is Resource.Loading ->{
                        showProgressbar()
                    }
                }


            })

        }






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



    private fun showProgressbar() {
        search_progressbar.visibility = View.VISIBLE
    }


    private fun hideProgressbar() {
        search_progressbar.visibility = View.GONE
    }


    private fun openUrl(url:String){
        if (url.startsWith("http") || url.startsWith("https")){
            val uri: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        else{
            Utils.showMessage(requireView(), getString(R.string.url_mavjud_emas))
        }
    }


}