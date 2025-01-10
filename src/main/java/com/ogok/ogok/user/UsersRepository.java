package com.ogok.ogok.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Users findByEmail(String email);

	List<Users> findAllByStatus(UsersStatus status);
}
