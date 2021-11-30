package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_create_order.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.RegionDropDownAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.dialog.PositiveNegativeDialog
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.model.Region
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.util.Constants
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils

@AndroidEntryPoint
class CreateOrderFragment : Fragment(R.layout.fragment_create_order),
    PositiveNegativeDialog.PositiveNegativeListener {
    private val TAG = "CreateOrderFragment"

    private val viewModel: MahsulotViewModel by viewModels()
    private var token = ""
    private var user = User(id = -1)
    private var quantity = 0
    private var totalPrice = 0
    private var product = Product()
    private lateinit var regionDropDownAdapter: RegionDropDownAdapter
    private var regionId = 0
    private lateinit var positiveNegativeDialog: PositiveNegativeDialog
    private lateinit var customProgressDialog: CustomProgressDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customProgressDialog = CustomProgressDialog(requireContext())
        positiveNegativeDialog = PositiveNegativeDialog(requireContext(), this)
        initToolbar()
        initRegionDropDown()


        arguments?.let {
            product = CreateOrderFragmentArgs.fromBundle(it).product
            totalPrice = CreateOrderFragmentArgs.fromBundle(it).totalPrice
            quantity = CreateOrderFragmentArgs.fromBundle(it).amount
        }

        order_product_title.text = "${getString(R.string.mahsulot_nomi)}: ${product.name}"
        order_product_total_price.text =
            "${getString(R.string.umumiy_narx)}: ${Utils.toMoney(totalPrice)} ${getString(R.string.som)}"
        order_product_quantity.text = "${getString(R.string.mahsulot_soni)}: ${quantity} ta"


        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            it?.token?.let { mToken->
                token = "Token "+mToken
            }
        })


        order_btn_order.setOnClickListener {
            createOrder()
        }

        order_regions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        order_regions.setOnItemClickListener { parent, view, position, id ->
            regionId = (order_regions.adapter.getItem(position) as Region).id
            Log.d(TAG, "onViewCreated: itemclick " + regionId)
        }


        viewModel.order.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: response " + response)
            when (response) {
                is Resource.Loading -> {
                    customProgressDialog.show()
                }
                is Resource.Error -> {
                    customProgressDialog.dismiss()
                    positiveNegativeDialog.show()
                    positiveNegativeDialog.setData(
                        getString(R.string.xatolik),
                        response.message,
                        false
                    )
                }
                is Resource.Success -> {
                    customProgressDialog.dismiss()
                    response.data?.let { orderResponse ->
                        if (orderResponse.status == 200) {
                            positiveNegativeDialog.show()
                            positiveNegativeDialog.setData(
                                getString(R.string.muvaffaqiyatli),
                                getString(R.string.buyurtmangiz_qabul_qilindi),
                                true
                            )
                        } else {
                            positiveNegativeDialog.show()
                            positiveNegativeDialog.setData(
                                getString(R.string.xatolik),
                                orderResponse.message,
                                false
                            )
                        }
                    }
                }
            }
        })


    }

    private fun createOrder() {
        val name = order_name.text.toString()
        var phone = order_phone_edittext.unmaskedText.toString()
        phone = "+998$phone"
        val regionName = order_regions.text.toString()

        if (name.isEmpty()) {
            order_name_layout.error = getString(R.string.ismingizni_kiriting)
            return
        } else {
            order_name_layout.error = null
        }

        if (phone.length <= 12) {
            order_phone_container.error = getString(R.string.mavjud_telefon_raqami)
            return
        } else {
            order_phone_container.error = null
        }

        if (regionName.isEmpty()) {
            order_regions_layout.error = getString(R.string.viloyatingizni_tanlang)
            return
        } else {
            order_regions_layout.error = null
        }

        val map = HashMap<String, Any>()
        map["product_id"] = product.id.toString()
        map["product_bonus"] = product.bonus.toString()
        map["product_price"] = product.price.toString()
        map["marja_amount"] = product.marja_amount.toString()
        map["amount"] = quantity
        map["customer_name"] = name
        map["customer_region"] = regionId
        map["customer_phone"] = phone
        map["status"] = 1

        viewModel.makeOder(Constants.MY_TOKEN, token, map)

//        viewModel.makeOder(
//            Constants.MY_TOKEN,
//            token,
//            product.id!!,
//            product.bonus!!,
//            product.price!!,
//            product.marja_amount!!,
//            quantity,
//            name,
//            regionId,
//            phone,
//            1
//        )

    }


    private fun initRegionDropDown() {
        regionDropDownAdapter =
            RegionDropDownAdapter(requireContext(), R.layout.adapter_dropdown, Utils.regionList())
        order_regions.setAdapter(regionDropDownAdapter)
        order_regions.setOnItemClickListener { parent, view, position, id ->

        }


    }

    private fun initToolbar() {
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.savdo_tarixi)
        toolbar_title.visibility = View.INVISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }

    override fun onBackBtnClick() {
        findNavController().popBackStack()
    }


}