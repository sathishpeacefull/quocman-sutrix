package com.sutrix.uatest.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sutrix.uatest.R
import com.sutrix.uatest.database.BurgerDao
import com.sutrix.uatest.database.BurgerDatabase
import com.sutrix.uatest.databinding.CatalogItemBinding
import com.sutrix.uatest.databinding.MainActivityBinding
import com.sutrix.uatest.model.models.Burger
import com.sutrix.uatest.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private val viewModel: MainViewModel by viewModel()

    private var burgers: List<Burger>? = null

    private val burgerDatabase = BurgerDatabase

    private var cartList: List<Burger>? = null

    inner class BurgerViewHolder(private val binding: CatalogItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(burger: Burger) {
            Glide.with(binding.root)
                .load(Uri.parse(burger.thumbnail))
                .into(binding.tvImage)
            binding.tvLabel.text = burger.title
            binding.tvPrice.text = burger.price.toString()
            binding.btnAdd.setOnClickListener {
                addToCart(burger)
            }
        }
    }

    private fun addToCart(burger: Burger) {
        val database = burgerDatabase.invoke(binding.root.context).getBurgerDao()
        database.insertBurger(burger)
        showCartCount()
    }

    private fun showCartCount() {
        val database = burgerDatabase.invoke(binding.root.context).getBurgerDao()
        cartList = database.getAllBurger()
        binding.btnBasket.text = "My basket " + cartList?.groupBy { it.ref }?.count() + " items"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val cartFragment = CartFragment()

        binding.btnBasket.setOnClickListener {
            if (cartList?.groupBy { it.ref }?.count()!! > 0) {
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.root_container, cartFragment).addToBackStack("tag")
                    .commit()
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = object : RecyclerView.Adapter<BurgerViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurgerViewHolder {
                return BurgerViewHolder(
                    CatalogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            override fun onBindViewHolder(holder: BurgerViewHolder, position: Int) {
                burgers?.get(position)?.let {
                    holder.bind(it)
                }

            }

            override fun getItemCount(): Int {
                return burgers?.size ?: 0
            }
        }

        lifecycleScope.launch {
            viewModel.burgers.collect {
                burgers = it
                binding.recyclerView.adapter?.notifyDataSetChanged()
                if (!it.isNullOrEmpty()) {
                    binding.progressBar.isVisible = false
                }
                showCartCount()
            }
        }
    }
}