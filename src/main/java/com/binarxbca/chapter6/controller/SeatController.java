package com.binarxbca.chapter6.controller;

import com.binarxbca.chapter6.model.Seat;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import com.binarxbca.chapter6.service.SeatService;
import com.binarxbca.chapter6.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/chapter6/auth/seats")
public class SeatController {
	@Autowired
	private SeatService seatService;

	@SecurityRequirement(name = "bearerAuth")
	@GetMapping
	public PagedResponse<Seat> getAllSeats(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		return seatService.getAllSeats(page, size);
	}

	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/available")
	public PagedResponse<Seat> getAvailableSeats(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		return seatService.getAvailableSeats(page, size);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/add")
	public ResponseEntity<Seat> addSeat(@Valid @RequestBody Seat seat) {

		return seatService.addSeat(seat);
	}

	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/{id}")
	public ResponseEntity<Seat> getSeat(@PathVariable(name = "id") Long id) {
		return seatService.getSeat(id);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/update/{id}")
	public ResponseEntity<Seat> updateSeat(@PathVariable(name = "id") Long id,
			@Valid @RequestBody Seat seat) {
		return seatService.updateSeat(id, seat);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deleteSeat(@PathVariable(name = "id") Long id) {
		return seatService.deleteSeat(id);
	}
}
