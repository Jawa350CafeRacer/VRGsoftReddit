package com.example.vrgsoftreddit.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vrgsoftreddit.R
import com.example.vrgsoftreddit.databinding.PostBinding
import com.example.vrgsoftreddit.model.Children
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class ScreenAdapter : RecyclerView.Adapter<ScreenAdapter.StartViewHolder>() {
    inner class StartViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)

    var listener: ((String?) -> Unit)? = null
    private var listScreen = ArrayList<Children>()
    private var after: String? = null
    private val bundle = Bundle()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartViewHolder {
        return StartViewHolder(
            PostBinding.inflate(
                 LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: StartViewHolder, position: Int)  {
        val item = listScreen[position].data



        holder.binding.apply {
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss")
            val datePost = (item.created)
            fun getDataString(datePost: Int): String = simpleDateFormat.format(datePost * 1000L)
            val past: Date = simpleDateFormat.parse(getDataString(datePost)) as Date
            val now = Date()
            val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)


            textView2.text = item.title
            itemTitle.text = "Posted by ${item.author}, $hours h ago"
            textView.text = "${item.num_comments} comments"

            if (item.is_video) {
                Glide.with(holder.itemView.context).load(item.thumbnail)
                    .into(imageView)
            } else {
                Glide.with(holder.itemView.context).load(item.url_overridden_by_dest)
                    .into(imageView)
            }


                imageView.setOnClickListener() {

                    bundle.putString("MyArg", item.url_overridden_by_dest)
                    Navigation.findNavController(holder.itemView).navigate(R.id.action_screenFragment_to_fullScreenFragment, bundle)
                }


            if (position == itemCount - 1 && after != null) {
                listener?.invoke(after)
            }
        }


    }


    override fun getItemCount(): Int {
        return listScreen.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Children>, after: String?) {
        listScreen.addAll(list)
        this.after = after
        notifyDataSetChanged()
    }


}