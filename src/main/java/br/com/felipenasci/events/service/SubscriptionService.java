package br.com.felipenasci.events.service;

import br.com.felipenasci.events.dto.SubscriptionResponse;
import br.com.felipenasci.events.exceptions.SubscriptionConflictException;
import br.com.felipenasci.events.exceptions.UserIndicatorNotFound;
import br.com.felipenasci.events.model.User;
import br.com.felipenasci.events.exceptions.EventNotFoundException;
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

  public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userIndicatorId) {

    Event event = eventRepository.findByPrettyName(eventName);
    if (event == null) {
      throw new EventNotFoundException(eventName);
    }

    User storedUser = userRepository.findByEmail(user.getEmail());
    if (storedUser == null) {
      storedUser = userRepository.save(user);
    }

    User indicator = userRepository.findById(userIndicatorId).orElse(null);
    if (indicator == null){
      throw new UserIndicatorNotFound(userIndicatorId.toString());
    }

    Subscription tempSubscription = subscriptionRepository.findByEventAndSubscriber(event, storedUser);
    if(tempSubscription != null){
      throw new SubscriptionConflictException(storedUser);
    }

    Subscription subscription = new Subscription();
    subscription.setEvent(event);
    subscription.setSubscriber(storedUser);
    subscription.setIndication(indicator);

    Subscription newSubscription = subscriptionRepository.save(subscription);

    String url = "http://codecraft.com/" + newSubscription.getEvent().getPrettyName() + '/'+ newSubscription.getSubscriber().getId();
    return new SubscriptionResponse(newSubscription.getId(), url);
  }

}
