package com.binarxbca.chapter6.repository;

import com.binarxbca.chapter6.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	@Query(value = "select * from schedules where film_id = ?1", nativeQuery = true)
	Page<Schedule> findByFilmId(@Param("film_id") Long filmId, Pageable pageable);

	@Query(value = "update schedules set film_id = ?1 where id = ?2", nativeQuery = true)
	@Modifying
	void updateScheduleWithFilm(
			@Param("film_id") Long filmId,
			@Param("id") Long id
	);

	@Query(value = "delete from schedules where film_id = ?1", nativeQuery = true)
	@Modifying
	void deleteScheduleByFilmId(@Param("film_id") Long filmId);
}
