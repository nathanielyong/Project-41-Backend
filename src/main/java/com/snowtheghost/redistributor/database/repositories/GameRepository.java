package com.snowtheghost.redistributor.database.repositories;

import com.snowtheghost.redistributor.database.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {

}
