package com.tweetapp.controller;


import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.constants.TweetAppConstants;
import com.tweetapp.model.request.ForgotPasswordRequest;
import com.tweetapp.model.request.LoginRequest;
import com.tweetapp.model.request.RegisterRequest;
import com.tweetapp.model.response.UserResponse;
import com.tweetapp.service.LoginService;

@RestController
public class LoginController {

	private LoginService loginService;
	
	private LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	private static final Logger LOGGER = LogManager.getLogger(LoginController.class);
	
		
	
	@PostMapping("/register")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
		String transactionId = request.getRequestHeader().getTransactionId();
		LOGGER.debug( "Login-log" + ": registeruser - start " + transactionId);
		UserResponse response = new UserResponse();
		try {
			LOGGER.debug("Login-log" + " Request : " + new ObjectMapper().writeValueAsString(request));
			response = loginService.registerUser(request);
		}catch(Exception e) {
			LOGGER.debug("Exception Occurred in "+ "Login-log" + e.getMessage() + transactionId);
		}
		
		LOGGER.debug( "Login-Log" + ": registeruser - end " + transactionId);
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private ResponseEntity<UserResponse> loginUser(@Valid @RequestBody LoginRequest request) {
		String transactionId = request.getRequestHeader().getTransactionId();
		LOGGER.debug( "Login-log" + ": loginUser - start " + transactionId);
		UserResponse response = new UserResponse();
		try {
			LOGGER.debug("Login-log" + " Request : " + new ObjectMapper().writeValueAsString(request));
			response = loginService.loginUser(request);
		}catch(Exception e) {
			LOGGER.debug("Exception Occurred in "+ "Login-log" + e.getMessage() + transactionId);
		}
		
		LOGGER.debug( "Login-log" + ": loginUser - end " + transactionId);
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/{username}/forgot")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private ResponseEntity<UserResponse> forgotPassword(@PathVariable String username, @Valid @RequestBody ForgotPasswordRequest request) {
		String transactionId = request.getRequestHeader().getTransactionId();
		LOGGER.debug( "Login-log" + ": forgotPassword - start " + transactionId);
		UserResponse response = new UserResponse();
		try {
			LOGGER.debug("Login-log" + " Request : " + new ObjectMapper().writeValueAsString(request));
			response = loginService.forgotPassword(request,username);
		}catch(Exception e) {
			LOGGER.debug("Exception Occurred in "+ "Login-log" + e.getMessage() + transactionId);
		}
		
		LOGGER.debug( "Login-log" + ": forgotPassword - end " + transactionId);
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}
	

	
	@GetMapping("/users/all")
	private ResponseEntity<UserResponse> getAllUsers(@RequestHeader("transactionId") String transactionId) {
		LOGGER.debug( "Login-log" + ": getAllUsers - start " + transactionId);
		UserResponse response = new UserResponse();
		try {
			 response = loginService.getAllUsers();
		}catch(Exception e) {
			LOGGER.debug("Exception Occurred in "+ "Login-log" + e.getMessage() + transactionId);
		}
		
		LOGGER.debug( "Login-log" + ": getAllUsers - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}

	@GetMapping("/user/search/{username}")
	private ResponseEntity<UserResponse> searchUserByLoginId(@PathVariable(name = "username") String username,@RequestHeader("transactionId") String transactionId) {
		LOGGER.debug( "Login-log" + ": searchUserByLoginId - start " + transactionId);
		UserResponse response = new UserResponse();
		try {
			LOGGER.debug("Login-log" + " Username from path : " + username);
			 response = loginService.searchByUserName(username);
		}catch(Exception e) {
			LOGGER.debug("Exception Occurred in "+ "Login-log" + e.getMessage() + transactionId);
		}
		
		LOGGER.debug( "Login-log" + ": searchUserByLoginId - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
}
