package com.mmd.library.controller;

import com.mmd.library.constant.SecurityConstants;
import com.mmd.library.dto.ShelfCurrentLoansResponse;
import com.mmd.library.entity.Checkout;
import com.mmd.library.service.BookService;
import com.mmd.library.util.JWTExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    BookController(BookService bookService){
        this.bookService = bookService;
    }


    @PostMapping("/checkout/{bookId}")
    public ResponseEntity<Checkout> bookCheckout(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken, @PathVariable Long bookId){
        Exception exp;
        try {

            Checkout checkout =  bookService.checkoutBook(JWTExtractor.getUsername(jwtToken), bookId);
            return ResponseEntity.ok(checkout);
        } catch (Exception e) {
            exp = e;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error", exp.getMessage()).body(null);
    }

    @GetMapping("/isCheckedOutByUser/{bookId}")
    public ResponseEntity<Boolean> isBookCheckedOutByUser(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken, @PathVariable long bookId){
        return ResponseEntity.ok().body(bookService.verifyCheckedOutBookByUserEmail(JWTExtractor.getUsername(jwtToken), bookId));
    }

    @GetMapping("/currentCheckedOutCountByUser")
    public ResponseEntity<Integer> currentCheckedOutCountByUser(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken){
        return ResponseEntity.ok(bookService.currentCheckedOutCountByUser(JWTExtractor.getUsername(jwtToken)));
    }

    @GetMapping("/currentLoans")
    public ResponseEntity<List<ShelfCurrentLoansResponse>> currentLoans(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken){
        try{
            return ResponseEntity.ok(bookService.currentLoans(JWTExtractor.getUsername(jwtToken)));
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Exception", ex.getMessage()).body(null);
        }
    }

    @PutMapping("/returnCheckedOutBook/{bookId}")
    public ResponseEntity<String> returnCheckedOutBook(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken, @PathVariable long bookId){
        try{
            bookService.returnBook(JWTExtractor.getUsername(jwtToken), bookId);
            return ResponseEntity.ok("Success");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Exception", ex.getMessage()).body(ex.getMessage());
        }
    }

    @PutMapping("/renewCheckedOutBook/{bookId}")
    public ResponseEntity<String> renewCheckedOutBook(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken, @PathVariable long bookId){
        try{
            bookService.renewLoan(JWTExtractor.getUsername(jwtToken), bookId);
            return ResponseEntity.ok("Success");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Exception", ex.getMessage()).body(ex.getMessage());
        }
    }

}
