package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_cabinet.*
import kotlinx.android.synthetic.main.fragment_login.*
import okio.Utf8
import uz.techie.mahsulot.R
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.dialog.InfoDialog
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val START_TIME = 120000L
    private var leftTime = START_TIME
    private lateinit var countDownTimer: CountDownTimer
    private var isTimerRunning = false

    lateinit var customProgressDialog: CustomProgressDialog
    private var phoneNumber = ""
    val viewModel: MahsulotViewModel by viewModels()
    private val TAG = "LoginFragment"
    private var sPhone = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        customProgressDialog = CustomProgressDialog(requireContext())


        phone_input_change_phone_tv.setOnClickListener {
            hideCodeInput(sPhone)

            phone_input_code_notreceived_tv.visibility = View.GONE
            phone_input_change_phone_tv.visibility = View.GONE

            login_btn_takecode.visibility = View.VISIBLE
            phone_input_phone_edittext.isEnabled = true
            phone_input_phone_edittext.requestFocus()
            phone_input_phone_edittext.isFocusable = true
        }

        phone_input_code_notreceived_tv.setOnClickListener {
            requestCode(sPhone)
            phone_input_code_notreceived_tv.visibility = View.GONE
            phone_input_change_phone_tv.visibility = View.GONE


        }

        takeCode()

        login_btn_confirm.setOnClickListener {
            val sCode = phone_input_code_et.text.toString()
            if (sCode.length > 5) {
                confirmCode(sCode)
            } else {
                showMessage(getString(R.string.iltimos_tasdiqlash_kodini_toliq_kiriting))
            }
        }


    }

    private fun takeCode() {
        login_btn_takecode.setOnClickListener {

            sPhone = "998" + phone_input_phone_edittext.unmaskedText
            if (sPhone.length < 12) {
                phone_input_phone_container.error = getString(R.string.mavjud_telefon_raqami)
                return@setOnClickListener
            } else {
                phone_input_phone_container.error = null
            }

            if (!Utils.isNetworkAvailable(requireContext())) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.internetga_ulanmagan),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            requestCode(sPhone)
            customProgressDialog.show()

        }
    }

    private fun confirmCode(sCode: String) {
        viewModel.confirmSms(sPhone, sCode)
        viewModel.smsConfirmation.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "confirmCode: " + response)
            when (response) {
                is Resource.Success -> {
                    customProgressDialog.dismiss()
                    response.data?.let { smsResponse ->
                        when (smsResponse.status) {
                            200 -> {
                                smsResponse.user?.let {
                                    pauseTimer()
                                    saveUser(smsResponse.user)
                                }
                            }
                            201 -> {
                                pauseTimer()
                                Log.d(TAG, "confirmCode: user not found ")
                                navigateToCabinet()
                            }

                            else -> {
                                smsResponse.message?.let { showMessage(it) }
                            }
                        }


                    }
                }
                is Resource.Error -> {
                    customProgressDialog.dismiss()
                    response.message?.let {
                        showMessage(it)
                    }

                }

                is Resource.Loading -> {
                    customProgressDialog.show()
                }
            }
        })

    }

    private fun navigateToCabinet() {
        Log.d(TAG, "navigateToCabinet: ")
        if (findNavController().currentDestination?.id == R.id.loginFragment) {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment(
                    sPhone
                )
            )
        }
    }

    private fun saveUser(user: User) {
        viewModel.deleteUser()
        viewModel.insertUser(user)
//        showMessage("User saved")
        Log.d(TAG, "saveUser: ")
        if (findNavController().currentDestination?.id == R.id.loginFragment) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCabinetFragment())
        }


    }


    private fun requestCode(sPhone: String) {
        resetTimer()
        startTimer()

        phone_input_code_info_tv.text =
            "${getString(R.string.tasdiqlash_kodi_ushbu_raqamga_yuborildi)}\n $sPhone"

        viewModel.sms.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "requestCode: " + response)
            when (response) {
                is Resource.Success -> {
                    customProgressDialog.dismiss()
                    response.data?.let { smsResponse ->
                        if (smsResponse.status == 200) {
                            showCodeInput(sPhone)
                        }
                    }
                }
                is Resource.Error -> {
                    customProgressDialog.dismiss()
                    response.message?.let {
                        showMessage(it)
                        pauseTimer()
                    }

                }

                is Resource.Loading -> {
                    customProgressDialog.show()
                }
            }

        })

        viewModel.sendSms(sPhone)

    }

    private fun showCodeInput(sPhone: String) {
        code_layout.visibility = View.VISIBLE
        login_btn_takecode.visibility = View.GONE
        phone_input_phone_edittext.isEnabled = false
    }

    private fun hideCodeInput(sPhone: String) {
        code_layout.visibility = View.GONE
        login_btn_takecode.visibility = View.VISIBLE
    }

    private fun showMessage(message: String) {
        Utils.toastIconError(requireActivity(), message)
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(leftTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                leftTime = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                isTimerRunning = false
                phone_input_code_notreceived_tv.visibility = View.VISIBLE
                phone_input_change_phone_tv.visibility = View.VISIBLE
            }

        }.start()
        isTimerRunning = true
    }

    private fun updateTimer() {
        val minute = leftTime / 1000 / 60
        val seconds = leftTime / 1000 % 60

        val formattedTime = String.format("%02d:%02d", minute, seconds)
        phone_input_timer.text = formattedTime
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
    }

    private fun resetTimer() {
        leftTime = START_TIME
        updateTimer()
    }

    private fun initToolbar() {
        toolbar_layout.setBackgroundColor(resources.getColor(R.color.background_color))
        toolbar_title.text = getString(R.string.kirish)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }


}