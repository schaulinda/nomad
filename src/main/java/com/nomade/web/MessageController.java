package com.nomade.web;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nomade.domain.Message;
import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.MessageService;
import com.nomade.service.RelationService;
import com.nomade.service.UserService;

@RequestMapping("/message")
@Controller
public class MessageController {
	
	@Autowired
	Security security;
	@Autowired
	UserService userService;
	@Autowired
	MessageService messageService;
	@Autowired
	RelationService relationService;

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping("/send")
    @ResponseBody
    public String sendMsg(@RequestParam("content") String content,
    		@RequestParam("idReceiver") String idReceiver, HttpServletRequest request) {
       
    	UserNomade nomade = security.getUserNomade();
    	
    	System.out.print("content: "+content);
    	Message message = new Message();
    	
    	message.setContent(content);
    	message.setRead(false);
    	message.setDateSend(new Date());
    	//message.setReceiver(userService.findUserNomade(new BigInteger(idReceiver)));
    	messageService.saveMessage(message);
    	
    	return "envoyer";
    }
    
    @RequestMapping("/listMsg")
    public String listMsg(HttpServletRequest request,Model uiModel) {
       
    	UserNomade nomade = security.getUserNomade();
    	
    	
    	
    	
    	return "message/listMsg";
    }
    
    @RequestMapping("/read")
    public String read(@RequestParam("idSender") String idSender, HttpServletRequest request,
    		Model uiModel) {
       
    	UserNomade nomade = security.getUserNomade();
    	
 
    	
    	return "message/listMsg";
    }
}
