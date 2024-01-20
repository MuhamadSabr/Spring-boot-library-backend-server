package com.mmd.library.entity;

import com.mmd.library.dto.AddBookDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String author;

	private String description;

	private int copies;

	@Column(name = "copies_available")
	private int copiesAvailable;

	private String category;

	@Column(name = "img")
	private String image;

	public Book() {}

	public Book(AddBookDTO addBookDTO){
		this.title = addBookDTO.getTitle();
		this.author = addBookDTO.getAuthor();
		this.description = addBookDTO.getDescription();
		this.copies = addBookDTO.getCopies();
		this.copiesAvailable = addBookDTO.getCopies();
		this.category = addBookDTO.getCategory();
		this.image = addBookDTO.getImage();
	}

}
