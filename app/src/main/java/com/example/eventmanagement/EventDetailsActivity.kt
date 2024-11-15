package com.example.eventmanagement

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventName = intent.getStringExtra("eventName") ?: "No Name"
        val eventDate = intent.getStringExtra("eventDate") ?: "No Date"
        val eventBudget = intent.getStringExtra("eventBudget") ?: "No Budget"
        val eventPeople = intent.getStringExtra("eventPeople") ?: "No People"
        val eventDescription = intent.getStringExtra("eventDescription") ?: "No Description"
        findViewById<TextView>(R.id.detail_event_name).text = eventName
        findViewById<TextView>(R.id.detail_event_date).text = eventDate
        findViewById<TextView>(R.id.detail_event_budget).text = eventBudget
        findViewById<TextView>(R.id.detail_event_people).text = eventPeople
        findViewById<TextView>(R.id.detail_event_description).text = eventDescription
    }
}
