package br.com.felipenasci.events.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

  @PostMapping("/subscriptions/{prettyName}")
  public ResponseEntity<Subscription> createNewSubscription(@PathVariable String prettyName,
      @RequestBody User subscriber) {
    Subscription subscription = subscriptionService.createNewSubscription(prettyName, subscriber);

    if (subscription == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(subscription);
  }

}
