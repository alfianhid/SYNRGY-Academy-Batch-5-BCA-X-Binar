package com.binarxbca.chapter6.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
