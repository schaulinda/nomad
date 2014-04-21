package com.nomade.web;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nomade.domain.FileMsg;
import com.nomade.domain.Message;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.MessageService;
import com.nomade.service.UserService;

@RequestMapping("/filemsgs")
@Controller
@RooWebScaffold(path = "filemsgs", formBackingObject = FileMsg.class)
public class FileMsgController {
	
	@Autowired
	Security security;
	@Autowired
	UserService userService;
	@Autowired
	MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping("/sendFromProfile")
    @ResponseBody
    public String sendMsg(@RequestParam("content") String content,
    		@RequestParam("idReceiver") String idReceiver, HttpServletRequest request) {
       
    	send(content, idReceiver);
    	
    	return "envoyer";
    }
    
    @RequestMapping("/send")
    @ResponseBody
    public String sendMsg2(@RequestParam("content") String content,
    		@RequestParam("sender") String sender, HttpServletRequest request, Model uiModel) {
       
    	send(content, sender);
    	
    	UserNomade nomade = security.getUserNomade();
    	UserNomade nomad2 = userService.findUserNomade(new BigInteger(sender));
    	
    	List<FileMsg> myFileMsg = fileMsgService.findMyFileMsg(nomade);
    	uiModel.addAttribute("listNomad", myFileMsg);
    	
    	try {
			List<FileMsg> meAndOther = fileMsgService.findByMeAndOther(nomade, nomad2);
			FileMsg fileMsg = meAndOther.get(0);
			fileMsg.setNumberUnreadMsg(0);
			fileMsgService.saveFileMsg(fileMsg);
			uiModel.addAttribute("msg", fileMsg.getMessages());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    	uiModel.addAttribute("nomade", nomade);
    	return "message/listMsg";
    }

	private void send(String content, String idReceiver) {
		UserNomade nomad1 = security.getUserNomade();
    	UserNomade nomad2 = userService.findUserNomade(new BigInteger(idReceiver));
    	
    	System.out.print("content: "+content);
    	Message message = new Message(); 	
    	message.setContent(content);
    	message.setRead(false);
    	message.setDateSend(new Date());
    	
    	List<FileMsg> listFileMsg = fileMsgService.findByMeAndOther(nomad1, nomad2);
    	if(listFileMsg!=null && listFileMsg.size()>0){
    		
    		listFileMsg.get(0).getMessages().add(message);
    	}else{
    		
    		FileMsg fileMsg = new FileMsg();
    		fileMsg.setNomad1(nomad1);
    		fileMsg.setNomad2(nomad2);
    		fileMsg.setNumberUnreadMsg(fileMsg.getNumberUnreadMsg()+1);
    		fileMsgService.saveFileMsg(fileMsg);
    		
    	}
	}
    
    @RequestMapping("/listMsg")
    public String listMsg(HttpServletRequest request,Model uiModel) {
       
    	UserNomade nomade = security.getUserNomade();
    	
    	List<FileMsg> myFileMsg = fileMsgService.findMyFileMsg(nomade);
    	
    	uiModel.addAttribute("listNomad", myFileMsg);
    	uiModel.addAttribute("nomade", nomade);
    	return "message/listMsg";
    }
    
    @RequestMapping("/read")
    public String read(@RequestParam("idNomad") String idNomad, HttpServletRequest request,
    		Model uiModel) {
       
    	UserNomade nomade = security.getUserNomade();
    	UserNomade nomad2 = userService.findUserNomade(new BigInteger(idNomad));
    	
    	List<FileMsg> myFileMsg = fileMsgService.findMyFileMsg(nomade);
    	uiModel.addAttribute("listNomad", myFileMsg);
    	
    	try {
			List<FileMsg> meAndOther = fileMsgService.findByMeAndOther(nomade, nomad2);
			FileMsg fileMsg = meAndOther.get(0);
			fileMsg.setNumberUnreadMsg(0);
			fileMsgService.saveFileMsg(fileMsg);
			uiModel.addAttribute("msg", fileMsg.getMessages());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    	uiModel.addAttribute("nomade", nomade);
    	return "message/listMsg";
    }
    
    @RequestMapping("/clear")
    public String clear(@RequestParam("idNomad") String idNomad, HttpServletRequest request,
    		Model uiModel) {
       
    	UserNomade nomade = security.getUserNomade();
    	UserNomade nomad2 = userService.findUserNomade(new BigInteger(idNomad));
    	
    	List<FileMsg> myFileMsg = fileMsgService.findMyFileMsg(nomade);
    	uiModel.addAttribute("listNomad", myFileMsg);
    	
    	try {
			List<FileMsg> meAndOther = fileMsgService.findByMeAndOther(nomade, nomad2);
			FileMsg fileMsg = meAndOther.get(0);
			fileMsg.setMessages(new ArrayList<Message>());
			fileMsg.setNumberUnreadMsg(0);
			fileMsgService.saveFileMsg(fileMsg);
			uiModel.addAttribute("msg", fileMsg.getMessages());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    	uiModel.addAttribute("nomade", nomade);
    	return "message/listMsg";
    }
}
