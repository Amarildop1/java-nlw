package br.com.felipenasci.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.felipenasci.events.model.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
  public Event findByPrettyName(String prettyName);
}
