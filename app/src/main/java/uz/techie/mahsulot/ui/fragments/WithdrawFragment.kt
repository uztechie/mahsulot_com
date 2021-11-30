package uz.techie.mahsulot.ui.fragments

import android.animation.Animator
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_withdraw.*
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.model.ProfileResponse
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils
import android.animation.AnimatorListenerAdapter

import android.view.animation.AccelerateDecelerateInterpolator


import com.yy.mobile.rollingtextview.CharOrder.Alphabet

import com.yy.mobile.rollingtextview.strategy.Strategy
import kotlinx.android.synthetic.main.fragment_withdraw_history.*
import kotlinx.android.synthetic.main.progress_button.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.dialog.PositiveNegativeDialog
import uz.techie.mahsulot.model.UserData
import uz.techie.mahsulot.util.Constants


@AndroidEntryPoint
class WithdrawFragment : Fragment(R.layout.fragment_withdraw),
    PositiveNegativeDialog.PositiveNegativeListener {
    val viewModel: MahsulotViewModel by viewModels()
    private val TAG = "WithdrawFragment"
    lateinit var customProgressDialog: CustomProgressDialog
    private var user: User? = null
    private var token: String = ""
    private var userData = UserData()
    private lateinit var positiveNegativeDialog: PositiveNegativeDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        positiveNegativeDialog = PositiveNegativeDialog(requireContext(), this)

        initToolbar()
        initCardNumber()
        saveBtnReset()

        customProgressDialog = CustomProgressDialog(requireContext())


        viewModel.profile.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: " + response.data)
            when (response) {
                is Resource.Loading -> {
                    customProgressDialog.show()
                }
                is Resource.Error -> {
//                    Utils.toastIconError(requireActivity(),"Error: " + response.message)
                    customProgressDialog.dismiss()
                    positiveNegativeDialog.show()
                    positiveNegativeDialog.setData(getString(R.string.xatolik), "Error "+response.message, false)
                }
                is Resource.Success -> {
                    customProgressDialog.dismiss()
                    response.data?.let {
                        showProfileData(it)
                       it.data?.let { data->
                           userData = data
                       }

                    }

                }
            }


        })
        viewModel.getUser().observe(viewLifecycleOwner, Observer { mUser ->
            user = mUser
            mUser?.token?.let {
                token = "Token $it"
                Log.d(TAG, "onViewCreated: token " + token)
                viewModel.loadProfile(token)
            }
        })

        viewModel.withdraw.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: withdraw "+response)
            when(response){
                is Resource.Loading->{
                    saveBtnProgress()
                }
                is Resource.Error->{
                    saveBtnReset()
                    positiveNegativeDialog.show()
                    positiveNegativeDialog.setData(getString(R.string.xatolik), "Error "+response.message, false)
                }
                is Resource.Success->{
                    response.data?.let { withdrawResponse ->
                        if (withdrawResponse.status == 200){
                            saveBtnSuccess()
                            viewModel.loadProfile(token)
                            positiveNegativeDialog.show()
                            positiveNegativeDialog.setData(getString(R.string.muvaffaqiyatli), getString(R.string.sorovingiz_qabul_qilindi), true)

                        }
                        else{
                            saveBtnReset()
                            positiveNegativeDialog.show()
                            positiveNegativeDialog.setData(getString(R.string.xatolik), "Error "+withdrawResponse.message, false)
                        }
                    }
                }

            }
        })


        progress_button_layout.setOnClickListener {
            val totalAmount = userData.hisobi!!
            val sAmount = withdraw_amount.text.toString()
            var amount = 0
            var cardNumber = withdraw_card_number.text.toString()
            cardNumber = cardNumber.replace(" ", "")

            val myToken = Constants.MY_TOKEN
            val sellerId = user!!.id
            val status = 10
            val desc = 10

            if (cardNumber.length<=15){
                Utils.toastIconError(requireActivity(), getString(R.string.karta_raqamini_toliq_kiriting))
                return@setOnClickListener
            }

            if (sAmount.isNotEmpty()){
                amount = sAmount.toInt()
            }

            if (amount<39999){
                Utils.toastIconError(requireActivity(), getString(R.string.kamida_40000_yechishingiz_mumkin))
                return@setOnClickListener
            }



            viewModel.makePayment(
                myToken,
                token,
                sellerId,
                amount,
                cardNumber,
                status,
                desc,
                totalAmount
            )




        }


        withdraw_history_btn.setOnClickListener {
            findNavController().navigate(WithdrawFragmentDirections.actionWithdrawFragmentToWithdrawHistoryFragment())
        }



    }

    private fun showProfileData(profile: ProfileResponse) {
        profile.data?.let { data ->
            mainBalanceAnim("${data.hisobi?.let { Utils.toMoney(it) }} ${getString(R.string.som)}")
            holdBalanceAnim("${data.hold?.let { Utils.toMoney(it) }} ${getString(R.string.som)}")
        }
    }


    private fun showMessage(message: String?) {
        message?.let {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        }
    }


    private fun initToolbar() {
        toolbar_title.text = getString(R.string.mablag_ni_yechish)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }

    private fun initCardNumber(){
        val nonDigits = Regex("[^\\d]")
        var current = ""
        withdraw_card_number.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    val userInput = s.toString().replace(nonDigits,"")
                    if (userInput.length <= 16) {
                        current = userInput.chunked(4).joinToString(" ")
                        s?.filters = arrayOfNulls<InputFilter>(0)
                    }
                    s?.replace(0, s.length, current, 0, current.length)
                }

                if (s.toString().replace(" ", "").isEmpty()){
                    withdraw_card_number.background = ContextCompat.getDrawable(requireContext(), R.drawable.phone_et_bg)
                }
                else if (s.toString().replace(" ", "").length <16){
                    withdraw_card_number.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_number_et_error)
                }
                else{
                    withdraw_card_number.background = ContextCompat.getDrawable(requireContext(), R.drawable.card_number_et_success)
                }

            }

        })
    }



    private fun mainBalanceAnim(amount:String){
        withdraw_main_balance_tv.animationDuration = 1000L
        withdraw_main_balance_tv.charStrategy = Strategy.NormalAnimation()
        withdraw_main_balance_tv.addCharOrder(Alphabet)
        withdraw_main_balance_tv.animationInterpolator = AccelerateDecelerateInterpolator()
        withdraw_main_balance_tv.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                //finsih
            }
        })
        withdraw_main_balance_tv.setText(amount)
    }

    private fun holdBalanceAnim(amount:String){
        withdraw_hold_balance_tv.animationDuration = 1000L
        withdraw_hold_balance_tv.charStrategy = Strategy.NormalAnimation()
        withdraw_hold_balance_tv.addCharOrder(Alphabet)
        withdraw_hold_balance_tv.animationInterpolator = AccelerateDecelerateInterpolator()
        withdraw_hold_balance_tv.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                //finsih
            }
        })
        withdraw_hold_balance_tv.setText(amount)
    }


    private fun saveBtnProgress(){
        progress_button_title.text = getString(R.string.bajarilmoqda)
        progress_button_progressbar.visibility = View.VISIBLE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.light_blue))
        progress_button_layout.isEnabled = false
    }

    private fun saveBtnSuccess(){
        progress_button_title.text = getString(R.string.sorov_yuborildi)
        progress_button_progressbar.visibility = View.GONE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.green))
    }
    private fun saveBtnReset(){
        progress_button_layout.isEnabled = true
        progress_button_title.text = getString(R.string.mablag_ni_yechish)
        progress_button_progressbar.visibility = View.GONE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.light_blue))
    }

    override fun onBackBtnClick() {
        findNavController().popBackStack()
    }


}