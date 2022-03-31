package com.sutrix.uatest.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sutrix.uatest.database.BurgerDatabase
import com.sutrix.uatest.databinding.CartFragmentBinding
import com.sutrix.uatest.databinding.CatalogItemBinding
import com.sutrix.uatest.model.models.Burger


class CartFragment : Fragment() {

    private lateinit var binding: CartFragmentBinding

    private val burgerDatabase = BurgerDatabase

    private var cartList: List<Burger>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = burgerDatabase.invoke(binding.root.context).getBurgerDao()
        cartList = database.getAllBurger()
        val priceList = mutableListOf<Double>()
        cartList?.forEach {
            priceList.add(it.price)
        }
        if (priceList.isNotEmpty()) {
            val totalPay = priceList.reduce { acc, d -> acc + d }
            binding.payTxt.text = "Pay " + String.format("%.2f", totalPay).toDouble()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = object : RecyclerView.Adapter<CartViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
                return CartViewHolder(
                    CatalogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
                val res = cartList?.groupingBy { it }?.eachCount()
                val res1 = cartList?.groupingBy {
                    Burger(
                        ref = it.ref,
                        price = it.price,
                        description = it.description,
                        thumbnail = it.thumbnail,
                        title = it.title
                    )
                }?.eachCount()
                res1?.entries?.forEachIndexed { index, entry ->
                    if (index == position) {
                        holder.bind(entry.key, entry.value)
                    }
                }
            }

            override fun getItemCount(): Int {
                return cartList?.groupBy { it.ref }?.count() ?: 0
            }
        }
    }

    inner class CartViewHolder(private val binding: CatalogItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(burger: Burger, count: Int) {
            Glide.with(binding.root)
                .load(Uri.parse(burger.thumbnail))
                .into(binding.tvImage)
            binding.tvLabel.text = burger.title + " x " + count
            binding.tvPrice.text = burger.price.toString()
            binding.btnAdd.isVisible = false
        }
    }
}