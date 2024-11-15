package com.example.eventmanagement

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("EventPrefs", Context.MODE_PRIVATE) // initializes shared preferences for storing events
    private val gson = Gson() // initializes gson for JSON conversion

    fun saveEvents(events: List<Event>) { // saves a list of events to shared preferences
        val editor = sharedPreferences.edit()
        val eventsJson = gson.toJson(events)
        editor.putString("events", eventsJson)
        editor.apply()
    }

    fun getEvents(): List<Event> { // retrieves a list of events from shared preferences
        val eventsJson = sharedPreferences.getString("events", null)
        val type = object : TypeToken<List<Event>>() {}.type
        return if (eventsJson != null) {
            gson.fromJson(eventsJson, type)
        } else {
            emptyList()
        }
    }
}
