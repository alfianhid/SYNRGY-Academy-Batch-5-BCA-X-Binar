package com.binarxbca.chapter6.controller;

import com.binarxbca.chapter6.model.Film;
import com.binarxbca.chapter6.model.User;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.PagedResponse;
import com.binarxbca.chapter6.model.payload.response.UserIdentityAvailability;
import com.binarxbca.chapter6.model.payload.response.UserProfile;
import com.binarxbca.chapter6.service.FilmService;
import com.binarxbca.chapter6.service.UserService;
import com.binarxbca.chapter6.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/chapter6/auth/users")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private FilmService filmService;

	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/{username}/profile")
	public ResponseEntity<UserProfile> getUserProfile(@PathVariable(value = "username") String username) {
		UserProfile userProfile = userService.getUserProfile(username);

		return new ResponseEntity<>(userProfile, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<List<User>> getAllUser() {
		List<User> listOfUser = userService.getAllUser();

		return new ResponseEntity<>(listOfUser, HttpStatus.OK);
	}

	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/update/{username}")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user,
			@PathVariable(value = "username") String username) {
		User updatedUser = userService.updateUser(user, username);

		return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
	}

	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/delete/{username}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "username") String username) {
		ApiResponse apiResponse = userService.deleteUser(username);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('CUSTOMER')")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/{username}/films")
	public ResponseEntity<PagedResponse<Film>> getFilmList(@PathVariable(value = "id") Long id,
																 @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
																 @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<Film> response = filmService.getByUserId(id, page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
