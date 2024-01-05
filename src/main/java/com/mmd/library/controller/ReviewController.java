package com.mmd.library.controller;

import com.mmd.library.Repository.ReviewRepository;
import com.mmd.library.constant.SecurityConstants;
import com.mmd.library.dto.ReviewDTO;
import com.mmd.library.service.ReviewService;
import com.mmd.library.util.JWTExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken, @RequestBody ReviewDTO newReview){
        try{
            reviewService.addReview(JWTExtractor.getUsername(jwtToken), newReview);
            return ResponseEntity.ok("Successful operation");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/hasUserLeftReview/{bookId}")
    public ResponseEntity<Boolean> hasUserLeftAReviewOnBookId(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken, @PathVariable long bookId){
        return ResponseEntity.ok(reviewService.hasUserLeftReview(JWTExtractor.getUsername(jwtToken), bookId));
    }
}
