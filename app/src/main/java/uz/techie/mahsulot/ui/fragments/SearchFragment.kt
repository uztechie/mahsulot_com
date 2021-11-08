package uz.techie.mahsulot.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.ProductAdapter
import uz.techie.mahsulot.adapter.SliderAdapter
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.Product

@AndroidEntryPoint
class SearchFragment:Fragment(R.layout.fragment_search){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchview = view.findViewById<SearchView>(R.id.search_searchview)
        searchview.requestFocusFromTouch()
        searchview.isIconified = true
        searchview.isFocusable = true
        showKeyboard(searchview.findFocus())





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






}