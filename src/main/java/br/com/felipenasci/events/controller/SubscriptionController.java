package br.com.felipenasci.events.controller;

import java.util.List;

import br.com.felipenasci.events.exceptions.SubscriptionConflictException;
import br.com.felipenasci.events.exceptions.UserIndicatorNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipenasci.events.dto.ErrorMessage;
import br.com.felipenasci.events.exceptions.EventNotFoundException;
import br.com.felipenasci.events.model.Subscription;
import br.com.felipenasci.events.model.User;
import br.com.felipenasci.events.service.EventService;
import br.com.felipenasci.events.service.SubscriptionService;

@RestController
public class SubscriptionController {

  @Autowired
  private EventService eventService;

  @Autowired
  private SubscriptionService subscriptionService;

  @GetMapping("/subscriptions")
  public List<Subscription> createNewSubscription() {
    return subscriptionService.getAllSubscriptions();
  }

  @PostMapping({"/subscriptions/{prettyName}", "/subscriptions/{prettyName}/{userId}"})
  public ResponseEntity<?> createNewSubscription(@PathVariable String prettyName,
      @RequestBody User subscriber, @PathVariable(required = false) Integer userId) {
    try {

      var subscription = subscriptionService.createNewSubscription(prettyName, subscriber, userId);

      if (subscription == null) {
        return ResponseEntity.notFound().build();
      }

      return ResponseEntity.status(201).body(subscription);
    } catch (EventNotFoundException | UserIndicatorNotFound exception) {
      return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
    } catch (SubscriptionConflictException exception) {
      return ResponseEntity.status(409).body(new ErrorMessage(exception.getMessage()));

    }

  }

  @GetMapping("/subscriptions/{prettyName}/ranking")
  public ResponseEntity<?> getSubscriptionRankingByEvent(@PathVariable String prettyName){
      try {
        return ResponseEntity.ok(subscriptionService.getCompleteRanking(prettyName));
      } catch (EventNotFoundException exception){
        return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
      }
  }

  @GetMapping("/subscriptions/{prettyName}/ranking/{userId}")
  public ResponseEntity<?> getSubscriptionRankingByUser(@PathVariable String prettyName, @PathVariable Integer userId){
    try {

      return ResponseEntity.ok(subscriptionService.getRankingByUser(prettyName, userId));
    } catch (UserIndicatorNotFound exception){
      return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
    }
  }

}
