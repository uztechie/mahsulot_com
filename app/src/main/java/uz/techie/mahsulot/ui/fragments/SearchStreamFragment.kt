package uz.techie.mahsulot.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_stream_product.*
import kotlinx.coroutines.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.MarjaAdapter
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.adapter.ProductStreamAdapter
import uz.techie.mahsulot.adapter.StreamAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.ConfirmDialog
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils

@AndroidEntryPoint
class SearchStreamFragment:Fragment(R.layout.fragment_search){
    var searchType = 1

    companion object{
        const val SEARCH_PRODUCT = 1
        const val SEARCH_STREAM = 2
        const val SEARCH_MARJA = 3
    }

    lateinit var searchview: SearchView
    lateinit var productSreamAdapter: ProductStreamAdapter
    lateinit var streamAdapter: StreamAdapter
    lateinit var marjaAdapter: MarjaAdapter
    lateinit var customProgressDialog: CustomProgressDialog
    private var customerId = -1
    private var job:Job? = null



    val viewModel by viewModels<MahsulotViewModel>()
    private  val TAG = "SearchFragment"
    private var token = ""



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            searchType = SearchStreamFragmentArgs.fromBundle(it).searchType
        }

        customProgressDialog = CustomProgressDialog(requireContext())

        searchview = view.findViewById<SearchView>(R.id.search_searchview)
        searchview.requestFocusFromTouch()
        searchview.isIconified = true
        searchview.isFocusable = true
        showKeyboard(searchview.findFocus())

        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            it?.token?.let { mToken->
                token = "Token $mToken"
            }
            it?.id?.let { id->
                customerId = id
            }
        })

        productSreamAdapter = ProductStreamAdapter(requireContext(), object : ProductStreamAdapter.ProductStreamListener{
            override fun onAdLink(link: String) {
                openUrl(link)
            }

            override fun onCreateStream(product: Product) {
                createStream(product)
            }

        })

        streamAdapter = StreamAdapter(requireContext(), object : StreamAdapter.StreamListener{
            override fun onClickAdLink(link: String) {
                openUrl(link)
            }

            override fun onClickDelete(id: Int) {
                deleteStream(id)
            }

            override fun onSimpleTgLinkClick(link: String) {
                openUrl(link)
            }

            override fun onSpecialTgLinkClick(link: String) {
                openUrl(link)
            }

        })

        marjaAdapter = MarjaAdapter(object :MarjaAdapter.MarjaListener{
            override fun onItemClick(product: Product) {
                findNavController().navigate(SearchStreamFragmentDirections.actionGlobalMarjaProductDetailsFragment(product))
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
                            val list = mutableListOf<Product>()
                            list.addAll(productResponse.filter { stream ->
                                stream.status == "True"
                            })
                            productSreamAdapter.differ.submitList(list)

                            if (list.isEmpty()){
                                showErrorText(getString(R.string.malumotlar_topilmadi))
                            }
                            else{
                                hideErrorText()
                            }
                        }
                    }
                    is Resource.Error ->{
                        hideProgressbar()
                        hideErrorText()
                        Utils.toastIconError(requireActivity(), response.message!!)
                    }
                    is Resource.Loading ->{
                        hideErrorText()
                        showProgressbar()
                    }
                }


            })

        }
        else if (searchType == SEARCH_MARJA){
            search_recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = marjaAdapter
            }

            viewModel.searchProducts.observe(viewLifecycleOwner, Observer {response ->
                Log.d(TAG, "onViewCreated: search "+response.data)
                when(response){
                    is Resource.Success ->{
                        hideProgressbar()
                        response.data?.let { productResponse->
                            val list = mutableListOf<Product>()
                            list.addAll(productResponse.filter { stream ->
                                stream.marja == "True"
                            })
                            marjaAdapter.differ.submitList(list)

                            if (list.isEmpty()){
                                showErrorText(getString(R.string.malumotlar_topilmadi))
                            }
                            else{
                                hideErrorText()
                            }
                        }
                    }
                    is Resource.Error ->{
                        hideProgressbar()
                        hideErrorText()
                        Utils.toastIconError(requireActivity(), response.message!!)
                    }
                    is Resource.Loading ->{
                        hideErrorText()
                        showProgressbar()
                    }
                }


            })

        }
        else {
            search_recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = streamAdapter
            }

            viewModel.streams.observe(viewLifecycleOwner, Observer {response ->
                Log.d(TAG, "onViewCreated: search stream "+response.data)
                when(response){
                    is Resource.Success ->{
                        hideProgressbar()
                        response.data?.let { streamResponse->
                            val list = mutableListOf<Stream>()
                            list.addAll(streamResponse.filter { stream ->
                                stream.status == "true"
                            })
                            streamAdapter.differ.submitList(list)

                            if (list.isEmpty()){
                                showErrorText(getString(R.string.malumotlar_topilmadi))
                            }
                            else{
                                hideErrorText()
                            }

                        }
                    }
                    is Resource.Error ->{
                        hideErrorText()
                        hideProgressbar()
                        Utils.toastIconError(requireActivity(), response.message!!)
                    }
                    is Resource.Loading ->{
                        hideErrorText()
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
                            if (searchType == SEARCH_PRODUCT || searchType == SEARCH_MARJA){
                                viewModel.searchProducts(it)
                            }
                            else{
                                viewModel.searchStreams(token, it)
                            }

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
                            if (searchType == SEARCH_PRODUCT || searchType == SEARCH_MARJA){
                                viewModel.searchProducts(it)
                            }
                            else{
                                viewModel.searchStreams(token, it)
                            }
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
            Utils.toastIconError(requireActivity(), getString(R.string.url_mavjud_emas))
        }
    }


    private fun deleteStream(id: Int) {
        val confirmDialog = ConfirmDialog(requireContext(), object : ConfirmDialog.ConfirmDialogListener{
            override fun onOkClick() {
                viewModel.deleteStream(token, id)
                viewModel.streamResponse.observe(viewLifecycleOwner, Observer { response ->
                    Log.d(TAG, "deleteStream: " + response.data)
                    when (response) {
                        is Resource.Loading -> {
                            customProgressDialog.show()
                        }
                        is Resource.Error -> {
                            customProgressDialog.dismiss()
                            Utils.toastIconError(requireActivity(), response.message!!)
                        }
                        is Resource.Success -> {
                            customProgressDialog.dismiss()
                            response.data?.let { streamResponse ->
                                if (streamResponse.status == 200) {
                                    Utils.toastIconSuccess(requireActivity(), getString(R.string.oqim_ochirildi))
                                    viewModel.searchStreams(token, searchview.query.toString())
                                } else {
                                    Utils.toastIconError(requireActivity(), streamResponse.message)
                                }
                            }
                        }
                    }
                })
            }

        })
        confirmDialog.show()
        confirmDialog.setTitle(getString(R.string.oqimni_ochirish))
        confirmDialog.setMessage(getString(R.string.siz_rostdan_oqimni_ochirmoq))
    }


    private fun showErrorText(text:String){
        search_error_tv.visibility = View.VISIBLE
        search_error_tv.text = text
    }

    private fun hideErrorText(){
        search_error_tv.visibility = View.GONE
    }


    private fun createStream(product: Product) {
        viewModel.createStream(
            token = token,
            title = product.name!!,
            url = "url",
            status = "True",
            productId = product.id!!,
            sellerId = customerId
        )

        viewModel.streamResponse.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "createStream: " + response.data)
            when (response) {
                is Resource.Loading -> {
                    customProgressDialog.show()
                }
                is Resource.Error -> {
                    customProgressDialog.dismiss()
                    Utils.toastIconError(requireActivity(), response.message!!)
                }
                is Resource.Success -> {
                    customProgressDialog.dismiss()
                    response.data?.let { streamResponse ->
                        if (streamResponse.status == 200) {
                            job = GlobalScope.launch(Dispatchers.Main) {
                                delay(1000)
                                Utils.toastIconSuccess(requireActivity(), getString(R.string.oqim_yaratildi))
                                findNavController().navigate(SearchStreamFragmentDirections.actionSearchStreamFragmentToStreamFragment())
                            }


                        } else {
                            Utils.toastIconError(requireActivity(), streamResponse.message!!)
                        }
                    }
                }
            }
        })

    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }


}