package com.example.memoir.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memoir.R
import com.example.memoir.databinding.FragmentDashboardBinding
import com.example.memoir.model.JournalEntryModel
import com.example.memoir.model.UserModel
import com.example.memoir.ui.adapter.JournalEntryAdapter
import com.example.memoir.ui.fragments.JournalEntryFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var journalAdapter: JournalEntryAdapter
    private val journalList = mutableListOf<JournalEntryModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView for recent journals
        setupRecyclerView()

        // Set up button click listeners
        binding.newEntryButton.setOnClickListener {
            navigateToFragment(JournalEntryFragment())
        }

        binding.analyticsButton.setOnClickListener {
            navigateToFragment(AnalyticsFragment())
        }

        binding.calendarButton.setOnClickListener {
            navigateToFragment(CalendarFragment())
        }

        // Fetch user data and recent journals
        fetchUserData()
        fetchRecentJournals()
    }

    private fun setupRecyclerView() {
        journalAdapter = JournalEntryAdapter(journalList) {entry ->
            navigateToFragment(JournalEntryFragment.newInstance(entry))
        }
        binding.recyclerViewJournals.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewJournals.adapter = journalAdapter
    }

    private fun fetchUserData() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userModel = document.toObject(UserModel::class.java)
                    if (userModel != null) {
                        updateUI(userModel)
                        Log.d(TAG, "Fetched user profile: $userModel")
                    } else {
                        Log.e(TAG, "UserModel is null")
                        Toast.makeText(requireContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(TAG, "User document does not exist")
                    Toast.makeText(requireContext(), "User not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to fetch user profile: ${e.message}", e)
                Toast.makeText(requireContext(), "Failed to fetch profile. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchRecentJournals() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("journals")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5) // Fetch the 5 most recent journals
            .get()
            .addOnSuccessListener { documents ->
                journalList.clear()
                for (document in documents) {
                    val journalEntry = document.toObject(JournalEntryModel::class.java)
                    journalList.add(journalEntry)
                }
                journalAdapter.notifyDataSetChanged()
                Log.d(TAG, "Fetched ${journalList.size} recent journals")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to fetch journals: ${e.message}", e)
                Toast.makeText(requireContext(), "Failed to fetch journals.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(userModel: UserModel) {
        // Update TextViews with user data
        binding.textViewUserName.text = userModel.userName
        binding.textViewUserEmail.text = userModel.email

        // Load profile image using Glide (if applicable)
        if (!userModel.profileImageUrl.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(userModel.profileImageUrl)
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.error)
                .into(binding.imageViewProfile)
        } else {
            binding.imageViewProfile.setImageResource(R.drawable.baseline_account_circle_24)
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)  // Enables back navigation
            .commit()
    }

    companion object {
        private const val TAG = "DashboardFragment"
    }
}