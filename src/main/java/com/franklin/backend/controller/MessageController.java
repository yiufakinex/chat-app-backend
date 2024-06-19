package com.franklin.backend.controller;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.franklin.backend.annotation.GetUser;
import com.franklin.backend.entity.Message;
import com.franklin.backend.entity.User;
import com.franklin.backend.form.NewMessageForm;
import com.franklin.backend.form.PaginationForm;
import com.franklin.backend.service.MessageService;
import com.franklin.backend.service.UserService;
import com.franklin.backend.util.Response;

@Controller
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @MessageMapping("/send/{id}")
    @SendTo("/topic/groupchat/{id}")
    public Message sendMessage(@DestinationVariable(value = "id") Long id, Principal principal,
            @Payload NewMessageForm newMessageForm) {
        User user = userService.getUserFromPrincipal(principal);
        return messageService.sendMessage(id, newMessageForm.getContent(), user);
    }

    @GetMapping(path = "/{id}/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getMessages(@GetUser User user,
            @PathVariable("id") Long id,
            PaginationForm paginationForm,
            @RequestParam("before") Long before) {
        return Response.page(messageService.getMessages(user, id, paginationForm, before));
    }

}