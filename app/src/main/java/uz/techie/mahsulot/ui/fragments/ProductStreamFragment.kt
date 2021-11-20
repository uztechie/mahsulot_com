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
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_PRODUCT


@AndroidEntryPoint
class ProductStreamFragment:Fragment(R.layout.fragment_stream_product) {
    private val viewModel:MahsulotViewModel by viewModels()
    lateinit var productAdapter:ProductStreamAdapter

    private val TAG = "ProductStreamFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        (activity as MainActivity).setSupportActionBar(toolbar)
//        (activity as MainActivity).supportActionBar!!.title = getString(R.string.mahsulotlar)
//        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
//        toolbar.setNavigationOnClickListener{
//            findNavController().popBackStack()
//        }

        initToolbar()


        productAdapter = ProductStreamAdapter(requireContext(), object :ProductStreamAdapter.ProductStreamListener{
            override fun onAdLink(link: String) {
                openUrl(link)
            }

            override fun onCreateStream(product: Product) {

            }

        })

        product_stream_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = productAdapter
        }

        viewModel.streamProducts.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: "+response.data)
            when(response){
                is Resource.Loading->{
                    product_stream_progressbar.visibility = View.VISIBLE
                }
                is Resource.Error->{
                    product_stream_progressbar.visibility = View.GONE
                    response.message?.let {
                        Utils.showMessage(requireView(), it)
                    }
                }
                is Resource.Success ->{
                    product_stream_progressbar.visibility = View.GONE
                    response.data?.let { productResponse ->
                        productAdapter.differ.submitList(productResponse)
                    }
                }
            }

        })
        viewModel.loadStreamProducts()



    }


    private fun initToolbar(){
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.mahsulotlar)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.setOnClickListener {
            findNavController().navigate(ProductStreamFragmentDirections.actionGlobalSearchStreamFragment(SEARCH_PRODUCT))
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchView:SearchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }


}