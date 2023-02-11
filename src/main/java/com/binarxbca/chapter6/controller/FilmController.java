package com.binarxbca.chapter6.controller;

import com.binarxbca.chapter6.exception.ResponseEntityErrorException;
import com.binarxbca.chapter6.model.Film;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.FilmResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import com.binarxbca.chapter6.model.payload.request.FilmRequest;
import com.binarxbca.chapter6.service.FilmService;
import com.binarxbca.chapter6.utils.AppConstants;
import com.binarxbca.chapter6.utils.AppUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/chapter6/")
public class FilmController {
	@Autowired
	private FilmService filmService;

	@ExceptionHandler(ResponseEntityErrorException.class)
	public ResponseEntity<ApiResponse> handleExceptions(ResponseEntityErrorException exception) {
		return exception.getApiResponse();
	}

	@GetMapping("/public/films")
	public PagedResponse<FilmResponse> getAllFilms(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		AppUtils.validatePageNumberAndSize(page, size);

		return filmService.getAllFilms(page, size);
	}

	@GetMapping("/public/films/showing")
	public PagedResponse<FilmResponse> getCurrentShowingFilms(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		AppUtils.validatePageNumberAndSize(page, size);

		return filmService.getCurrentShowing(page, size);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/auth/films/add")
	public ResponseEntity<Film> addFilm(@Valid @RequestBody FilmRequest filmRequest) {
		return filmService.addFilm(filmRequest);
	}

	@GetMapping("/public/films/{id}")
	public ResponseEntity<Film> getFilm(@PathVariable(name = "id") Long id) {
		return filmService.getFilm(id);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/auth/films/update/{id}")
	public ResponseEntity<FilmResponse> updateFilm(@PathVariable(name = "id") Long id, @Valid @RequestBody FilmRequest filmRequest) {
		return filmService.updateFilm(id, filmRequest);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/auth/films/delete/{id}")
	public ResponseEntity<ApiResponse> deleteFilm(@PathVariable(name = "id") Long id) {
		return filmService.deleteFilm(id);
	}
}
