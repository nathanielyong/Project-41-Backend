package com.snowtheghost.redistributor.database.repositories;

import com.snowtheghost.redistributor.database.models.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
}
