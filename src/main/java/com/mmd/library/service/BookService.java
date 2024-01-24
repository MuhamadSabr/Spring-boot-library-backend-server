package com.mmd.library.service;

import com.mmd.library.Repository.BookRepository;
import com.mmd.library.Repository.CheckoutRepository;
import com.mmd.library.Repository.HistoryRepository;
import com.mmd.library.Repository.PaymentRepository;
import com.mmd.library.dto.ShelfCurrentLoansResponse;
import com.mmd.library.entity.Book;
import com.mmd.library.entity.Checkout;
import com.mmd.library.entity.History;
import com.mmd.library.entity.Payment;
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
    private HistoryRepository historyRepository;
    private PaymentRepository paymentRepository;

    public BookService(CheckoutRepository checkoutRepository, BookRepository bookRepository, HistoryRepository historyRepository, PaymentRepository paymentRepository){
        this.checkoutRepository = checkoutRepository;
        this.bookRepository = bookRepository;
        this.historyRepository = historyRepository;
        this.paymentRepository = paymentRepository;
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

        List<Checkout> alreadyCheckedOutBooks = checkoutRepository.findByUserEmail(userEmail);
        for(Checkout ch : alreadyCheckedOutBooks){
            if((LocalDate.parse(ch.getReturnDate()).toEpochDay()) - (LocalDate.now().toEpochDay()) <0) {
                throw new Exception("User: " + userEmail + " has Over-due books, User has to return book and pay late fees to be able to checkout new books");
            }
        }

        Payment payment = paymentRepository.findByUserEmail(userEmail);
        if(payment!=null){
            throw new Exception("User: " + userEmail + " has to pay outstanding late-fees to be able to checkout new books");
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

    @Transactional
    public void returnBook(String userEmail, long bookId) throws Exception{
        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty()){
            throw new Exception("Book with id %d does not exist".formatted(bookId));
        }

        Checkout checkedOut = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(checkedOut==null){
            throw new Exception("User %s does not have book with id %d checked out".formatted(userEmail, bookId));
        }

        long daysLeft = (LocalDate.parse(checkedOut.getReturnDate()).toEpochDay()) - (LocalDate.now().toEpochDay());
        if(daysLeft<0) {
            Payment payment = new Payment(userEmail, (daysLeft*-1)*5);
            paymentRepository.save(payment);
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable()+1);
        bookRepository.save(book.get());
        checkoutRepository.delete(checkedOut);
        History history = new History(checkedOut.getUserEmail(), checkedOut.getCheckoutDate(), LocalDate.now().toString(),
                book.get().getTitle(), book.get().getAuthor(), book.get().getDescription(), book.get().getImage());
        historyRepository.save(history);
    }

    @Transactional
    public void renewLoan(String userEmail, long bookId) throws Exception{
        Checkout checkedOut = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(checkedOut==null){
            throw new Exception("Either the book does not exist or, User %s does not have book with id %d checked out".formatted(userEmail, bookId));
        }
        if((LocalDate.parse(checkedOut.getReturnDate()).toEpochDay()) - (LocalDate.now().toEpochDay()) >0) {
            checkedOut.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(checkedOut);
        }else{
            throw new Exception("Over-due books cannot be renewed");
        }
    }

}
