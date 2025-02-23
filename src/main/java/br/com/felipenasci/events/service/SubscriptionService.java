package br.com.felipenasci.events.service;

import br.com.felipenasci.events.model.User;
import br.com.felipenasci.events.model.Event;
import br.com.felipenasci.events.model.Subscription;
import br.com.felipenasci.events.repository.EventRepository;
import br.com.felipenasci.events.repository.SubscriptionRepository;
import br.com.felipenasci.events.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  public List<Subscription> getAllSubscriptions() {
    return (List<Subscription>) subscriptionRepository.findAll();
  }

  public Subscription createNewSubscription(String eventName, User user) {

    Event event = eventRepository.findByPrettyName(eventName);

    userRepository.save(user);

    Subscription subscription = new Subscription();
    subscription.setEvent(event);
    subscription.setSubscriber(user);

    return subscriptionRepository.save(subscription);
  }

}
