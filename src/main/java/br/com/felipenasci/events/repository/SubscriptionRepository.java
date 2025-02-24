package br.com.felipenasci.events.repository;

import br.com.felipenasci.events.dto.SubscriptionRankingItem;
import br.com.felipenasci.events.model.Event;
import br.com.felipenasci.events.model.Subscription;

import br.com.felipenasci.events.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

    public Subscription findByEventAndSubscriber(Event event, User user);
    @Query( value = "SELECT " +
                "count(subscription_number) as amount, " +
                "indication_user_id, " +
                "user_name " +
            "FROM db_events.tbl_subscription as subscriptions " +
                "INNER JOIN db_events.tbl_user as users " +
                    "ON subscriptions.indication_user_id = users.user_id " +
            "WHERE " +
                "indication_user_id IS NOT NULL " +
                "AND event_id = :eventId " +
            "GROUP BY (indication_user_id) " +
            "ORDER BY amount DESC;", nativeQuery = true)
    public List<SubscriptionRankingItem> generateRanking(@Param("eventId") Integer eventId);

}
