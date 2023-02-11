package com.binarxbca.chapter6.service;

import com.binarxbca.chapter6.model.Film;
import com.binarxbca.chapter6.model.payload.request.FilmRequest;
import com.binarxbca.chapter6.model.payload.response.FilmResponse;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FilmService {
	PagedResponse<FilmResponse> getAllFilms(int page, int size);

	ResponseEntity<Film> addFilm(FilmRequest filmRequest);

	ResponseEntity<Film> getFilm(Long id);

	PagedResponse<FilmResponse> getCurrentShowing(int page, int size);

	Film getFilmById(Long id);
	ResponseEntity<FilmResponse> updateFilm(Long id, FilmRequest filmRequest);

	ResponseEntity<ApiResponse> deleteFilm(Long id);

	PagedResponse<Film> getByUserId(Long id, int page, int size);
}
