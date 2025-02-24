package br.com.felipenasci.events.service;

import br.com.felipenasci.events.dto.SubscriptionRankingItem;
import br.com.felipenasci.events.dto.SubscriptionRankingByUser;
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
import java.util.stream.IntStream;

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

    User indicator = null;

    if(userIndicatorId != null){
      indicator = userRepository.findById(userIndicatorId).orElse(null);

      if (indicator == null){
        throw new UserIndicatorNotFound(userIndicatorId.toString());
      }
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

    Integer subscriberId = newSubscription.getSubscriber().getId();
    String url = "localhost:8080/" + newSubscription.getEvent().getPrettyName() + '/'+ subscriberId;
    return new SubscriptionResponse(newSubscription.getId(), url, subscriberId);
  }

  public List<SubscriptionRankingItem> getCompleteRanking(String prettyName){
    Event event = eventRepository.findByPrettyName(prettyName);
    if (event == null) {
      throw new EventNotFoundException(prettyName);
    }

    var ranking = subscriptionRepository.generateRanking(event.getEventId());
    var length = ranking.size();

    return length >= 3? ranking.subList(0,3): ranking.subList(0, length);
  }

  public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId){
    List<SubscriptionRankingItem> rankings = getCompleteRanking(prettyName);
    SubscriptionRankingItem item = rankings.stream().filter(ranking -> ranking.userId().equals(userId)).findFirst().orElse(null);

    if (item == null){
      throw new UserIndicatorNotFound(userId.toString());
    }

    var position = IntStream.range(0, rankings.size()).filter(index -> rankings.get(index).userId().equals(userId)).findFirst().getAsInt();

    return new SubscriptionRankingByUser(item, position + 1);
  }
}
