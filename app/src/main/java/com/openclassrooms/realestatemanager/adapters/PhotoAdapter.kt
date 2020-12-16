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
import com.openclassrooms.realestatemanager.databinding.ItemPhotoDetailsBinding
import com.openclassrooms.realestatemanager.models.database.PropertyPhoto
import com.openclassrooms.realestatemanager.ui.fragments.DetailsFragment.Companion.isForDetailsFragment

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoDetailsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<PropertyPhoto>() {
        override fun areItemsTheSame(oldItem: PropertyPhoto, newItem: PropertyPhoto) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PropertyPhoto, newItem: PropertyPhoto) =
                oldItem.hashCode() == newItem.hashCode()
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var photosListDetails: List<PropertyPhoto>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.PhotoDetailsViewHolder {
        val binding = ItemPhotoDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoAdapter.PhotoDetailsViewHolder, position: Int) {
        val currentPhotoDetails = photosListDetails[position]
        holder.bind(currentPhotoDetails)
    }

    override fun getItemCount(): Int {
        return photosListDetails.size
    }

    private var onItemClickListener: ((PropertyPhoto) -> Unit)? = null
    private var onItemDeleteListener: ((PropertyPhoto) -> Unit)? = null

    fun setOnItemClickListener(listener: (PropertyPhoto) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemDeleteListener(listener: (PropertyPhoto) -> Unit) {
        onItemDeleteListener = listener
    }


    inner class PhotoDetailsViewHolder(private val binding: ItemPhotoDetailsBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(photosListDetails[adapterPosition])
                }
            }

            binding.ivDeletePhoto.setOnClickListener {
                onItemDeleteListener?.let {
                    it(photosListDetails[adapterPosition])
                }
            }
        }

        fun bind(propertyPhoto: PropertyPhoto) {
            binding.apply {

                Glide.with(itemView)
                        .load(propertyPhoto.filename)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_bar)
                        .into(ivPhoto)

                tvPhotoDescription.text = propertyPhoto.label

                ivDeletePhoto.visibility = if (isForDetailsFragment) View.INVISIBLE else View.VISIBLE

            }

        }
    }
}