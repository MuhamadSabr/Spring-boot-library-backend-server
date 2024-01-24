package com.mmd.library.dto;

import lombok.Data;

@Data
public class PaymentInfo {
	private String currency;
	private int amount;
	private String receiptEmail;

	@Override
	public String toString() {
		return "PaymentInfo{" +
				"currency='" + currency + '\'' +
				", amount=" + amount +
				", receiptEmail='" + receiptEmail + '\'' +
				'}';
	}
}
