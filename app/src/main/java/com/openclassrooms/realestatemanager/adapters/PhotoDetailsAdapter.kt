package com.openclassrooms.realestatemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemPhotoDetailsBinding

class PhotoDetailsAdapter : RecyclerView.Adapter<PhotoDetailsAdapter.PhotoDetailsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int) =
                oldItem == newItem

        override fun areContentsTheSame(oldItem: Int, newItem: Int) =
                oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var photosDetails: List<Int>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDetailsAdapter.PhotoDetailsViewHolder {
        val binding = ItemPhotoDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoDetailsAdapter.PhotoDetailsViewHolder, position: Int) {
        val currentPhotoDetails = photosDetails[position]
        holder.bind(currentPhotoDetails)
    }

    override fun getItemCount(): Int {
        return photosDetails.size
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }


    inner class PhotoDetailsViewHolder(private val binding: ItemPhotoDetailsBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(photosDetails[adapterPosition])
                }
            }
        }

        fun bind(photoDetails: Int) {
            binding.apply {
//                Glide.with(itemView)
//                        .load(R.drawable.test_house_photo)
//                        .centerCrop()
//                        .transition(DrawableTransitionOptions.withCrossFade())
//                        .error(R.drawable.ic_error)
//                        .into(ivPhoto)
                ivPhoto.setImageResource(R.drawable.test_house_photo)
                tvPhotoDescription.text = "Outside"
            }
        }
    }
}