package com.mmd.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterNewUser {

    private String username;

    private String password;

}
