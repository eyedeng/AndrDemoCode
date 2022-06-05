package com.example.andrdemocode.grouprv.adapter;

import com.example.andrdemocode.grouprv.model.Event;

public class EventItem implements ListItem {
    public Event event;

    public EventItem(Event event) {
        this.event = event;
    }

    @Override
    public int getType() {
        return TYPE_EVENT;
    }
}
