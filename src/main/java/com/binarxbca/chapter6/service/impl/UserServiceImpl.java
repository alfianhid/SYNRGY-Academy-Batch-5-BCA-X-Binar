package com.binarxbca.chapter6.service.impl;

import com.binarxbca.chapter6.repository.UserRepository;
import com.binarxbca.chapter6.exception.BadRequestException;
import com.binarxbca.chapter6.exception.ResourceNotFoundException;
import com.binarxbca.chapter6.model.User;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.UserIdentityAvailability;
import com.binarxbca.chapter6.model.payload.response.UserProfile;
import com.binarxbca.chapter6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.binarxbca.chapter6.utils.AppConstants.USER;
import static com.binarxbca.chapter6.utils.AppConstants.USERNAME;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserIdentityAvailability checkUsernameAvailability(String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@Override
	public UserIdentityAvailability checkEmailAvailability(String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@Override
	public User addUser(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
			throw new BadRequestException(apiResponse);
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
			throw new BadRequestException(apiResponse);
		}

		user.setFirstName(user.getFirstName());
		user.setLastName(user.getLastName());
		user.setUsername(user.getUsername());
		user.setEmail(user.getEmail());
		user.setPassword(user.getPassword());
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public User getUserById(Long id) {
		if (!userRepository.findById(id).isPresent()) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "User ID is not found");
			throw new BadRequestException(apiResponse);
		}

		return userRepository.findById(id).get();
	}

	@Override
	public UserProfile getUserProfile(String username) {
		User user = userRepository.findByUsername(username).get();

		return new UserProfile(user.getId(), user.getFirstName(), user.getLastName(),
				user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
	}

	@Override
	public User updateUser(User user, String username) {
		userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(USER, USERNAME, username));

		user.setFirstName(user.getFirstName());
		user.setLastName(user.getLastName());
		user.setUsername(user.getUsername());
		user.setEmail(user.getEmail());
		user.setPassword(user.getPassword());
		user.setUpdatedAt(LocalDateTime.now());

		return userRepository.save(user);
	}

	@Override
	public ApiResponse deleteUser(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(USER, USERNAME, username));

		userRepository.deleteById(user.getId());

		return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);
	}
}
