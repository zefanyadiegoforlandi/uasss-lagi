package com.ppb.travellin.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ppb.travellin.databinding.FragmentBottomSheetDialogBinding
import com.ppb.travellin.dialog.adapter.GlobalTypeAdapter

class GlobalSheetFragment<T>(
    private val title : String,
    val globalAdapter: GlobalTypeAdapter<T>,
    private val searchFeatures: Boolean = false,
    var searchLogic : (String) -> Unit = {}
) : BottomSheetDialogFragment() {

    private val binding by lazy { FragmentBottomSheetDialogBinding.inflate(layoutInflater)}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTitleBottomSheet.text = title

        setupRecycler()

        if (searchFeatures) {
            binding.textEditSearchSheet.visibility = View.VISIBLE

            binding.textEditSearchSheet.doAfterTextChanged {
                val searchQuery = it.toString().trim()

                searchLogic(searchQuery)
            }

            binding.textInputLayoutSearchSheet.setEndIconOnClickListener {
                binding.textEditSearchSheet.text?.clear()
                searchLogic("")

                binding.textEditSearchSheet.clearFocus()
            }

        } else {
            binding.textEditSearchSheet.visibility = View.GONE
        }

    }

    private fun setupRecycler() {
        with(binding) {
            recyclerViewSheet.apply {
                adapter = globalAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }

    fun closeDialog() {
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        isDialogOpen = true
    }

    override fun onStop() {
        super.onStop()
        isDialogOpen = false
    }

    companion object {
        var isDialogOpen = false
    }

}