package com.binarxbca.chapter6.service;

import com.binarxbca.chapter6.model.payload.request.ScheduleRequest;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import com.binarxbca.chapter6.model.payload.response.ScheduleResponse;

public interface ScheduleService {

	PagedResponse<ScheduleResponse> getAllSchedules(int page, int size);

	ScheduleResponse getSchedule(Long id);

	void updateScheduleWithFilmId(Long id, Long filmId);

	ScheduleResponse updateSchedule(Long id, ScheduleRequest scheduleRequest);

	ScheduleResponse addSchedule(ScheduleRequest scheduleRequest);

	ApiResponse deleteSchedule(Long id);

	PagedResponse<ScheduleResponse> getAllSchedulesByFilmId(Long filmId, int page, int size);
}