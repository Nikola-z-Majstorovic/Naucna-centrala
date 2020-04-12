package org.camunda.bpmn.controller;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpmn.dto.UserInfoDTO;
import org.camunda.bpmn.model.Admin;
import org.camunda.bpmn.model.Author;
import org.camunda.bpmn.model.Editor;
import org.camunda.bpmn.model.Reviewer;
import org.camunda.bpmn.model.User;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.UserService;
import org.camunda.bpmn.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "users")
public class UserController {
	
    @Autowired
    public TokenUtils tokenUtils;
    
    @Autowired
    UserService userService;
    
    @RequestMapping(value = "/get-user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDTO> getUser(HttpServletRequest request){
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        UserInfoDTO ui = new UserInfoDTO();
        if(username != "" && username != null) {
            User u = (User) userService.findOneByUsername(username);
            if(u instanceof Admin){
                u = (Admin) u;
                ui.setRole("ADMIN");
            }else if(u instanceof Reviewer){
                u = (Reviewer) u;
                ui.setRole("REVIEWER");
            }else if(u instanceof Editor){
                u = (Editor) u;
                ui.setRole("EDITOR");
            }else if(u instanceof  Author){
                u = (Author) u;
                ui.setRole("AUTHOR");
            }
            ui.setUsername(u.getUsername());
            return new ResponseEntity<UserInfoDTO>(ui, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
