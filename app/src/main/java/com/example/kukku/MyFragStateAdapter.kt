package com.example.kukku;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
//        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->Fragment_GyoYang()
            1->Fragment_Major()
            2->Fragment_Timetable()
            3->Fragment_KuMap()
//            4->Fragment_Programmer()
            else->Fragment_GyoYang()
        }
    }
}