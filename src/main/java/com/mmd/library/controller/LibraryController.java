package com.mmd.library.controller;

import com.mmd.library.entity.Checkout;
import com.mmd.library.service.BookService;
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
    public ResponseEntity<Checkout> bookCheckout(@PathVariable Long bookId){
        Exception exp;
        try {
            Checkout checkout =  bookService.checkoutBook("test", bookId);
            return ResponseEntity.ok(checkout);
        } catch (Exception e) {
            exp = e;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error", exp.getMessage()).body(null);
    }

    @GetMapping("/api/books/isCheckedOutByUser/{bookId}")
    public ResponseEntity<Boolean> isBookCheckedOutByUser(@PathVariable long bookId){
        return ResponseEntity.ok().body(bookService.verifyCheckedOutBookByUserEmail("test", bookId));
    }

    @GetMapping("/api/books/currentCheckedOutCountByUser/{userEmail}")
    public ResponseEntity<Integer> currentCheckedOutCountByUser(@PathVariable String userEmail){
        return ResponseEntity.ok(bookService.currentCheckedOutCountByUser(userEmail));
    }
}
