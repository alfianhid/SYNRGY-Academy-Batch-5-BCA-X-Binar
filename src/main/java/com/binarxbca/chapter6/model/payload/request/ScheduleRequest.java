package com.binarxbca.chapter6.model.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ScheduleRequest {
	@NotBlank
	@Schema(example = "14 Februari 2022")
	@Size(min = 10, message = "show_date must be minimum 10 characters")
	private String showDate;

	@NotBlank
	@Schema(example = "19:00 WIB")
	@Size(min = 5, message = "start_time must be minimum 5 characters")
	private String startTime;

	@NotBlank
	@Schema(example = "21:00 WIB")
	@Size(min = 5, message = "end_time must be minimum 5 characters")
	private String endTime;

	@NotNull
	private Long filmId;
}
