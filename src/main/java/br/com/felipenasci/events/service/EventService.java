package br.com.felipenasci.events.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipenasci.events.model.Event;
import br.com.felipenasci.events.repository.EventRepository;

@Service
public class EventService {

  @Autowired
  private EventRepository eventRepository;

  public Event addNewEvent(Event event) {
    var prettyName = event.getTitle().toLowerCase().replace(" ", "-");
    event.setPrettyName(prettyName);
    return eventRepository.save(event);
  }

  public List<Event> getAllEvents() {
    return (List<Event>) eventRepository.findAll();
  }

  public Event getEventByPrettyName(String prettyName) {
    return eventRepository.findByPrettyName(prettyName);
  }
}
