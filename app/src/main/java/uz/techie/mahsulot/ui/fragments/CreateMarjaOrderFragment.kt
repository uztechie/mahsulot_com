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
import kotlinx.android.synthetic.main.fragment_create_marja_order.*
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
class CreateMarjaOrderFragment : Fragment(R.layout.fragment_create_marja_order),
    PositiveNegativeDialog.PositiveNegativeListener {
    private val TAG = "CreateOrderFragment"

    private val viewModel: MahsulotViewModel by viewModels()
    private var token = ""
    private var user = User(id = -1)
    private var quantity = 1
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
            product = CreateMarjaOrderFragmentArgs.fromBundle(it).product
        }

        marja_order_product_title.text = "${getString(R.string.mahsulot_nomi)}: ${product?.name}"
        marja_order_product_total_price.text =
            "${getString(R.string.marja)}: ${product?.marja_price}"
        marja_order_product_quantity.text = "${getString(R.string.mahsulot_soni)}: ${quantity} ta"


        viewModel.getUser().observe(viewLifecycleOwner, Observer { mUser->
            user = mUser
            order_marja_name.text = user.first_name+" "+user.last_name
            order_marja_phone.text = "+"+user.phone
            mUser?.token?.let { mToken->
                token = "Token "+mToken
            }
        })


        order_marja_btn_order.setOnClickListener {
            createOrder()
        }

        order_marja_regions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        order_marja_regions.setOnItemClickListener { parent, view, position, id ->
            regionId = (order_marja_regions.adapter.getItem(position) as Region).id
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
        val regionName = order_marja_regions.text.toString()

        if (regionName.isEmpty()) {
            order_marja_regions_layout.error = getString(R.string.viloyatingizni_tanlang)
            return
        } else {
            order_marja_regions_layout.error = null
        }

        val map = HashMap<String, Any>()
        map["seller"] = user.id.toString()
        map["product"] = product.id.toString()
        map["pay_marja"] = product.marja_price.toString()
        map["marja_product_price"] = product.marja_price.toString()
        map["customer_region"] = regionId
        map["status"] = 1


        viewModel.makeMarjaOder(Constants.MY_TOKEN, token, map)

    }


    private fun initRegionDropDown() {
        regionDropDownAdapter =
            RegionDropDownAdapter(requireContext(), R.layout.adapter_dropdown, Utils.regionList())
        order_marja_regions.setAdapter(regionDropDownAdapter)
        order_marja_regions.setOnItemClickListener { parent, view, position, id ->

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