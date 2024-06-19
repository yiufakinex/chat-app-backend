package com.franklin.backend.controller;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.franklin.backend.annotation.GetUser;
import com.franklin.backend.entity.Message;
import com.franklin.backend.entity.User;
import com.franklin.backend.form.AddGroupChatUserForm;
import com.franklin.backend.form.NewGroupChatForm;
import com.franklin.backend.form.RenameGroupChatForm;
import com.franklin.backend.service.GroupChatService;
import com.franklin.backend.service.UserService;
import com.franklin.backend.util.Response;

@Controller
@RequestMapping("/api/groupchat")
public class GroupChatController {

    private final GroupChatService groupChatService;
    private final UserService userService;

    public GroupChatController(GroupChatService groupChatService, UserService userService) {
        this.groupChatService = groupChatService;
        this.userService = userService;
    }

    @PostMapping(path = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> newGroupChat(@GetUser User user,
            @RequestBody NewGroupChatForm newGroupChatForm) {
        return new ResponseEntity<>(
                Response.createBody("groupChat", groupChatService.newGroupChat(user, newGroupChatForm)), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getGroupChats(@GetUser User user) {
        return new ResponseEntity<>(Response.createBody("groupChats", groupChatService.getGroupChats(user)),
                HttpStatus.OK);
    }

    @MessageMapping("/update/{id}/users/add")
    @SendTo("/topic/groupchat/{id}")
    public Message addUser(@DestinationVariable(value = "id") Long id, Principal principal,
            @Payload AddGroupChatUserForm form) {
        User user = userService.getUserFromPrincipal(principal);
        return groupChatService.addUser(user, form, id);
    }

    @MessageMapping("/update/{id}/users/remove")
    @SendTo("/topic/groupchat/{id}")
    public Message removeUser(@DestinationVariable(value = "id") Long id, Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        return groupChatService.removeUser(user, id);
    }

    @MessageMapping("/update/{id}/rename")
    @SendTo("/topic/groupchat/{id}")
    public Message renameGroupChat(@DestinationVariable(value = "id") Long id, Principal principal,
            @Payload RenameGroupChatForm form) {
        User user = userService.getUserFromPrincipal(principal);
        return groupChatService.renameGroupChat(user, form, id);
    }
}