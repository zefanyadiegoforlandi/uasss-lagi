package com.ppb.travellin.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ppb.travellin.R
import com.ppb.travellin.adapter.TabAdapter
import com.ppb.travellin.databinding.FragmentBottomWelcomeScreenLayoutBinding


class WelcomeSheetFragment : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentBottomWelcomeScreenLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (this as BottomSheetDialog).window?.let { window ->
                    window.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.color.semi_transparent))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.viewPagerWelcomeScreen.adapter = TabAdapter(childFragmentManager, lifecycle)
        binding.viewPagerWelcomeScreen.currentItem = 1
        TabLayoutMediator(binding.tabLayout, binding.viewPagerWelcomeScreen) {
            tab, position ->
            when(position) {
                0 -> tab.text = "Register"
                1 -> tab.text = "Login"
            }
        }.attach()

        viewPagerCompanion = binding.viewPagerWelcomeScreen

    }

    companion object {
        var viewPagerCompanion : ViewPager2? = null
    }

}