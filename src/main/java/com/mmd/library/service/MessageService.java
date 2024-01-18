package com.mmd.library.service;

import com.mmd.library.Repository.MessageRepository;
import com.mmd.library.entity.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void postMessage(String userEmail, Message message){
        messageRepository.save( new Message(userEmail, message.getTitle(), message.getQuestion()) );
    }
}
