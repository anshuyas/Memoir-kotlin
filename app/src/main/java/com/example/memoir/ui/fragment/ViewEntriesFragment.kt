package com.example.memoir.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoir.databinding.FragmentViewEntriesBinding
import com.example.memoir.model.JournalEntryModel
import com.example.memoir.ui.adapter.JournalEntryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewEntriesFragment : Fragment() {

    private var _binding: FragmentViewEntriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: JournalEntryAdapter
    private val journalEntries = mutableListOf<JournalEntryModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewEntriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
            .child("JournalEntries").child(auth.currentUser!!.uid)

        setupRecyclerView()
        fetchJournalEntries()
    }

    private fun setupRecyclerView() {
        adapter = JournalEntryAdapter(journalEntries)
        binding.recyclerViewEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEntries.adapter = adapter
    }

    private fun fetchJournalEntries() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                journalEntries.clear()
                for (entrySnapshot in snapshot.children) {
                    val entry = entrySnapshot.getValue(JournalEntryModel::class.java)
                    entry?.let { journalEntries.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
