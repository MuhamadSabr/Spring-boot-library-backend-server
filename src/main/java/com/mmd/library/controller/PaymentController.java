package com.mmd.library.controller;

import com.mmd.library.constant.SecurityConstants;
import com.mmd.library.dto.PaymentInfo;
import com.mmd.library.service.PaymentService;
import com.mmd.library.util.JWTExtractor;
import com.stripe.model.PaymentIntent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService){
		this.paymentService = paymentService;
	}

	@PostMapping("/payment-intent")
	public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo){
		try{
			PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfo);
			String paymentStr = paymentIntent.toJson();
			return new ResponseEntity<>(paymentStr, HttpStatus.OK);
		}
		catch (Exception exp){
			System.out.println("exp" + exp.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp.getMessage());
		}
	}

	@PutMapping("/complete-payment")
	public ResponseEntity<String> paymentComplete(@RequestHeader(name = SecurityConstants.JWT_HEADER)String jwtToken, @RequestBody PaymentInfo paymentInfo){
		try{
			paymentService.payment(JWTExtractor.getUsername(jwtToken), paymentInfo);
			return ResponseEntity.ok().build();
		}
		catch (Exception exp){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMessage());
		}
	}

}
