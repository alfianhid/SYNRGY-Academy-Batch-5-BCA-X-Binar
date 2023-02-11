package com.binarxbca.chapter6.service.impl;

import com.binarxbca.chapter6.exception.BadRequestException;
import com.binarxbca.chapter6.exception.ResourceNotFoundException;
import com.binarxbca.chapter6.model.Film;
import com.binarxbca.chapter6.model.User;
import com.binarxbca.chapter6.model.payload.response.FilmResponse;
import com.binarxbca.chapter6.repository.FilmRepository;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import com.binarxbca.chapter6.model.payload.request.FilmRequest;
import com.binarxbca.chapter6.repository.UserRepository;
import com.binarxbca.chapter6.service.FilmService;
import com.binarxbca.chapter6.service.ScheduleService;
import com.binarxbca.chapter6.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.binarxbca.chapter6.utils.AppConstants.*;

@Service
public class FilmServiceImpl implements FilmService {
	@Autowired
	private FilmRepository filmRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ScheduleService scheduleService;

	@Override
	public PagedResponse<FilmResponse> getAllFilms(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

		Page<Film> films = filmRepository.findAll(pageable);

		if (films.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), films.getNumber(), films.getSize(),
					films.getTotalElements(), films.getTotalPages(), films.isLast());
		}

		List<FilmResponse> filmResponse = Arrays.asList(modelMapper.map(films.getContent(), FilmResponse[].class));

		return new PagedResponse<>(filmResponse, films.getNumber(), films.getSize(),
				films.getTotalElements(), films.getTotalPages(), films.isLast());
	}

	@Override
	public ResponseEntity<Film> addFilm(FilmRequest filmRequest) {
		try {
			Film film = new Film();

			modelMapper.map(filmRequest, film);

			for (int i = 0; i < film.getSchedules().size(); i++) {
				scheduleService.updateScheduleWithFilmId(film.getSchedules().get(i).getId(), film.getId());

				film.getSchedules().get(i).setId(film.getId());
			}

			Film film1 = filmRepository.save(film);

			return new ResponseEntity<>(film1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Film> getFilm(Long id) {
		Film film = filmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(FILM, ID, id));
		return new ResponseEntity<>(film, HttpStatus.OK);
	}

	@Override
	public PagedResponse<FilmResponse> getCurrentShowing(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

		Page<Film> films = filmRepository.findCurrentShowing(pageable);

		if (films.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), films.getNumber(), films.getSize(),
					films.getTotalElements(), films.getTotalPages(), films.isLast());
		}

		List<FilmResponse> filmResponse = Arrays.asList(modelMapper.map(films.getContent(), FilmResponse[].class));

		return new PagedResponse<>(filmResponse, films.getNumber(), films.getSize(),
				films.getTotalElements(), films.getTotalPages(), films.isLast());
	}

	@Override
	public Film getFilmById(Long id) {
		if (!filmRepository.findById(id).isPresent()) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Film ID is not found");
			throw new BadRequestException(apiResponse);
		}

		return filmRepository.findById(id).get();
	}

	@Override
	public ResponseEntity<FilmResponse> updateFilm(Long id, FilmRequest filmRequest) {
		Film film = filmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(FILM, ID, id));

		film.setTitle(film.getTitle());
		film.setRating(film.getRating());
		film.setStock(film.getStock());
		film.setTicketPrice(film.getTicketPrice());
		film.setIsShowing(film.getIsShowing());

		Film updatedFilm = filmRepository.save(film);

		FilmResponse filmResponse = new FilmResponse();

		modelMapper.map(updatedFilm, filmResponse);

		return new ResponseEntity<>(filmResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResponse> deleteFilm(Long id) {
		filmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(FILM, ID, id));

		filmRepository.deleteById(id);

		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "You successfully deleted film"), HttpStatus.OK);
	}

	@Override
	public PagedResponse<Film> getByUserId(Long userId, int page, int size) {
		User user = userRepository.findById(userId).get();

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

		Page<Film> films = filmRepository.findByUserId(user.getId(), pageable);

		List<Film> content = films.getNumberOfElements() > 0 ? films.getContent() : Collections.emptyList();

		return new PagedResponse<>(content, films.getNumber(), films.getSize(), films.getTotalElements(), films.getTotalPages(), films.isLast());
	}
}
