package com.whereitgo.repository;

import com.whereitgo.model.WIGUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WIGUserRepository extends JpaRepository<WIGUser, String> {

    Optional<WIGUser> findByEmail(String email);

    Optional<WIGUser> findByPhoneNumber(String phoneNumber);

    Optional<WIGUser> findByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
