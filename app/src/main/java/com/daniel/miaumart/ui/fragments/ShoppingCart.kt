package com.daniel.miaumart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.FragmentShoppingCartBinding
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.domain.models.ShoppingCartML
import com.daniel.miaumart.ui.activities.Login
import com.daniel.miaumart.ui.adapters.CartItemClickListener
import com.daniel.miaumart.ui.adapters.RecyclerShoppingCartAdapter
import com.daniel.miaumart.ui.commons.BasicUserData
import com.daniel.miaumart.ui.commons.Snackbar
import com.daniel.miaumart.ui.viewModels.ShoppingCartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingCart : Fragment(), CartItemClickListener {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingCartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)

        if(BasicUserData.isRegistered) initUI()
        else{
            startActivity(Intent(requireContext(), Login::class.java))
            val navController = Navigation.findNavController(requireActivity(), R.id.fragment)
            navController.popBackStack()
        }

        return binding.root
    }

    private fun initUI() {
        toBuy()
        binding.progressBarSc.visibility = View.VISIBLE
        binding.recyclerShoppingCart.visibility = View.GONE
        viewModel.startListening(BasicUserData.username) { onDataRetrieved(it) }
        binding.profileImage.load(BasicUserData.profileImage)
        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Snackbar.showMessage(message, binding.shoppingCartLayout)
                viewModel.message.value = ""
            }
        }
    }

    override fun onDestroy() {
        viewModel.stopListening(BasicUserData.username) { onDataRetrieved(it) }
        super.onDestroy()
    }

    private fun onDataRetrieved(products: ArrayList<ShoppingCartML>) {
        if (products.isEmpty()) binding.emptyCartLayout.visibility =
            View.VISIBLE else binding.emptyCartLayout.visibility = View.GONE
        var totalItems = 0
        var total = 0.0
        products.forEach { items ->
            totalItems += items.numberItems.toInt()
            total += items.productPrice.toDouble() * items.numberItems.toInt()
        }
        binding.numberItems.text = "$totalItems Items"
        binding.total.text = "$" + "%.2f".format(total)
        binding.recyclerShoppingCart.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerShoppingCartAdapter(products, this@ShoppingCart)
        }
        binding.progressBarSc.visibility = View.GONE
        binding.recyclerShoppingCart.visibility = View.VISIBLE
    }

    private fun toBuy() {
        binding.openToBuy.setOnClickListener {
            binding.openToBuy.visibility = View.GONE
            binding.toBuyLayout.visibility = View.VISIBLE
        }
        binding.closeToBuy.setOnClickListener {
            binding.toBuyLayout.visibility = View.GONE
            binding.openToBuy.visibility = View.VISIBLE
        }

        binding.toBuyButton.setOnClickListener {
            alertDialog()
        }
    }

    override fun onDeleteItemClickListener(productId: String) {
        viewModel.deleteProductCart(BasicUserData.username, productId)
    }

    private fun alertDialog(){
        val build = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.alert_dialog_shopping_cart, null)
        build.setView(view)
        val alertDialog = build.create()
        alertDialog.show()
        view.findViewById<Button>(R.id.positive_button).setOnClickListener {
            viewModel.toBuy()
            alertDialog.dismiss()
        }
        view.findViewById<Button>(R.id.negative_button).setOnClickListener {
            viewModel.message.value = "Purchase canceled"
            alertDialog.dismiss()
        }
    }

}