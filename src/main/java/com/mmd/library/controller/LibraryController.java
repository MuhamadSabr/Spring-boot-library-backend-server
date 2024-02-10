package com.mmd.library.controller;

import com.mmd.library.constant.SecurityConstants;
import com.mmd.library.dto.RegisterNewUser;
import com.mmd.library.entity.Checkout;
import com.mmd.library.exception.UserConfirmationException;
import com.mmd.library.exception.UserExistsException;
import com.mmd.library.service.BookService;
import com.mmd.library.service.UserService;
import com.mmd.library.util.JWTExtractor;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryController {

    private UserService userService;

    public LibraryController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/refreshJwtToken")
    public void refreshToken(){
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerNewUser(@RequestBody RegisterNewUser registerNewUser) {
        try {
            userService.createNewUser(registerNewUser);
        } catch (UserExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/verify-email/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token){
        try{
            userService.verifyEmail(token);
            return ResponseEntity.ok().body("""
                    The email was successfully verified""");
        } catch (UserConfirmationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("""
                    There is no confirmation for the specified user""");
        }

    }

}
