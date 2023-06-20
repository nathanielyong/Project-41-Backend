package com.snowtheghost.redistributor.database.repositories;

import com.snowtheghost.redistributor.database.models.GameWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameWinnerRepository extends JpaRepository<GameWinner, Long> {

}
