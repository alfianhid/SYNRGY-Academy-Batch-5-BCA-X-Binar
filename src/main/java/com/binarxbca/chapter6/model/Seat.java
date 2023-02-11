package com.binarxbca.chapter6.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seats", uniqueConstraints = { @UniqueConstraint(columnNames = { "studio_name" }) })
public class Seat extends DateAudit {
	@EmbeddedId
	private SeatId seatId;

	@NotBlank
	@Column(name = "studio_name")
	private String studioName;

	@Column(name = "is_available")
	private Boolean isAvailable;

//	@JoinColumn(name = "schedule_id")
//	private Long scheduleId;
}
