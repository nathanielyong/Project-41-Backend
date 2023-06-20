package com.snowtheghost.redistributor.database.repositories;

import com.snowtheghost.redistributor.database.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
}
