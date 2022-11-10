package com.example.vrgsoftreddit.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vrgsoftreddit.R
import com.example.vrgsoftreddit.databinding.ScreenFragmentBinding


class ScreenFragment : Fragment() {


    private lateinit var screenViewModel: ScreenFragmentViewModel
    private lateinit var binding: ScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=ScreenFragmentBinding.inflate(layoutInflater)
        screenViewModel = ViewModelProvider(this)[ScreenFragmentViewModel::class.java]
        return inflater.inflate(R.layout.screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {

        super.onViewCreated(view, savedInstanceState)
        val adapter = ScreenAdapter()


        binding.rcView.adapter = adapter


        adapter.listener = { after ->
            Log.d("MyLog", "onViewCreated: after = $after")
            screenViewModel.getDataInfo(after)
        }

        screenViewModel.getDataInfo(after = null)
        screenViewModel.allApi.observe(viewLifecycleOwner) { response ->
            adapter.setList(response.data.children, response.data.after)
        }


    }

}