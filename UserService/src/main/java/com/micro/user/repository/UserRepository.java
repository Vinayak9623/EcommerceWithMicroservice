package com.micro.user.repository;
import com.micro.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByMobileNumber(String mobileNumber);

    @Query("select u from User u where u.name like %:keyword% OR :keyword is null ")
    Page<User> searchUser(@Param("keyword") String keyword, Pageable pageable);
}
