package uz.techie.mahsulot.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_cabinet.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.ConfirmDialog
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.util.Utils
import java.util.*
import androidx.activity.OnBackPressedCallback
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import uz.techie.mahsulot.util.Constants


@AndroidEntryPoint
class CabinetFragment :Fragment(R.layout.fragment_cabinet) {
    private val viewModel:MahsulotViewModel by viewModels()
    private var user:User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(CabinetFragmentDirections.actionGlobalHomeFragment())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)



        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            user = it
            if (user == null){
                cabinet_info_layout.visibility = View.GONE
                cabinet_private_layout.visibility = View.GONE
                cabinet_login_btn.visibility = View.VISIBLE
                cabinet_logout_btn.visibility = View.GONE
            }
            else{
                cabinet_info_layout.visibility = View.VISIBLE
                cabinet_private_layout.visibility = View.VISIBLE
                cabinet_login_btn.visibility = View.GONE
                cabinet_logout_btn.visibility = View.VISIBLE
            }
        })


        cabinet_login_btn.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionGlobalLoginFragment())
        }

        cabinet_logout_btn.setOnClickListener {
            val confirmDialog = ConfirmDialog(requireContext(), object :
                ConfirmDialog.ConfirmDialogListener {
                override fun onOkClick() {
                    logout()
                }
            })

            confirmDialog.show()
            confirmDialog.setTitle(getString(R.string.tizimdan_chiqish))
            confirmDialog.setMessage(getString(R.string.siz_rostdan_chiqmoqchimisiz))

        }

        cabinet_profile.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionCabinetFragmentToProfileFragment())
        }

        cabinet_products.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionCabinetFragmentToProductStreamFragment())
        }

        cabinet_stream.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionCabinetFragmentToStreamFragment())
        }

        cabinet_statistics.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionCabinetFragmentToStatisticFragment())
        }

        cabinet_private_info.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionCabinetFragmentToPrivateInfoFragment())
        }

        cabinet_withdraw.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionCabinetFragmentToWithdrawFragment())
        }

        cabinet_marja_market.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionCabinetFragmentToMarjaFragment())
        }

        cabinet_concurs.setOnClickListener {
            findNavController().navigate(CabinetFragmentDirections.actionCabinetFragmentToCompetitionFragment())
        }

    }

    private fun logout() {
        viewModel.getUser().observe(viewLifecycleOwner, Observer { user->
            user?.phone?.let {
                Firebase.messaging.unsubscribeFromTopic(it)
            }
            user?.gender?.let {
                if (it == "True"){
                    Firebase.messaging.unsubscribeFromTopic(Constants.MALE)
                }
                else{
                    Firebase.messaging.unsubscribeFromTopic(Constants.FEMALE)
                }
            }
            Firebase.messaging.unsubscribeFromTopic(Constants.LOGGED)
            viewModel.deleteUser()
        })


        cabinet_info_layout.visibility = View.GONE
        cabinet_private_layout.visibility = View.GONE
        cabinet_login_btn.visibility = View.VISIBLE
        cabinet_logout_btn.visibility = View.GONE
    }

    private fun initToolbar(){
        toolbar_layout.setBackgroundColor(resources.getColor(R.color.background_color))
        toolbar_title.text = getString(R.string.kabinet)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }


}