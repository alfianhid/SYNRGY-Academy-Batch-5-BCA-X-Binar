package com.binarxbca.chapter6.service;

import com.binarxbca.chapter6.model.Seat;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface SeatService {

	PagedResponse<Seat> getAllSeats(int page, int size);

	ResponseEntity<Seat> getSeat(Long id);

	PagedResponse<Seat> getAvailableSeats(int page, int size);

	ResponseEntity<Seat> addSeat(Seat seat);

	ResponseEntity<Seat> updateSeat(Long id, Seat newSeat);

	ResponseEntity<ApiResponse> deleteSeat(Long id);

}
