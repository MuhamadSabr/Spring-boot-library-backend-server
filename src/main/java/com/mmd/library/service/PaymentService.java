package com.mmd.library.service;

import com.mmd.library.Repository.PaymentRepository;
import com.mmd.library.dto.PaymentInfo;
import com.mmd.library.entity.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

	private final PaymentRepository paymentRepository;

	public PaymentService(PaymentRepository paymentRepository, @Value("${stripe.key.secret}")String stripeKey){
		this.paymentRepository = paymentRepository;

		Stripe.apiKey = stripeKey;
	}

	public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
		List<String> paymentMethods = new ArrayList<>(List.of("card"));
		Map<String, Object> paymentParameters = new HashMap<>();
		paymentParameters.put("amount", paymentInfo.getAmount());
		paymentParameters.put("currency", paymentInfo.getCurrency());
		paymentParameters.put("payment_method_types", paymentMethods);
		return PaymentIntent.create(paymentParameters);
	}

	@Transactional
	public void payment(String userEmail, PaymentInfo paymentInfo) throws Exception{
		Payment payment = paymentRepository.findByUserEmail(userEmail);
		if(payment==null){
			throw new Exception("Payment information is missing");
		}
		if((Double.valueOf(payment.getAmount()).intValue()*100 )!=paymentInfo.getAmount()){
			throw new Exception("Payment amount does not match the outstanding late-fee");
		}
		paymentRepository.delete(payment);
	}
}
