package com.binarxbca.chapter6.repository;

import com.binarxbca.chapter6.model.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query(value = "update seats set is_available = ?1 where seat_row = ?2 and seat_column = ?3", nativeQuery = true)
    @Modifying
    void updateSeatAvailability(
            @Param("is_available") Boolean isAvailable,
            @Param("seat_row") String seatRow,
            @Param("seat_column") String seatColumn
    );

    @Query(value = "select seat_row, seat_column, is_available, schedule_id from seats where is_available=true", nativeQuery = true)
    Page<Seat> getAvailableSeats(Pageable pageable);
}
