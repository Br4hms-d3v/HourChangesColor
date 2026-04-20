package be.brahms.hourchangecolor.repository;

import be.brahms.hourchangecolor.model.HourColorEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ColorRepository extends JpaRepository<HourColorEntity, Long> {

  @Query("SELECT hc From HourColorEntity hc ORDER BY hc.updateAt DESC LIMIT 1")
  Optional<HourColorEntity> findLatest();
}
