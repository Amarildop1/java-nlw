package br.com.felipenasci.events.repository;

import br.com.felipenasci.events.model.Subscription;

import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

}
