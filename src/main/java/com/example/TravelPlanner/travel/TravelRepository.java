package com.example.TravelPlanner.travel;

import com.example.TravelPlanner.Event.Event;
import com.example.TravelPlanner.registration.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    Optional<Travel> findById(int id);

    List<Travel> findByAppUserId(Long appUserId);

    /*@Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Travel t " +
            "SET t.startDate = ?1 WHERE t.id = ?2")
        void updateStartDate(LocalDateTime startDate, Long id);*/
}
