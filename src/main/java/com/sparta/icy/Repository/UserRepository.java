package com.sparta.icy.Repository;

import com.sparta.icy.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
