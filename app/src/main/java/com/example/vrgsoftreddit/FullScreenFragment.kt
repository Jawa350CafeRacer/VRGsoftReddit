package com.example.vrgsoftreddit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.vrgsoftreddit.databinding.FragmentFullScreenBinding



class FullScreenFragment : Fragment() {

    private var _binding: FragmentFullScreenBinding? = null
    private val binding get() = _binding!!
   // private var imageView =

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_full_screen, container, false)
        binding.button2.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fullScreenFragment_to_screenFragment)
        }
        //Glide.with(this).load(imageView)
       //     .into(binding.FsImageView)

        return view
    }



}