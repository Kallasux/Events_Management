package com.example.eventmanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditEventActivity : AppCompatActivity() { // activity for adding or editing an event

    private lateinit var eventNameEditText: EditText
    private lateinit var eventDateEditText: EditText
    private lateinit var eventBudgetEditText: EditText
    private lateinit var eventPeopleEditText: EditText
    private lateinit var eventDescriptionEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var eventHelper: EventHelper

    private var editingEvent: Event? = null // holds the event being edited
    private var isEditing: Boolean = false // checks if in edit mode

    override fun onCreate(savedInstanceState: Bundle?) { // initializes views and checks if editing
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_event)

        eventHelper = EventHelper(this) // initializes EventHelper

        eventNameEditText = findViewById(R.id.edit_event_name)
        eventDateEditText = findViewById(R.id.edit_event_date)
        eventBudgetEditText = findViewById(R.id.edit_event_budget)
        eventPeopleEditText = findViewById(R.id.edit_event_people)
        eventDescriptionEditText = findViewById(R.id.edit_event_description)
        saveButton = findViewById(R.id.btn_save_event)

        isEditing = intent.getBooleanExtra("isEditing", false) // checks if editing an event

        if (isEditing) { // loads event details if editing
            editingEvent = Event(
                name = intent.getStringExtra("eventName") ?: "",
                date = intent.getStringExtra("eventDate") ?: "",
                budget = intent.getStringExtra("eventBudget") ?: "",
                people = intent.getStringExtra("eventPeople") ?: "",
                description = intent.getStringExtra("eventDescription") ?: ""
            )
            editingEvent?.let { event ->
                eventNameEditText.setText(event.name)
                eventDateEditText.setText(event.date)
                eventBudgetEditText.setText(event.budget)
                eventPeopleEditText.setText(event.people)
                eventDescriptionEditText.setText(event.description)
            }
        }

        saveButton.setOnClickListener { // saves event on button click
            saveEvent()
        }
    }

    private fun saveEvent() { // saves or updates the event
        val eventName = eventNameEditText.text.toString()
        val eventDate = eventDateEditText.text.toString()
        val eventBudget = eventBudgetEditText.text.toString()
        val eventPeople = eventPeopleEditText.text.toString()
        val eventDescription = eventDescriptionEditText.text.toString()

        if (eventName.isNotEmpty() && eventDate.isNotEmpty() && eventBudget.isNotEmpty() && eventPeople.isNotEmpty() && eventDescription.isNotEmpty()) {
            val newEvent = Event(eventName, eventDate, eventBudget, eventPeople, eventDescription)
            val events = eventHelper.getEvents().toMutableList()

            if (isEditing && editingEvent != null) { // updates event if in edit mode
                val index = events.indexOf(editingEvent)
                if (index != -1) {
                    events[index] = newEvent
                }
            } else { // adds a new event if not editing
                events.add(newEvent)
            }

            eventHelper.saveEvents(events) // saves events list to storage
            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show()
            finish() // closes the activity
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
