package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_cabinet.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R

class CabinetFragment :Fragment(R.layout.fragment_cabinet) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        (activity as MainActivity).updateStatusLight()

        cabinet_login_btn.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionGlobalLoginFragment())
        }


    }

    private fun initToolbar(){

        toolbar_title.text = getString(R.string.kabinet)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionGlobalSearchFragment())
        }

    }

}