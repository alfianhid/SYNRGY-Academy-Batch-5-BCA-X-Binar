package com.binarxbca.chapter6.controller;

import com.binarxbca.chapter6.model.payload.request.ScheduleRequest;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import com.binarxbca.chapter6.model.payload.response.ScheduleResponse;
import com.binarxbca.chapter6.service.ScheduleService;
import com.binarxbca.chapter6.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/chapter6/auth/schedules")
public class ScheduleController {
	@Autowired
	private ScheduleService scheduleService;

	@SecurityRequirement(name = "bearerAuth")
	@GetMapping
	public PagedResponse<ScheduleResponse> getAllSchedules(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		return scheduleService.getAllSchedules(page, size);
	}

//	@PostMapping
//	public PagedResponse<ScheduleResponse> getAllSchedulesByFilmId(
//			@RequestParam(name = "filmId", required = false) Long filmId,
//			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
//			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
//		return scheduleService.getAllSchedulesByFilmId(filmId, page, size);
//	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/add")
	public ResponseEntity<ScheduleResponse> addSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
		ScheduleResponse scheduleResponse = scheduleService.addSchedule(scheduleRequest);

		return new ResponseEntity<>(scheduleResponse, HttpStatus.OK);
	}

	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/{id}")
	public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable(name = "id") Long id) {
		ScheduleResponse scheduleResponse = scheduleService.getSchedule(id);

		return new ResponseEntity<>(scheduleResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/update/{id}")
	public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable(name = "id") Long id,
                                                        @Valid @RequestBody ScheduleRequest scheduleRequest) {

		ScheduleResponse scheduleResponse = scheduleService.updateSchedule(id, scheduleRequest);

		return new ResponseEntity<>(scheduleResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deleteSchedule(@PathVariable(name = "id") Long id) {
		ApiResponse apiResponse = scheduleService.deleteSchedule(id);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
}
