package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_private_info.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.data.MahsulotViewModel

@AndroidEntryPoint
class PrivateInfoFragment:Fragment(R.layout.fragment_private_info) {
    private val viewModel:MahsulotViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()


        viewModel.getUser().observe(viewLifecycleOwner, Observer { user->
            info_phone.text = user.phone
            info_name.setText(user.first_name)
            info_lastname.setText(user.last_name)
            info_birhday.setText(user.birthday)
        })

    }


    private fun initToolbar(){
        toolbar_layout.setBackgroundColor(resources.getColor(R.color.background_color))
        toolbar_title.text = getString(R.string.shaxsiy_ma_lumotlar)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }
}