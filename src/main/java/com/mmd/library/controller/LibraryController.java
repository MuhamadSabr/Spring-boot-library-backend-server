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

    @RequestMapping("/refreshJwtToken")
    public void refreshToken(){
    }

}
