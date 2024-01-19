package com.mmd.library.service;

import com.mmd.library.Repository.MessageRepository;
import com.mmd.library.dto.AdminMessageResponse;
import com.mmd.library.entity.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional
    public void updateMessageResponse(AdminMessageResponse adminMessageResponse, String adminEmail) throws Exception{
        Optional<Message> message = messageRepository.findById(adminMessageResponse.getId());
        if(message.isEmpty()){
            throw new Exception("No message with message Id of " + adminMessageResponse.getId() + " exists");
        }
        if(message.get().isClosed()){
            throw new Exception("Message has already been responded to");
        }
        message.get().setAdminEmail(adminEmail);
        message.get().setResponse(adminMessageResponse.getResponse());
        message.get().setClosed(true);
        messageRepository.save(message.get());
    }
}
