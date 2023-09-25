package com.snowtheghost.project41.database.repositories;

import com.snowtheghost.project41.database.models.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
}
