package com.mmd.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "user_email")
	private String userEmail;

	private double amount;

	public Payment() {}

	public Payment(String userEmail, double amount) {
		this.userEmail = userEmail;
		this.amount = amount;
	}
}
