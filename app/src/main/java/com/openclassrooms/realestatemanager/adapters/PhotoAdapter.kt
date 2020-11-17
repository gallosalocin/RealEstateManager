package com.openclassrooms.realestatemanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.ItemPhotoDetailsBinding

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoDetailsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var photosListDetails: List<String>
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

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }


    inner class PhotoDetailsViewHolder(private val binding: ItemPhotoDetailsBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(photosListDetails[adapterPosition])
                }
            }
        }

        fun bind(photoDetails: String) {
            binding.apply {

//                if(photoDetails.toInt() == 0) {
//                    ivPhoto.setImageResource(R.drawable.ic_error)
//                } else {
//                    Glide.with(itemView)
//                            .load(photoDetails.toInt())
//                            .centerCrop()
//                            .transition(DrawableTransitionOptions.withCrossFade())
//                            .error(R.drawable.ic_bar)
//                            .into(ivPhoto)
//                }
                tvPhotoDescription.text = "Outside"
            }
        }
    }
}