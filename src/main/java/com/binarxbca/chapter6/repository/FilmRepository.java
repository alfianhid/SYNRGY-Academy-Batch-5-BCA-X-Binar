package com.binarxbca.chapter6.repository;

import com.binarxbca.chapter6.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
	Page<Film> findByUserId(Long userId, Pageable pageable);

	@Query(value = "select * from films where is_showing=true", nativeQuery = true)
	Page<Film> findCurrentShowing(Pageable pageable);
}
