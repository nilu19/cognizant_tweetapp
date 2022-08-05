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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.tweetapp.constants.TweetAppConstants;
import com.tweetapp.model.request.LikeRequest;
import com.tweetapp.model.request.ReplyRequest;
import com.tweetapp.model.request.TweetRequest;
import com.tweetapp.model.response.ReplyResponse;
import com.tweetapp.model.response.TweetResponse;
import com.tweetapp.service.TweetService;

@RestController
public class TweetController {

	private static final Logger LOGGER = LogManager.getLogger(TweetController.class);

	

	public TweetController(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	private TweetService tweetService;

	@GetMapping("/all")
	private ResponseEntity<TweetResponse> getAllTweets(@RequestHeader("transactionId") String transactionId) {
		LOGGER.debug("TweetController-log" + ": getAllTweets - start " + transactionId);
		TweetResponse response = new TweetResponse();
		try {
			response = tweetService.getAllTweets();
		} catch (Exception e) {
			LOGGER.debug("Exception Occurred in " + "TweetController-log" + e.getMessage() + transactionId);
		}

		LOGGER.debug("TweetController-log" + ": getAllTweets - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/{username}")
	private ResponseEntity<TweetResponse> getAllTweetsForUser(@PathVariable(name = "username") String username,@RequestHeader("transactionId") String transactionId) {
		LOGGER.debug("TweetController-log" + ": getAllTweets - start " + transactionId);
		TweetResponse response = new TweetResponse();
		try {
			response = tweetService.getTweetsForUser(username);
		} catch (Exception e) {
			LOGGER.debug("Exception Occurred in " + "TweetController-log" + e.getMessage() + transactionId);
		}

		LOGGER.debug("TweetController-log" + ": getAllTweets - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/{username}/add")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private ResponseEntity<TweetResponse> postTweet(@PathVariable(name = "username") String username, @Valid @RequestBody TweetRequest request) {
		String transactionId = request.getRequestHeader().getTransactionId();
		LOGGER.debug("TweetController-log" + ": postTweet - start " + transactionId);
		TweetResponse response = new TweetResponse();
		try {
			response = tweetService.postTweet(request);
		} catch (Exception e) {
			LOGGER.debug("Exception Occurred in " + "TweetController-log" + e.getMessage() + transactionId);
		}

		LOGGER.debug("TweetController-log" + ": postTweet - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}

	@PutMapping("/{username}/update/{id}")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private ResponseEntity<TweetResponse> updateTweet(@PathVariable(name = "username") String username, @PathVariable(name = "id") String tweetId,
			@Valid @RequestBody TweetRequest request) {
		String transactionId = request.getRequestHeader().getTransactionId();
		LOGGER.debug("TweetController-log" + ": updateTweet - start " + transactionId);
		TweetResponse response = new TweetResponse();
		try {
			response = tweetService.updateTweet(request,tweetId);
		} catch (Exception e) {
			LOGGER.debug("Exception Occurred in " + "TweetController-log" + e.getMessage() + transactionId);
		}

		LOGGER.debug("TweetController-log" + ": updateTweet - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@DeleteMapping("/{username}/delete/{id}")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private ResponseEntity<Object> deleteTweet(@PathVariable(name = "username") String username, @PathVariable(name = "id") String tweetId,
			@RequestHeader("transactionId") String transactionId) {
		LOGGER.debug("TweetController-log" + ": deleteTweet - start " + transactionId);
		TweetResponse response = new TweetResponse();
		try {
			response = tweetService.deleteTweet(tweetId);
		} catch (Exception e) {
			LOGGER.debug("Exception Occurred in " + "TweetController-log" + e.getMessage() + transactionId);
		}

		LOGGER.debug("TweetController-log" + ": deleteTweet - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
	}

	@PostMapping("/{username}/like/{id}")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private ResponseEntity<Object> likeTweet(@PathVariable("id") String tweetId,@Valid @RequestBody LikeRequest request) {
		String transactionId = request.getRequestHeader().getTransactionId();
		LOGGER.debug("TweetController-log" + ": likeTweet - start " + transactionId);
		TweetResponse response = new TweetResponse();
		try {
			if(request.getLikeFlag().equalsIgnoreCase("Y")){
				response = tweetService.likeTweet(request);
			}else {
				response = tweetService.unlikeTweet(request);
			}
			
		} catch (Exception e) {
			LOGGER.debug("Exception Occurred in " + "TweetController-log" + e.getMessage() + transactionId);
		}

		LOGGER.debug("TweetController-log" + ": likeTweet - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}

	@PostMapping("/{username}/reply/{id}")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private ResponseEntity<Object> replyTweet(@PathVariable("id") String tweetId,@Valid @RequestBody ReplyRequest request) {
		String transactionId = request.getRequestHeader().getTransactionId();
		LOGGER.debug("TweetController-log" + ": replyTweet - start " + transactionId);
		ReplyResponse response = new ReplyResponse();
		try {
			response = tweetService.replyTweet(request);
			
		} catch (Exception e) {
			LOGGER.debug("Exception Occurred in " + "TweetController-log" + e.getMessage() + transactionId);
		}

		LOGGER.debug("TweetController-log" + ": replyTweet - end " + transactionId);
		response.getResponseHeader().getTransactionNotification().setTransactionId(transactionId);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}

}
