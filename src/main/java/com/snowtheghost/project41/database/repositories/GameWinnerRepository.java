package com.snowtheghost.project41.database.repositories;

import com.snowtheghost.project41.database.models.GameWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameWinnerRepository extends JpaRepository<GameWinner, Long> {

}
