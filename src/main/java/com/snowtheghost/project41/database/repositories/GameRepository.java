package com.snowtheghost.project41.database.repositories;

import com.snowtheghost.project41.database.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {

}
