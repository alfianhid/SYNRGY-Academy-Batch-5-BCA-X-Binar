package com.binarxbca.chapter6.service.impl;

import com.binarxbca.chapter6.exception.ResourceNotFoundException;
import com.binarxbca.chapter6.model.Film;
import com.binarxbca.chapter6.model.Schedule;
import com.binarxbca.chapter6.model.payload.request.ScheduleRequest;
import com.binarxbca.chapter6.model.payload.response.ScheduleResponse;
import com.binarxbca.chapter6.repository.FilmRepository;
import com.binarxbca.chapter6.repository.ScheduleRepository;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import com.binarxbca.chapter6.service.ScheduleService;
import com.binarxbca.chapter6.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.binarxbca.chapter6.utils.AppConstants.*;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private FilmRepository filmRepository;

	@Override
	public PagedResponse<ScheduleResponse> getAllSchedules(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

		Page<Schedule> schedules = scheduleRepository.findAll(pageable);

		List<ScheduleResponse> scheduleResponse = new ArrayList<>(schedules.getContent().size());

		for (Schedule schedule : schedules.getContent()) {
			scheduleResponse.add(new ScheduleResponse(schedule.getId(), schedule.getShowDate(),
					schedule.getStartTime(), schedule.getEndTime(), schedule.getFilm().getId()));
		}

		if (schedules.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), schedules.getNumber(), schedules.getSize(),
					schedules.getTotalElements(), schedules.getTotalPages(), schedules.isLast());
		}

		return new PagedResponse<>(scheduleResponse, schedules.getNumber(), schedules.getSize(),
				schedules.getTotalElements(), schedules.getTotalPages(), schedules.isLast());
	}

	@Override
	public ScheduleResponse getSchedule(Long id) {
		Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SCHEDULE, ID, id));

		return new ScheduleResponse(schedule.getId(), schedule.getShowDate(),
				schedule.getStartTime(), schedule.getEndTime(), schedule.getFilm().getId());
	}

	@Override
	public void updateScheduleWithFilmId(Long id, Long filmId) {
		scheduleRepository.updateScheduleWithFilm(id, filmId);
	}

	@Override
	public ScheduleResponse updateSchedule(Long id, ScheduleRequest scheduleRequest) {
		Film film = filmRepository.findById(scheduleRequest.getFilmId())
				.orElseThrow(() -> new ResourceNotFoundException(FILM, ID, scheduleRequest.getFilmId()));
		Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SCHEDULE, ID, id));

		schedule.setShowDate(scheduleRequest.getShowDate());
		schedule.setStartTime(scheduleRequest.getStartTime());
		schedule.setEndTime(scheduleRequest.getEndTime());
		schedule.setFilm(film);

		Schedule updatedSchedule = scheduleRepository.save(schedule);

		return new ScheduleResponse(updatedSchedule.getId(), updatedSchedule.getShowDate(),
					updatedSchedule.getStartTime(), updatedSchedule.getEndTime(), updatedSchedule.getFilm().getId());
	}

	@Override
	public ScheduleResponse addSchedule(ScheduleRequest scheduleRequest) {
		Schedule schedule = scheduleRepository.findById(scheduleRequest.getFilmId())
				.orElseThrow(() -> new ResourceNotFoundException(FILM, ID, scheduleRequest.getFilmId()));

		Schedule schedules = new Schedule(scheduleRequest.getShowDate(), scheduleRequest.getStartTime(),
				scheduleRequest.getEndTime(), schedule.getFilm());

		Schedule newSchedule = scheduleRepository.save(schedules);

		return new ScheduleResponse(newSchedule.getId(), newSchedule.getShowDate(), newSchedule.getStartTime(),
				newSchedule.getEndTime(), newSchedule.getFilm().getId());
	}

	@Override
	public ApiResponse deleteSchedule(Long id) {
		scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SCHEDULE, ID, id));

		scheduleRepository.deleteById(id);

		return new ApiResponse(Boolean.TRUE, "Schedule deleted successfully");
	}

	@Override
	public PagedResponse<ScheduleResponse> getAllSchedulesByFilmId(Long filmId, int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

		Page<Schedule> schedules = scheduleRepository.findByFilmId(filmId, pageable);

		List<ScheduleResponse> scheduleResponse = new ArrayList<>(schedules.getContent().size());

		for (Schedule schedule : schedules.getContent()) {
			scheduleResponse.add(new ScheduleResponse(schedule.getId(), schedule.getShowDate(),
					schedule.getStartTime(), schedule.getEndTime(), schedule.getFilm().getId()));
		}

		if (schedules.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), schedules.getNumber(), schedules.getSize(),
					schedules.getTotalElements(), schedules.getTotalPages(), schedules.isLast());
		}

		return new PagedResponse<>(scheduleResponse, schedules.getNumber(), schedules.getSize(),
				schedules.getTotalElements(), schedules.getTotalPages(), schedules.isLast());
	}
}
