package com.dating.lib.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dating.lib.ui.fragment.chat.HomeFragment

class RootFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0-> HomeFragment()
            1-> HomeFragment()
            else-> HomeFragment()
        }
    }
}