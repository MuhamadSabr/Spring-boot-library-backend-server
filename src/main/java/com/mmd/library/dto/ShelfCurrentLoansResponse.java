package com.mmd.library.dto;

import com.mmd.library.entity.Book;
import lombok.Data;

@Data
public class ShelfCurrentLoansResponse {

    private Book book;
    private long daysLeft;

    public ShelfCurrentLoansResponse(Book book, long daysLeft) {
        this.book = book;
        this.daysLeft = daysLeft;
    }

}
