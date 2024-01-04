package com.mmd.library.controller;

import com.mmd.library.constant.SecurityConstants;
import com.mmd.library.entity.Checkout;
import com.mmd.library.service.BookService;
import com.mmd.library.util.JWTExtractor;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryController {

    private BookService bookService;

    LibraryController(BookService bookService){
        this.bookService = bookService;
    }

    @RequestMapping("/refreshJwtToken")
    public void refreshToken(){
    }

    @PutMapping("/api/books/checkout/{bookId}")
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

    @GetMapping("/api/books/isCheckedOutByUser/{bookId}")
    public ResponseEntity<Boolean> isBookCheckedOutByUser(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken, @PathVariable long bookId){
        return ResponseEntity.ok().body(bookService.verifyCheckedOutBookByUserEmail(JWTExtractor.getUsername(jwtToken), bookId));
    }

    @GetMapping("/api/books/currentCheckedOutCountByUser")
    public ResponseEntity<Integer> currentCheckedOutCountByUser(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken){
        return ResponseEntity.ok(bookService.currentCheckedOutCountByUser(JWTExtractor.getUsername(jwtToken)));
    }
}
