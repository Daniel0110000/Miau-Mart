package com.daniel.miaumart.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.miaumart.R
import com.daniel.miaumart.ui.commons.AlertDialog
import com.daniel.miaumart.databinding.FragmentShoppingCartBinding
import com.daniel.miaumart.domain.extensions.loadWithGlide
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

    private var thereAreProducts = false
    private var documentExists = false

    private var isToBuyLayoutVisible = false
    private var desiredToBuyLayoutVisibility = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)

        if (BasicUserData.isRegistered) initUI()
        else {
            startActivity(Intent(requireContext(), Login::class.java))
            Animatoo.animateSlideLeft(requireActivity())
            val navController = Navigation.findNavController(requireActivity(), R.id.fragment)
            navController.popBackStack()
        }

        return binding.root
    }

    private fun initUI() {
        toBuy()
        binding.recyclerShoppingCart.visibility = View.GONE
        binding.profileImage.loadWithGlide(requireContext(), BasicUserData.profileImage)
        viewModel.startListening(BasicUserData.username) {
            documentExists = true
            viewModel.productDates.value = it
            onDataRetrieved(it)
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Snackbar.showMessage(message, binding.shoppingCartLayout)
                viewModel.message.value = ""
            }
        }

        if (!documentExists) binding.emptyCartLayout.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        viewModel.stopListening(BasicUserData.username) { onDataRetrieved(it) }
        super.onDestroy()
    }

    private fun onDataRetrieved(products: ArrayList<ShoppingCartML>) {
        if (products.isEmpty()) binding.emptyCartLayout.visibility = View.VISIBLE
        else { thereAreProducts = true;binding.emptyCartLayout.visibility = View.GONE }
        calculateTotal(products)
        setUpRecyclerView(products)
    }

    private fun calculateTotal(products: ArrayList<ShoppingCartML>){
        var totalItems = 0
        var total = 0.0
        products.forEach { items ->
            totalItems += items.numberItems.toInt()
            total += items.productPrice.toDouble() * items.numberItems.toInt()
        }
        binding.numberItems.text = "$totalItems Items"
        binding.total.text = "$" + "%.2f".format(total)
    }

    private fun setUpRecyclerView(products: ArrayList<ShoppingCartML>) {
        binding.recyclerShoppingCart.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerShoppingCartAdapter(products, this@ShoppingCart)
        }
        binding.recyclerShoppingCart.visibility = View.VISIBLE
    }

    private fun toBuy() {
        binding.openToBuy.setOnClickListener {
            desiredToBuyLayoutVisibility = true
            setAnimation(true)
            updateToBuyLayoutVisibility()
        }
        binding.closeToBuy.setOnClickListener {
            desiredToBuyLayoutVisibility = false
            setAnimation(false)
            updateToBuyLayoutVisibility()
        }

        binding.toBuyButton.setOnClickListener {
            if (thereAreProducts) {
                AlertDialog.buildAlertDialog(
                    "Shopping Cart",
                    "Do you want to buy the products?",
                    requireContext()
                ) { result ->
                    if (result) viewModel.toBuy()
                    else viewModel.message.value = "Purchase canceled"
                }
            } else viewModel.message.value = "You have no products to buy"
        }
    }

    private fun setAnimation(isOpen: Boolean) {
        binding.toBuyLayout.animation = if (isOpen) {
            AnimationUtils.loadAnimation(context, R.anim.open_layout_animation)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.close_layout_animation)
        }
    }

    private fun updateToBuyLayoutVisibility() {
        if (isToBuyLayoutVisible == desiredToBuyLayoutVisibility) return
        isToBuyLayoutVisible = desiredToBuyLayoutVisibility
        binding.toBuyLayout.visibility = if (desiredToBuyLayoutVisibility) View.VISIBLE else View.GONE
        binding.openToBuy.visibility = if (desiredToBuyLayoutVisibility) View.GONE else View.VISIBLE
    }

    override fun onDeleteItemClickListener(productId: String) {
        viewModel.deleteProductCart(BasicUserData.username, productId)
    }

}