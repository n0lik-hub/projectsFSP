package com.stoloto.bonusarena.repository;

import com.stoloto.bonusarena.model.RoomConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomConfigurationRepository extends JpaRepository<RoomConfiguration, Long> {
    
    List<RoomConfiguration> findByActiveTrue();
}
