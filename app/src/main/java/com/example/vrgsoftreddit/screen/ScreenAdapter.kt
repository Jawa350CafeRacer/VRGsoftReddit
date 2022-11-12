package com.example.vrgsoftreddit.screen

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.vrgsoftreddit.MainActivity
import com.example.vrgsoftreddit.R
import com.example.vrgsoftreddit.databinding.ActivityMainBinding
import com.example.vrgsoftreddit.databinding.PostBinding
import com.example.vrgsoftreddit.model.Children
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class ScreenAdapter : RecyclerView.Adapter<ScreenAdapter.StartViewHolder>() {
    inner class StartViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)

    var listener: ((String?) -> Unit)? = null
    private var listScreen = ArrayList<Children>()
    private var after: String? = null



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
                    Navigation.findNavController(holder.itemView).navigate(R.id.action_screenFragment_to_fullScreenFragment)

                }




            button.setOnClickListener() {
                CoroutineScope(Dispatchers.IO).launch {


                    // this method saves the image to gallery
                    fun saveMediaToStorage(bitmap: Bitmap) {
                        // Generating a file name
                        val filename = "${System.currentTimeMillis()}.jpg"

                        // Output stream
                        var fos: OutputStream? = null

                        // For devices running android >= Q
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            // getting the contentResolver
                            holder.itemView.context.contentResolver?.also { resolver ->

                                // Content resolver will process the content-values
                                val contentValues = ContentValues().apply {

                                    // putting file information in content values
                                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                                    put(
                                        MediaStore.MediaColumns.RELATIVE_PATH,
                                        Environment.DIRECTORY_PICTURES
                                    )
                                }

                                // Inserting the contentValues to
                                // contentResolver and getting the Uri
                                val imageUri: Uri? = resolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    contentValues
                                )

                                // Opening an output stream with the Uri that we got
                                fos = imageUri?.let { resolver.openOutputStream(it) }
                            }
                        } else {
                            // These for devices running on android < Q
                            val imagesDir =
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            val image = File(imagesDir, filename)
                            fos = FileOutputStream(image)
                        }

                        fos?.use {
                            // Finally writing the bitmap to the output stream that we opened
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

                        }

                    }
                    saveMediaToStorage(imageView.drawToBitmap())
                }
                Toast.makeText(
                    holder.itemView.context,
                    "Image saved to Gallery",
                    Toast.LENGTH_SHORT
                ).show()
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