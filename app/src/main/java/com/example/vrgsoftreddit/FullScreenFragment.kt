package com.example.vrgsoftreddit

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.vrgsoftreddit.databinding.FragmentFullScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class FullScreenFragment : Fragment() {

    private var _binding: FragmentFullScreenBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = this.arguments
        val img = args?.get("MyArg")
        Glide.with(this).load(img.toString()).into(binding.fsImageView)

        binding.fsImageView.setOnClickListener(){
            Navigation.findNavController(view)
                .navigate(R.id.action_fullScreenFragment_to_screenFragment)
        }

        binding.button3.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {


                // this method saves the image to gallery
                fun saveMediaToStorage(bitmap: Bitmap) {
                    // Generating a file name
                    val filename = "${System.currentTimeMillis()}.jpg"

                    // Output stream
                    var fos: OutputStream? = null
                    val resolver = requireActivity().contentResolver

                    // For devices running android >= Q
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // getting the contentResolver
                        resolver?.also { resolver ->

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
                saveMediaToStorage(binding.fsImageView.drawToBitmap())
            }
            Toast.makeText(
                this.context,
                "Image saved to Gallery",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.button2.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_fullScreenFragment_to_screenFragment)
        }

    }
}