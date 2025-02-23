package br.com.felipenasci.events.exceptions;

import br.com.felipenasci.events.model.User;

public class SubscriptionConflictException extends RuntimeException {
    public SubscriptionConflictException(User user){
        super("User " + user.getName() + " already subscribed");
    }
}
