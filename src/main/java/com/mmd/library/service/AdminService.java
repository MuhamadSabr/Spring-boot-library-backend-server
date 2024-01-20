package com.mmd.library.service;

import com.mmd.library.Repository.BookRepository;
import com.mmd.library.dto.AddBookDTO;
import com.mmd.library.entity.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

	private final BookRepository bookRepository;

	public AdminService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Transactional
	public void addBook(AddBookDTO addBook){
		Book newBook = new Book(addBook);
		bookRepository.save(newBook);
	}
}
