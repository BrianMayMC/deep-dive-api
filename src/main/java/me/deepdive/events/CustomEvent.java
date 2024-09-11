package me.deepdive.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class represents a custom event. Make a class extend this, and you'll be able to
 * add an event to your servers event loop.
 */
public abstract class CustomEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public CustomEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}