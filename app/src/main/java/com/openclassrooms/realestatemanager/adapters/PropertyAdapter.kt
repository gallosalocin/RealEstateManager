package com.openclassrooms.realestatemanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemPropertyBinding
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.ui.fragments.ListFragment
import com.openclassrooms.realestatemanager.utils.Utils

class PropertyAdapter : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<PropertyWithAllData>() {
        override fun areItemsTheSame(oldItem: PropertyWithAllData, newItem: PropertyWithAllData) =
                oldItem.property.id == newItem.property.id

        override fun areContentsTheSame(oldItem: PropertyWithAllData, newItem: PropertyWithAllData) =
                oldItem.property.hashCode() == newItem.property.hashCode()
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var properties: List<PropertyWithAllData>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val currentProperty = properties[position]
        holder.bind(currentProperty)
    }

    override fun getItemCount(): Int {
        return properties.size
    }

    private var onItemClickListener: ((PropertyWithAllData) -> Unit)? = null

    fun setOnItemClickListener(listener: (PropertyWithAllData) -> Unit) {
        onItemClickListener = listener
    }


    inner class PropertyViewHolder(private val binding: ItemPropertyBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(properties[adapterPosition])
                }
            }
        }

        fun bind(propertyWithAllData: PropertyWithAllData) {
            binding.apply {

                Glide.with(itemView)
                        .load(propertyWithAllData.property.coverPhoto)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_bar)
                        .into(ivImage)

                tvType.text = propertyWithAllData.property.type
                tvCity.text = propertyWithAllData.property.city

                tvRoom.text = if (propertyWithAllData.property.nbrRoom >= 10) {
                    "${propertyWithAllData.property.nbrRoom}+"
                } else {
                    propertyWithAllData.property.nbrRoom.toString()
                }
                tvBedroom.text = if (propertyWithAllData.property.nbrBedroom >= 10) {
                    "${propertyWithAllData.property.nbrBedroom}+"
                } else {
                    propertyWithAllData.property.nbrBedroom.toString()
                }
                tvBathroom.text = if (propertyWithAllData.property.nbrBathroom >= 10) {
                    "${propertyWithAllData.property.nbrBathroom}+"
                } else {
                    propertyWithAllData.property.nbrBathroom.toString()
                }

                if (propertyWithAllData.property.isSold) tvSoldDiagonal.visibility = View.VISIBLE else tvSoldDiagonal.visibility = View.INVISIBLE

                when (ListFragment.isDollar) {
                    null -> tvPrice.text = Utils.formatInDollar(propertyWithAllData.property.priceInDollars, 0)
                    true -> {
                        val price = Utils.convertDollarToEuro(propertyWithAllData.property.priceInDollars)
                        tvPrice.text = Utils.formatInEuro(price, 0)
                    }
                    false -> tvPrice.text = Utils.formatInDollar(propertyWithAllData.property.priceInDollars, 0)
                }
            }
        }
    }
}