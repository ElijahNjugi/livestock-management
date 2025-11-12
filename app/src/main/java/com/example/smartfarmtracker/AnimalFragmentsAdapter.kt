package com.example.smartfarmtracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AnimalFragmentsAdapter(
    fragmentActivity: FragmentActivity,
    private val animalId: String
) : FragmentStateAdapter(fragmentActivity) {

    // List of fragments for the ViewPager2
    private val fragments: List<Fragment> = listOf(
        FeedingFragment.newInstance(animalId),
        MedicalFragment.newInstance(animalId),
        NotesFragment.newInstance(animalId),
        SalesFragment.newInstance(animalId)
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
