package com.example.smartfarmtracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AnimalFragmentsAdapter(
    fragmentActivity: FragmentActivity,
    private val animalId: String,
    private val animalType: String
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: List<Fragment> = listOf(
        SalesFragment.newInstance(animalId, animalType)
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
