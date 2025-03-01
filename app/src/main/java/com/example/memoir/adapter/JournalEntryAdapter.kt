package com.example.memoir.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoir.databinding.ItemJournalEntryBinding
import com.example.memoir.model.JournalEntryModel

class JournalEntryAdapter(
    private val entries: List<JournalEntryModel>, // List of journal entries
    private val onItemClick: (JournalEntryModel) -> Unit // Click listener for each entry
) : RecyclerView.Adapter<JournalEntryAdapter.EntryViewHolder>() {

    // ViewHolder for journal entries
    inner class EntryViewHolder(private val binding: ItemJournalEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(journalEntry: JournalEntryModel) {
            // Bind data to views
            binding.textTitle.text = journalEntry.title
            binding.textContent.text = journalEntry.content
            binding.textDate.text = journalEntry.date // Display the date

            // Set click listener for the entire item
            binding.root.setOnClickListener {
                onItemClick(journalEntry)
            }
        }
    }

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemJournalEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryViewHolder(binding)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    // Return the number of journal entries
    override fun getItemCount(): Int {
        return entries.size
    }
}