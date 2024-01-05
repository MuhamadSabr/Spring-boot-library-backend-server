package com.mmd.library.service;

import com.mmd.library.Repository.BookRepository;
import com.mmd.library.Repository.ReviewRepository;
import com.mmd.library.dto.ReviewDTO;
import com.mmd.library.entity.Book;
import com.mmd.library.entity.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository){
        this.reviewRepository = reviewRepository;
        this.bookRepository   = bookRepository;
    }

    @Transactional
    public void addReview(String userEmail, ReviewDTO reviewDTO) throws Exception{

        Optional<Book> book = bookRepository.findById(reviewDTO.getBookId());
        if(book.isEmpty()){
            throw new Exception("Book with id %d does not exist".formatted(reviewDTO.getBookId()));
        }

        Review review = reviewRepository.findByUserEmailAndBookId(userEmail, reviewDTO.getBookId());
        if(review!=null){
            throw new Exception("User %s has already added a review to book with id %d".formatted(userEmail, reviewDTO.getBookId()));
        }


        Review newReview = new Review(userEmail, LocalDate.now(), reviewDTO.getRating(), reviewDTO.getBookId(),
                reviewDTO.getDescription()!=null ? reviewDTO.getDescription().get() : "");
        newReview = reviewRepository.save(newReview);
        if(newReview==null){
            throw new Exception("Could not save the review");
        }
    }

    public boolean hasUserLeftReview(String userEmail, long bookId){
        return reviewRepository.findByUserEmailAndBookId(userEmail, bookId) != null;
    }

}
