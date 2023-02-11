package com.binarxbca.chapter6.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "films", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Film extends DateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "title")
	private String title;

	@NotBlank
	@Column(name = "rating")
	private String rating;

	@NotBlank
	@Column(name = "stock")
	private Integer stock;

	@NotBlank
	@Column(name = "ticket_price")
	private Double ticketPrice;

	@NotBlank
	@Column(name = "is_showing")
	private Boolean isShowing;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Schedule> schedules;

	public User getUser() {
		return user;
	}

	public List<Schedule> getSchedule() {
		return this.schedules == null ? null : new ArrayList<>(this.schedules);
	}

	public void setSchedule(List<Schedule> schedules) {
		if (schedules == null) {
			this.schedules = null;
		} else {
			this.schedules = Collections.unmodifiableList(schedules);
		}
	}
}
