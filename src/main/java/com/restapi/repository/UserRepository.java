package com.restapi.repository;

import com.restapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String username);

	public List<User> findUsersByUsernameIsNotNull();

	public Long deleteUserByUsername(String username);

	public List<User> findByFamilyId(String familyId);

	public List<User> findByUsernameContains(String username);

}
