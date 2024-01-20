package com.mmd.library.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class AddBookDTO {

	private String title;

	private String author;

	private String description;

	private int copies;

	private String category;

	private String image;

}
