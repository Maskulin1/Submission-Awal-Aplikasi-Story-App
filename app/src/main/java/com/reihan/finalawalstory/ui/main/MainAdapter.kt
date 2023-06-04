package com.reihan.finalawalstory.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reihan.finalawalstory.R
import com.reihan.finalawalstory.remote.data.ListStory
import java.text.SimpleDateFormat
import java.util.Locale

class MainAdapter(private val storyData: List<ListStory>) :
    RecyclerView.Adapter<MainAdapter.ViewModel>() {
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                listener(position)
            }
        }
    }

    inner class ViewModel(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.img_story)
        val userName: TextView = view.findViewById(R.id.tv_title_story)
        val detail: TextView = view.findViewById(R.id.tv_story_description)
        val createdAt: TextView = view.findViewById(R.id.tv_story_date)
        fun bind(listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewModel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewModel(view)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewModel, position: Int) {
        val storyData = storyData[position]
        Glide.with(holder.itemView.context)
            .load(storyData.photoUrl)
            .into(holder.imageView)
        holder.userName.text = storyData.name
        holder.detail.text = storyData.description

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(storyData.createdAt)!!
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        holder.createdAt.text = outputFormat.format(date)

        onItemClickListener?.let { listener ->
            holder.bind(listener)
        }
    }

    override fun getItemCount(): Int = storyData.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}