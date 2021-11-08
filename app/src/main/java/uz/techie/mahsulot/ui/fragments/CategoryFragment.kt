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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.CategoryAdapter
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.adapter.SliderAdapter
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.Category
import uz.techie.mahsulot.model.Product

@AndroidEntryPoint
class CategoryFragment:Fragment(R.layout.fragment_category){
    private lateinit var categoryAdapter: CategoryAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        categoryAdapter = CategoryAdapter()
        category_recyclerview.setHasFixedSize(true)
        category_recyclerview.layoutManager = GridLayoutManager(context, 3)
        category_recyclerview.adapter = categoryAdapter


        categoryAdapter.submitList(categoryList())


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


}