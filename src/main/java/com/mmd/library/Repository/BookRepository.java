package com.mmd.library.Repository;

import com.mmd.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    Page<Book> findByCategoryIgnoreCase(@Param("category") String category, Pageable pageable);

    Page<Book> findByCategoryContainingIgnoreCaseAndTitleContainingIgnoreCase(@Param("category") String category,
                                                                              @Param("title") String title,
                                                                              Pageable pageable);

}
