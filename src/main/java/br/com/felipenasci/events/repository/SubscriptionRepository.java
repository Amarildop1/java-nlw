package br.com.felipenasci.events.repository;

import br.com.felipenasci.events.model.Event;
import br.com.felipenasci.events.model.Subscription;

import br.com.felipenasci.events.model.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User user);
}
