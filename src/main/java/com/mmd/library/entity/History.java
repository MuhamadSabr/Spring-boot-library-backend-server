package com.mmd.library.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "history")
@Data
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "checkout_date")
	private String checkedOutDate;

	@Column(name = "returned_date")
	private String returnedDate;

	private String title;

	private String author;

	private String description;

	@Column(name = "img")
	private String image;

	public History() {}

	public History(String userEmail, String checkedOutDate, String returnedDate, String title, String author, String description, String image) {
		this.userEmail = userEmail;
		this.checkedOutDate = checkedOutDate;
		this.returnedDate = returnedDate;
		this.title = title;
		this.author = author;
		this.description = description;
		this.image = image;
	}
}
