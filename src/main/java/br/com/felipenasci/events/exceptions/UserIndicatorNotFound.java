package br.com.felipenasci.events.exceptions;

public class UserIndicatorNotFound extends RuntimeException{
    public UserIndicatorNotFound(String userIndicator){
        super("User indicator " + userIndicator + " do not exist");
    }
}
