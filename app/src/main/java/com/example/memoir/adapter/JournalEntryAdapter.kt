package com.example.memoir.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoir.databinding.ItemJournalEntryBinding
import com.example.memoir.model.JournalEntry

class JournalEntryAdapter(private val entries: List<JournalEntry>) :
    RecyclerView.Adapter<JournalEntryAdapter.EntryViewHolder>() {

    class EntryViewHolder(val binding: ItemJournalEntryBinding) :
        RecyclerView.ViewHolder(binding.root)



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
        holder.binding.textTitle.text = entry.title
        holder.binding.textContent.text = entry.content
        holder.binding.textDate.text = entry.date
    }

    override fun getItemCount() = entries.size
}
