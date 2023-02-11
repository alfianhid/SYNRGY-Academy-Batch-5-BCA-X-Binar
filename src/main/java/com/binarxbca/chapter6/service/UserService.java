package com.binarxbca.chapter6.service;

import com.binarxbca.chapter6.model.User;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.UserIdentityAvailability;
import com.binarxbca.chapter6.model.payload.response.UserProfile;

import java.util.List;

public interface UserService {
	UserIdentityAvailability checkUsernameAvailability(String username);

	UserIdentityAvailability checkEmailAvailability(String email);

	User addUser(User user);

	List<User> getAllUser();

	UserProfile getUserProfile(String username);

	User getUserById(Long id);

	User updateUser(User user, String username);

	ApiResponse deleteUser(String username);
}