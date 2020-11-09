package com.openclassrooms.realestatemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemPropertyBinding
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.fragments.ListFragment
import timber.log.Timber
import java.text.DecimalFormat

class PropertyAdapter : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property) =
                oldItem == newItem

        override fun areContentsTheSame(oldItem: Property, newItem: Property) =
                oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var properties: List<Property>
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

    private var onItemClickListener: ((Property) -> Unit)? = null

    fun setOnItemClickListener(listener: (Property) -> Unit) {
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

        fun bind(property: Property) {
            binding.apply {
                Glide.with(itemView)
                        .load(R.drawable.test_house_photo)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(ivPicture)

                tvType.text = property.type
                tvCity.text = property.city  // TODO
                tvBedroom.text = property.nbrBedroom.toString()
                tvBathroom.text = property.nbrBathroom.toString()
                tvRoom.text = property.nbrRoom.toString()
                if (ListFragment.isDollar) {
                    tvPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro_focused, 0, 0, 0)
                    tvPrice.text = DecimalFormat("#,###").format(property.priceInDollars)
                } else {
                    tvPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar_focused, 0, 0, 0)
                    tvPrice.text = DecimalFormat("#,###").format(property.priceInDollars)
                }
            }
        }
    }
}