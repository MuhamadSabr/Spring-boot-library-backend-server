package com.mmd.library.Repository;

import com.mmd.library.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBookIdOrderByReviewDateDesc(@Param("bookId") Long bookId, Pageable pageable);

    Review findByUserEmailAndBookId(String userEmail, long bookId);

    @Modifying
    @Query("DELETE FROM Review c WHERE c.bookId in :bookId")
    void deleteAllByBookId(@Param("bookId") long bookId);
}
