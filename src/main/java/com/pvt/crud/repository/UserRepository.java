package com.pvt.crud.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pvt.crud.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);

}
