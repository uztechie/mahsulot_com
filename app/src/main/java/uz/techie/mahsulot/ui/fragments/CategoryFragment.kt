package uz.techie.mahsulot.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.CategoryAdapter
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.adapter.SliderAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.InfoDialog
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.Category
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.Resource

@AndroidEntryPoint
class CategoryFragment:Fragment(R.layout.fragment_category), CategoryAdapter.CategoryInterface {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var viewModel:MahsulotViewModel
    private val TAG = "CategoryFragment"
    private lateinit var gridLayoutManager:GridLayoutManager
    lateinit var infoDialog: InfoDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        infoDialog = InfoDialog(requireContext())
        categoryAdapter = CategoryAdapter(this)
        initToolbar()

        gridLayoutManager = GridLayoutManager(requireContext(), 3)
        category_recyclerview.setHasFixedSize(true)
        category_recyclerview.layoutManager = gridLayoutManager
        category_recyclerview.adapter = categoryAdapter

        viewModel.categories.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: observer "+response.message)
            Log.d(TAG, "onViewCreated: observer "+response.data)
            when (response) {
                is Resource.Success -> {
                    hideErrorText()
                    hideProgressbar()
                    response.data?.let { productsResponse ->
                        categoryAdapter.submitList(productsResponse)
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



    }

    private fun initToolbar(){
        toolbar_title.text = getString(R.string.kataloglar)
        toolbar_title.visibility = View.VISIBLE

        toolbar_btnClose.setOnClickListener {
            findNavController().navigate(CategoryFragmentDirections.actionGlobalHomeFragment())
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }

    private fun categoryList(): MutableList<Category>? {
        val img1 = "https://images.pexels.com/photos/2101137/pexels-photo-2101137.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img2 = "https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        val img3 = "https://images.pexels.com/photos/699122/pexels-photo-699122.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img4 = "https://images.pexels.com/photos/1194760/pexels-photo-1194760.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"



        val list = mutableListOf<Category>()
        list.add(Category(1, "Kompyuterkar va kitoblar", img1))
        list.add(Category(1, "Kiyimlar", img2))
        list.add(Category(1, "Uskunalar", img3))
        list.add(Category(1, "Mashinalar", img3))
        list.add(Category(1, "Samalyotlar", "sd"))
        return list
    }


    private fun showProgressbar() {
        category_progressbar.visibility = View.VISIBLE
    }


    private fun hideProgressbar() {
        category_progressbar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCategories()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onItemClick(category: Category) {
        category.id?.let {
            findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToProductFragment(category))
        }


    }

    private fun showErrorText(message: String) {
        infoDialog.show()
        infoDialog.submitData(message)
    }

    private fun hideErrorText() {
        infoDialog.dismiss()
    }




}