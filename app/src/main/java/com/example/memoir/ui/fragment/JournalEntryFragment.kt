package com.example.memoir.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.memoir.databinding.FragmentJournalEntryBinding

class JournalEntryFragment : Fragment() {

    private var _binding: FragmentJournalEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJournalEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveEntryButton.setOnClickListener {
            val title = binding.entryTitle.text.toString().trim()
            val content = binding.entryContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                saveEntry(title, content)
            }
        }
    }

    private fun saveEntry(title: String, content: String) {
        // Here we'll later save the entry to Firebase
        Toast.makeText(requireContext(), "Entry Saved!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
