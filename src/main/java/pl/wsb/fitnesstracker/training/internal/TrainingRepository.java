package pl.wsb.fitnesstracker.training.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.wsb.fitnesstracker.training.api.Training;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Modifying
    @Query("DELETE FROM Training t WHERE t.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
