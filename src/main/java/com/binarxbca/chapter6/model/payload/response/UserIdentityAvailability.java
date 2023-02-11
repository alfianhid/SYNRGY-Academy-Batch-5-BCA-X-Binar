package com.binarxbca.chapter6.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserIdentityAvailability {
	private Boolean available;
}
