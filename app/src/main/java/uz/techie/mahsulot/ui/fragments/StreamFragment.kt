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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_stream.*
import kotlinx.android.synthetic.main.fragment_stream_product.product_stream_recyclerview
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.adapter.StreamAdapter
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_PRODUCT


@AndroidEntryPoint
class StreamFragment:Fragment(R.layout.fragment_stream) {
    private val viewModel:MahsulotViewModel by viewModels()
    lateinit var streamAdapter: StreamAdapter
    var token = ""

    private val TAG = "ProductStreamFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        viewModel.getUser().observe(viewLifecycleOwner, Observer { user->
            user.token?.let {
                token = "Token $it"
                viewModel.loadStreams(token)
            }
        })

        streamAdapter = StreamAdapter(requireContext(), object :StreamAdapter.StreamListener{
            override fun onClickAdLink(link: String) {
                openUrl(link)
            }

            override fun onClickDelete(id: Int) {

            }

        })

        stream_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = streamAdapter
        }

        viewModel.streams.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: "+response.data)
            when(response){
                is Resource.Loading->{
                    stream_progressbar.visibility = View.VISIBLE
                }
                is Resource.Error->{
                    stream_progressbar.visibility = View.GONE
                    response.message?.let {
                        Utils.showMessage(requireView(), it)
                    }
                }
                is Resource.Success ->{
                    stream_progressbar.visibility = View.GONE
                    response.data?.let { productResponse ->
                        streamAdapter.differ.submitList(productResponse)

                        if (productResponse.isEmpty()){
                            Toast.makeText(requireContext(), getString(R.string.sizda_oqimlar_mavjud_emas), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

        })




    }


    private fun initToolbar(){
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.oqimlar)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.setOnClickListener {
//            findNavController().navigate(ProductStreamFragmentDirections.actionGlobalSearchStreamFragment(SEARCH_PRODUCT))
        }

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