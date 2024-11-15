package com.example.eventmanagement

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventmanagement.ui.theme.EventManagementTheme

class MainActivity : ComponentActivity() { // main activity to display and manage events

    private lateinit var eventHelper: EventHelper // helper class for managing event storage
    private var selectedEvent: Event? by mutableStateOf(null) // holds the currently selected event

    override fun onCreate(savedInstanceState: Bundle?) { // initializes activity and UI components
        super.onCreate(savedInstanceState)
        eventHelper = EventHelper(this)

        setContent {
            EventManagementTheme {
                var query by remember { mutableStateOf("") }
                val events = remember { mutableStateListOf<Event>().apply { addAll(eventHelper.getEvents()) } }
                val filteredEvents = remember {
                    derivedStateOf {
                        if (query.isEmpty()) events else events.filter {
                            it.name.contains(query, ignoreCase = true)
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    val intent = Intent(this@MainActivity, AddEditEventActivity::class.java)
                                    startActivity(intent)
                                },
                                containerColor = Color(0xFF00796B)
                            ) {
                                Text(text = "+", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            FloatingActionButton(
                                onClick = {
                                    selectedEvent?.let {
                                        deleteEvent(it, events)
                                    } ?: Toast.makeText(this@MainActivity, "No event selected", Toast.LENGTH_SHORT).show()
                                },
                                containerColor = Color(0xFFD32F2F)
                            ) {
                                Text(text = "Delete", color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color(0xFFF5F5F5))
                    ) {
                        SearchBar(query = query, onQueryChange = { query = it }) // search bar at the top
                        EventList(
                            events = filteredEvents.value,
                            onEventClick = { event ->
                                handleEventClick(event, events)
                            }
                        )
                    }
                }
            }
        }
    }

    private var lastClickTime = 0L // variable to detect double-clicks

    private fun handleEventClick(event: Event, events: MutableList<Event>) { // handles click and double-click for events
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime < 300) {
            val intent = Intent(this@MainActivity, AddEditEventActivity::class.java).apply {
                putExtra("eventName", event.name)
                putExtra("eventDate", event.date)
                putExtra("eventBudget", event.budget)
                putExtra("eventPeople", event.people)
                putExtra("eventDescription", event.description)
                putExtra("isEditing", true)
            }
            startActivity(intent)
            selectedEvent = null
        } else {
            selectedEvent = event
            Toast.makeText(this, "${event.name} selected", Toast.LENGTH_SHORT).show()
        }
        lastClickTime = currentTime
    }

    private fun deleteEvent(event: Event, events: MutableList<Event>) { // deletes the selected event from the list
        events.remove(event)
        eventHelper.saveEvents(events)
        selectedEvent = null
        Toast.makeText(this, "${event.name} deleted", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) { // composable function for the search bar
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = { Text("Search Events") }
    )
}

@Composable
fun EventList(events: List<Event>, modifier: Modifier = Modifier, onEventClick: (Event) -> Unit) { // displays list of events
    LazyColumn(modifier = modifier) {
        itemsIndexed(events) { index, event ->
            EventItem(event = event, index = index + 1, onClick = { onEventClick(event) })
        }
    }
}

@Composable
fun EventItem(event: Event, index: Int, onClick: () -> Unit) { // displays individual event item with index
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index.",
                color = Color(0xFF00796B),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = event.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004D40)
                )
                Text(
                    text = event.date,
                    fontSize = 14.sp,
                    color = Color(0xFF00796B)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventListPreview() { // preview for the event list
    EventManagementTheme {
        EventList(
            events = listOf(
                Event("Preview Event 1", "2023-11-14", "100", "50", "Description 1"),
                Event("Preview Event 2", "2023-11-15", "200", "75", "Description 2"),
                Event("Preview Event 3", "2023-11-16", "150", "30", "Description 3")
            ),
            onEventClick = {}
        )
    }
}
