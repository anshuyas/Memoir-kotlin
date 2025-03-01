package com.example.memoir.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoir.databinding.ItemJournalEntryBinding
import com.example.memoir.model.JournalEntryModel

class JournalEntryAdapter(
    private val entries: List<JournalEntryModel>,
    private val onItemClick: (JournalEntryModel) -> Unit // Optional: Add click listener
) : RecyclerView.Adapter<JournalEntryAdapter.EntryViewHolder>() {

    // ViewHolder class
    inner class EntryViewHolder(val binding: ItemJournalEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind data to views
        fun bind(entry: JournalEntryModel) {
            binding.textTitle.text = entry.title
            binding.textContent.text = entry.content
            binding.textDate.text = entry.date

            // Optional: Handle item clicks
            binding.root.setOnClickListener {
                onItemClick(entry)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemJournalEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.bind(entry) // Delegate binding to ViewHolder
    }

    override fun getItemCount() = entries.size
}