package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;  
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email=:email")
    public boolean existsByUserEmail(@Param("email") String email);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.accountNumber=:accountNumber")
	public boolean existsByAccountNumber(@Param("accountNumber")String accountNumber);

	@Query("FROM User u WHERE u.accountNumber=:accountNumber")
	public User findByAccountNumber(@Param("accountNumber")String accountNumber);



}
