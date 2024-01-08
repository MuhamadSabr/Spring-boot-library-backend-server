package com.mmd.library.service;

import com.mmd.library.Repository.BookRepository;
import com.mmd.library.Repository.CheckoutRepository;
import com.mmd.library.dto.ShelfCurrentLoansResponse;
import com.mmd.library.entity.Book;
import com.mmd.library.entity.Checkout;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private CheckoutRepository checkoutRepository;
    private BookRepository bookRepository;

    public BookService(CheckoutRepository checkoutRepository, BookRepository bookRepository){
        this.checkoutRepository = checkoutRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Checkout checkoutBook(String userEmail, long bookId) throws Exception{

        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty()){
            throw new Exception("Book with id %d does not exist".formatted(bookId));
        }
        if(book.get().getCopiesAvailable()==0){
            throw  new Exception("No copies of book with id %d, is available".formatted(bookId));
        }

        Checkout checkedOut = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(checkedOut!=null){
            throw new Exception("User %s has book with id %d, already checked out".formatted(userEmail, bookId));
        }

        Checkout newCheckout = new Checkout(userEmail, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(), bookId);

        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        bookRepository.save(book.get());
        newCheckout = checkoutRepository.save(newCheckout);

        return newCheckout;
    }

    public boolean verifyCheckedOutBookByUserEmail(String userEmail, long bookId){
        return checkoutRepository.findByUserEmailAndBookId(userEmail, bookId) != null;
    }

    public int currentCheckedOutCountByUser(String userEmail){
        return checkoutRepository.findByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception{
        List<Checkout> checkoutList = checkoutRepository.findByUserEmail(userEmail);
        if(checkoutList==null){
            throw new Exception("The user does not have any books checkout at this time.");
        }

        List<Long> bookIdList = new ArrayList<>();
        checkoutList.forEach(checkout -> bookIdList.add(checkout.getBookId()));
        List<Book> checkedOutBooks = bookRepository.findBooksByBookIds(bookIdList);

        List<ShelfCurrentLoansResponse> currentLoansResponses = new ArrayList<>();
        for(Book book : checkedOutBooks){
            Optional<Checkout> checkout = checkoutList.stream().filter(checkoutRecord -> checkoutRecord.getBookId()==book.getId()).findFirst();
            checkout.ifPresent(value -> currentLoansResponses.add(new ShelfCurrentLoansResponse(book,
                    (LocalDate.parse(value.getReturnDate()).toEpochDay() - LocalDate.now().toEpochDay()))));
        }
        return currentLoansResponses;
    }

}
