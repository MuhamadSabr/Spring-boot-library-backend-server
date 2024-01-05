package com.mmd.library.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class ReviewDTO {

    private double rating;

    private long bookId;

    private Optional<String> description;
}
