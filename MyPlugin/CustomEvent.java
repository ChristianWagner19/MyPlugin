package mingle.MyPlugin;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class CustomEvent extends Event {
  private static final HandlerList handlers = new HandlerList();
  
  private String message;
  
  public CustomEvent(String example) {
    this.message = example;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public HandlerList getHandlers() {
    return handlers;
  }
  
  public static HandlerList getHandlerList() {
    return handlers;
  }
}
