package com.snowtheghost.project41.database.repositories;

import com.snowtheghost.project41.database.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
}
