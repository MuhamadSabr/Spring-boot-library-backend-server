package com.mmd.library.controller;

import com.mmd.library.constant.SecurityConstants;
import com.mmd.library.dto.AddBookDTO;
import com.mmd.library.dto.AdminMessageResponse;
import com.mmd.library.service.AdminService;
import com.mmd.library.service.MessageService;
import com.mmd.library.util.JWTExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final MessageService messageService;
	private final AdminService adminService;

	public AdminController(MessageService messageService, AdminService adminService) {
		this.messageService = messageService;
		this.adminService = adminService;
	}

	@PutMapping("/responseMessage")
	public ResponseEntity<String> updateMessageResponse(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken,
														@RequestBody AdminMessageResponse adminMessageResponse){
		try{
			messageService.updateMessageResponse(adminMessageResponse, JWTExtractor.getUsername(jwtToken));
			return ResponseEntity.ok().build();
		}
		catch (Exception exp){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMessage());
		}
	}

	@PostMapping("/addBook")
	public ResponseEntity<Void> addBook(@RequestBody AddBookDTO addBook){
		adminService.addBook(addBook);
		return ResponseEntity.ok().build();
	}

}
