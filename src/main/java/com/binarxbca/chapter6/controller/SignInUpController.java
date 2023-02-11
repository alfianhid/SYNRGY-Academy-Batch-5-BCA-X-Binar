package com.binarxbca.chapter6.controller;

import com.binarxbca.chapter6.config.JwtUtils;
import com.binarxbca.chapter6.model.Role;
import com.binarxbca.chapter6.model.User;
import com.binarxbca.chapter6.model.UserDetailsImpl;
import com.binarxbca.chapter6.model.enums.ERoles;
import com.binarxbca.chapter6.model.payload.request.SignInRequest;
import com.binarxbca.chapter6.model.payload.request.SignUpRequest;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.model.payload.response.SignInResponse;
import com.binarxbca.chapter6.model.payload.response.UserIdentityAvailability;
import com.binarxbca.chapter6.repository.RoleRepository;
import com.binarxbca.chapter6.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.binarxbca.chapter6.utils.AppConstants.jwtType;

@RestController
@RequestMapping("/chapter6/public")
public class SignInUpController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(@RequestParam(value = "username") String username) {
        UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(username);

        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(@RequestParam(value = "email") String email) {
        UserIdentityAvailability userIdentityAvailability = userService.checkEmailAvailability(email);
        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    @Operation(summary = "this API endpoint is used for user login")
    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SignInResponse signedUser = new SignInResponse(jwtToken, jwtType, userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles);

        return new ResponseEntity<>(signedUser, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "this API endpoint is used for user registration")
    @PostMapping("/users/signup")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        checkUsernameAvailability(signUpRequest.getUsername());
        checkEmailAvailability(signUpRequest.getEmail());

        String encryptedPass = passwordEncoder.encode(signUpRequest.getPassword());

        User newUser = new User(null, signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getUsername(), signUpRequest.getEmail(), encryptedPass, null);

        Set<Role> strRoles = newUser.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role role = roleRepository.findByName(ERoles.CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found!"));

            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                Role role1 = roleRepository.findByName(ERoles.valueOf(role.toString()))
                        .orElseThrow(() -> new RuntimeException("Error: Role " + role + " is not found!"));

                roles.add(role1);
            });
        }

        newUser.setRoles(roles);
        userService.addUser(newUser);
        ApiResponse apiResponse = new ApiResponse(true, "User registration is successful!");

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "this API endpoint is used for admin registration")
    @PostMapping("/admin/signup")
    public ResponseEntity<ApiResponse> adminSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        checkUsernameAvailability(signUpRequest.getUsername());
        checkEmailAvailability(signUpRequest.getEmail());

        String encryptedPass = passwordEncoder.encode(signUpRequest.getPassword());

        User newUser = new User(null, signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getUsername(), signUpRequest.getEmail(), encryptedPass, null);

        Set<Role> strRoles = newUser.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role role = roleRepository.findByName(ERoles.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found!"));

            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                Role role1 = roleRepository.findByName(ERoles.valueOf(role.toString()))
                        .orElseThrow(() -> new RuntimeException("Error: Role " + role + " is not found!"));

                roles.add(role1);
            });
        }

        newUser.setRoles(roles);
        userService.addUser(newUser);
        ApiResponse apiResponse = new ApiResponse(true, "Admin registration is successful!");

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
