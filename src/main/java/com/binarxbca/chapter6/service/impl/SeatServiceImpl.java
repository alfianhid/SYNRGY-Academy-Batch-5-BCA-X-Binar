package com.binarxbca.chapter6.service.impl;

import com.binarxbca.chapter6.exception.ResourceNotFoundException;
import com.binarxbca.chapter6.model.Seat;
import com.binarxbca.chapter6.repository.SeatRepository;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import com.binarxbca.chapter6.service.SeatService;
import com.binarxbca.chapter6.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.binarxbca.chapter6.utils.AppConstants.SEAT;

@Service
public class SeatServiceImpl implements SeatService {
	@Autowired
	private SeatRepository seatRepository;

	@Override
	public PagedResponse<Seat> getAllSeats(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		Page<Seat> seats = seatRepository.findAll(pageable);

		List<Seat> content = seats.getNumberOfElements() == 0 ? Collections.emptyList() : seats.getContent();

		return new PagedResponse<>(content, seats.getNumber(), seats.getSize(), seats.getTotalElements(),
				seats.getTotalPages(), seats.isLast());
	}

	@Override
	public ResponseEntity<Seat> getSeat(Long id) {
		Seat seat = seatRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SEAT, "id", id));
		return new ResponseEntity<>(seat, HttpStatus.OK);
	}

	@Override
	public PagedResponse<Seat> getAvailableSeats(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		Page<Seat> seats = seatRepository.getAvailableSeats(pageable);

		List<Seat> content = seats.getNumberOfElements() == 0 ? Collections.emptyList() : seats.getContent();

		return new PagedResponse<>(content, seats.getNumber(), seats.getSize(), seats.getTotalElements(),
				seats.getTotalPages(), seats.isLast());
	}

	@Override
	public ResponseEntity<Seat> addSeat(Seat seat) {
		Seat newSeat = seatRepository.save(seat);
		return new ResponseEntity<>(newSeat, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Seat> updateSeat(Long id, Seat newSeat) {
		Seat seat = seatRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SEAT, "id", id));

		seat.setSeatId(newSeat.getSeatId());
		seat.setStudioName(newSeat.getStudioName());

		Seat updatedSeat = seatRepository.save(seat);

		return new ResponseEntity<>(updatedSeat, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResponse> deleteSeat(Long id) {
		seatRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(SEAT, "id", id));

		seatRepository.deleteById(id);

		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "You successfully deleted seat"), HttpStatus.OK);
	}
}






















