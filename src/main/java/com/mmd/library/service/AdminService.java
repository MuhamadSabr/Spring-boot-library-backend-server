package com.mmd.library.service;

import com.mmd.library.Repository.BookRepository;
import com.mmd.library.Repository.CheckoutRepository;
import com.mmd.library.Repository.ReviewRepository;
import com.mmd.library.dto.AddBookDTO;
import com.mmd.library.entity.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminService {

	private final BookRepository bookRepository;
	private final CheckoutRepository checkoutRepository;
	private final ReviewRepository reviewRepository;

	public AdminService(BookRepository bookRepository, CheckoutRepository checkoutRepository, ReviewRepository reviewRepository) {
		this.bookRepository = bookRepository;
		this.checkoutRepository = checkoutRepository;
		this.reviewRepository = reviewRepository;
	}

	@Transactional
	public void addBook(AddBookDTO addBook){
		Book newBook = new Book(addBook);
		bookRepository.save(newBook);
	}

	@Transactional
	public void updateBookStatus(Book book) throws Exception{
		if(bookRepository.findById(book.getId()).isEmpty()){
			throw new Exception("No book with id " + book.getId() + " exists");
		}
		bookRepository.save(book);
	}

	@Transactional
	public void deleteBookById(long bookId) throws Exception{
		Optional<Book> book = bookRepository.findById(bookId);
		if(book.isEmpty()){
			throw new Exception("No book with id " + bookId + " exists");
		}
		checkoutRepository.deleteAllByBookId(bookId);
		reviewRepository.deleteAllByBookId(bookId);
		bookRepository.delete(book.get());
	}
}
