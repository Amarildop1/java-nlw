package br.com.felipenasci.events.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.felipenasci.events.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
  public User findByEmail(String email);
}
