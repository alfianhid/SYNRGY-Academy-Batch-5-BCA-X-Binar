package com.binarxbca.chapter6.model.payload.response;

import lombok.Data;

@Data
public class ScheduleResponse {
	private Long id;
	private String showDate;
	private String startTime;
	private String endTime;
	private Long filmId;

	public ScheduleResponse(Long id, String showDate, String startTime, String endTime, Long filmId) {
		this.id = id;
		this.showDate = showDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.filmId = filmId;
	}
}
