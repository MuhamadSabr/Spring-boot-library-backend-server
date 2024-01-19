package com.mmd.library.controller;

import com.mmd.library.constant.SecurityConstants;
import com.mmd.library.dto.AdminMessageResponse;
import com.mmd.library.entity.Message;
import com.mmd.library.service.MessageService;
import com.mmd.library.util.JWTExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/postMessage")
    public ResponseEntity<Void> postMessage(@RequestHeader(name = SecurityConstants.JWT_HEADER) String jwtToken, @RequestBody Message message){
        messageService.postMessage(JWTExtractor.getUsername(jwtToken), message);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin/responseMessage")
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
}
