package com.example.memoir.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.memoir.R
import com.example.memoir.databinding.FragmentDashboardBinding
import com.example.memoir.ui.fragments.JournalEntryFragment

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Set up button click listeners
        binding.newEntryButton.setOnClickListener {
            navigateToFragment(JournalEntryFragment())
        }

        binding.viewEntriesButton.setOnClickListener {
            navigateToFragment(ViewEntriesFragment())
        }

        binding.calendarButton.setOnClickListener {
            navigateToFragment(CalendarFragment())
        }

        binding.analyticsButton.setOnClickListener {
            navigateToFragment(AnalyticsFragment())
        }

        return binding.root
    }

    private fun navigateToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)  // Enables back navigation
            .commit()
    }
}
