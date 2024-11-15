package com.example.eventmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter( // adapter for displaying event items in a RecyclerView
    private val eventList: MutableList<Event>,
    private val onItemClick: (Event) -> Unit,
    private val onDeleteClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // holds views for an event item
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        val eventDate: TextView = itemView.findViewById(R.id.event_date)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.eventName.text = event.name
        holder.eventDate.text = event.date

        holder.itemView.setOnClickListener {
            onItemClick(event)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(event)
            eventList.removeAt(holder.bindingAdapterPosition)
            notifyItemRemoved(holder.bindingAdapterPosition)
            notifyItemRangeChanged(holder.bindingAdapterPosition, eventList.size)
        }
    }

    override fun getItemCount(): Int = eventList.size // returns the total number of items
}
