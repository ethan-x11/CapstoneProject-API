package com.capstone.qwikpay.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.qwikpay.entities.Role;
import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.enums.ERole;
import com.capstone.qwikpay.repositories.RoleRepository;
import com.capstone.qwikpay.repositories.UserRepository;
import com.capstone.qwikpay.security.jwt.JwtUtils;
import com.capstone.qwikpay.security.payload.request.LoginRequest;
import com.capstone.qwikpay.security.payload.request.SignUpRequest;
import com.capstone.qwikpay.security.payload.response.JwtResponse;
import com.capstone.qwikpay.security.payload.response.MessageResponse;
import com.capstone.qwikpay.services.UserDetailsImpl;

import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;
	
/**	@PostMapping("/signin") 
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), 
						loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		//String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new MessageResponse("User with username "+userDetails.getUsername()+" Logged in Successfully "));
	}**/
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), 
							loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt, 
													 userDetails.getId(), 
													 userDetails.getUsername(), 
													 userDetails.getEmail(), 
													 roles));
		} catch (Exception e) {
			return ResponseEntity.status(401).body(new MessageResponse("Error: Invalid credentials"));
		}
	}
	 

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		
		if(userRepository.existsByMobile(signUpRequest.getMobile())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Mobile is already in user!"));
		}
		
		// Create new user's account
		UserEntity user = new UserEntity(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		
		//Add roles and other properties
		user.setRoles(getRoles(signUpRequest));
		user.setAddress(signUpRequest.getAddress());
		user.setEmail(signUpRequest.getEmail());
		user.setMobile(signUpRequest.getMobile());
		user.setGender(signUpRequest.getGender());
		// saving UserEntity to the database
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

	}
	
	//Get Roles from DB if not present in SignupRequest
	public Set<Role> getRoles(SignUpRequest signupRequest){
		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				/*
				 * case "child": Role childRole = roleRepository.findByName(ERole.ROLE_CHILD)
				 * .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				 * roles.add(childRole);
				 * 
				 * break; case "parent": Role parentRole =
				 * roleRepository.findByName(ERole.ROLE_PARENT) .orElseThrow(() -> new
				 * RuntimeException("Error: Role is not found.")); roles.add(parentRole);
				 * 
				 * break;
				 */
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		return roles;
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> passwordDetails) {
	    String email = passwordDetails.get("email");
	    String newPassword = passwordDetails.get("password");
	    String confirmPassword = passwordDetails.get("confirmPassword");

	    // Check if the email exists in the database
	    UserEntity user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Error: User not found with email: " + email));

	    // Check if password and confirmPassword match
	    if (!newPassword.equals(confirmPassword)) {
	        return ResponseEntity.badRequest().body(new MessageResponse("Error: Password and Confirm Password do not match!"));
	    }

	    // Update the user's password
	    user.setPassword(encoder.encode(newPassword));
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
	}

}
