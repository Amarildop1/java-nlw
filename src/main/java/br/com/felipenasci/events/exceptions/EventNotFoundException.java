package br.com.felipenasci.events.exceptions;

public class EventNotFoundException extends RuntimeException{
  public EventNotFoundException(String eventName) {
    super("Event " + eventName + " not exists");
  }
}
