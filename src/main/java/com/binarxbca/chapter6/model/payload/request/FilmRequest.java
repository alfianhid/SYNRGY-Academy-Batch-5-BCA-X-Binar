package com.binarxbca.chapter6.model.payload.request;

import com.binarxbca.chapter6.model.Schedule;
import com.binarxbca.chapter6.model.User;
import com.binarxbca.chapter6.model.payload.DateAuditPayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class FilmRequest extends DateAuditPayload {
	@NotBlank
	@Schema(example = "Ayat-Ayat Cinta")
	@Size(max = 100, message = "title must be maximum 100 characters")
	private String title;

	@NotBlank
	@Schema(example = "4.5")
	@Size(max = 3, message = "rating must be maximum 3 characters")
	private String rating;

	@NotBlank
	@Schema(example = "100")
	private Integer stock;

	@NotBlank
	@Schema(example = "30000.0")
	private Double ticketPrice;

	@NotBlank
	@Schema(example = "true")
	private Boolean isShowing;

	private List<Schedule> schedules;

	public List<Schedule> getSchedule() {

		return schedules == null ? null : new ArrayList<>(schedules);
	}

	public void setSchedule(List<Schedule> schedules) {

		if (schedules == null) {
			this.schedules = null;
		} else {
			this.schedules = Collections.unmodifiableList(schedules);
		}
	}
}
