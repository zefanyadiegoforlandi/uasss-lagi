package com.ppb.travellin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ppb.travellin.authenticate.LoginFragment
import com.ppb.travellin.authenticate.RegisterFragment

class TabAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
    private val page = arrayOf<Fragment>(
        RegisterFragment(),
        LoginFragment(),
    )
    override fun getItemCount(): Int = page.size

    override fun createFragment(position: Int): Fragment {
        return page[position]
    }
}