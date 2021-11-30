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
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_PRODUCT


@AndroidEntryPoint
class ProductStreamFragment : Fragment(R.layout.fragment_stream_product) {
    private val viewModel: MahsulotViewModel by viewModels()
    lateinit var productAdapter: ProductStreamAdapter
    lateinit var customProgressDialog: CustomProgressDialog

    private val TAG = "ProductStreamFragment"
    private var token = ""
    private var customerId = -1

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
        customProgressDialog = CustomProgressDialog(requireContext())

        viewModel.getUser().observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                token = "Token ${it.token}"
                customerId = it.id
            }
        })


        productAdapter = ProductStreamAdapter(
            requireContext(),
            object : ProductStreamAdapter.ProductStreamListener {
                override fun onAdLink(link: String) {
                    openUrl(link)
                }

                override fun onCreateStream(product: Product) {
                    createStream(product)
                }

            })

        product_stream_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = productAdapter
        }

        viewModel.streamProducts.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: " + response.data)
            when (response) {
                is Resource.Loading -> {
                    product_stream_progressbar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    product_stream_progressbar.visibility = View.GONE
                    response.message?.let {
                        Utils.toastIconError(requireActivity(), it)
                    }
                }
                is Resource.Success -> {
                    product_stream_progressbar.visibility = View.GONE
                    response.data?.let { productResponse ->
                        val list = mutableListOf<Product>()
                        list.addAll(productResponse.filter { product ->
                            product.status == "True"
                        })

                        productAdapter.differ.submitList(list)
                    }
                }
            }

        })
        viewModel.loadStreamProducts()




    }


    private fun initToolbar() {
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.mahsulotlar)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.setOnClickListener {
            findNavController().navigate(
                ProductStreamFragmentDirections.actionGlobalSearchStreamFragment(
                    SEARCH_PRODUCT
                )
            )
        }

    }


    private fun openUrl(url: String) {
        if (url.startsWith("http") || url.startsWith("https")) {
            val uri: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } else {
            Utils.showMessage(requireView(), getString(R.string.url_mavjud_emas))
        }
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
                            Utils.toastIconSuccess(requireActivity(), getString(R.string.oqim_yaratildi))
                            findNavController().navigate(ProductStreamFragmentDirections.actionProductStreamFragmentToStreamFragment())
                        } else {
                            Utils.toastIconError(requireActivity(), streamResponse.message!!)
                        }
                    }
                }
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchView: SearchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }


}