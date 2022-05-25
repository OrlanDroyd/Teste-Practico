package com.gmail.orlandroyd.testepratico.presentation.my_products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.orlandroyd.testepratico.databinding.ItemProductBinding
import com.gmail.orlandroyd.testepratico.domain.model.Product

class MyProductAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Product, MyProductAdapter.ProductViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    /**
     * ViewHolder
     */
    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(item: Product) {
            binding.apply {

                tvTitle.text = item.title

                Glide.with(binding.root.context)
                    .load(item.thumbnail)
                    .into(imgThumbnail)

            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    /**
     * DiffCallback
     */
    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }

}