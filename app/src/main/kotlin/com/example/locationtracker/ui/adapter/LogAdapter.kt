package com.example.locationtracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.data.LogEntry
import com.example.locationtracker.databinding.ItemLogBinding

class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {
    private val logs = mutableListOf<LogEntry>()

    fun updateLogs(newLogs: List<LogEntry>) {
        logs.clear()
        logs.addAll(newLogs)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount() = logs.size

    class LogViewHolder(private val binding: ItemLogBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(logEntry: LogEntry) {
            binding.apply {
                timestamp.text = logEntry.timestamp.toString()
                message.text = logEntry.message
                if (logEntry.isError) {
                    message.setTextColor(android.graphics.Color.RED)
                }
            }
        }
    }
}
