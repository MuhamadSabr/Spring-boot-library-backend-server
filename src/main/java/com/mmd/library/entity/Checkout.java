package com.mmd.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "checkout")
@Data
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "checkout_date")
    private String checkoutDate;

    @Column(name = "return_date")
    private String returnDate;

    @Column(name = "book_id")
    private long bookId;

    public Checkout() {}

}
