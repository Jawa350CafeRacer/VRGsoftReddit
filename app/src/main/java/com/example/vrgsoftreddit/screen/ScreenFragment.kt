package com.example.vrgsoftreddit.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vrgsoftreddit.databinding.ScreenFragmentBinding


class ScreenFragment : Fragment() {

    private val screenViewModel by lazy { ViewModelProviders.of(this)[(ScreenFragmentViewModel::class.java)] }
    private var _binding: ScreenFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ScreenAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ScreenFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupObservers()

        screenViewModel.getDataInfo(after = null)
    }


    private fun setupAdapter() {
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter
        adapter.listener = { after -> screenViewModel.getDataInfo(after) }
    }

    private fun setupObservers() {
        screenViewModel.response.observe(viewLifecycleOwner) { response ->
            adapter.setList(response.data.children, response.data.after)
        }
        screenViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }
        screenViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.pb.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

 }