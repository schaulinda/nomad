package com.nomade.web;

import java.math.BigInteger;

import com.nomade.domain.FriendState;
import com.nomade.domain.Relation;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/relations")
@Controller
@RooWebScaffold(path = "relations", formBackingObject = Relation.class)
public class FriendController {
	
	@Autowired
	UserService userservice;
	@Autowired
	Security security;
	
	@RequestMapping(value = "add/{idUser}")
   	@ResponseBody
   	public String add(@PathVariable("idUser") String idUser, Model uiModel) {
       	BigInteger bigInteger = new BigInteger(idUser);
       	UserNomade userNomade = userservice.findUserNomade(bigInteger);
       	Relation relation = new Relation(security.getUserNomade(), userNomade, FriendState.EN_ATTENTE);
       	relationService.saveRelation(relation);
       	
       	return "true";
       }
	
	@RequestMapping(value = "/friends")
   	public String listFriends(Model uiModel) {
		UserNomade nomade = security.getUserNomade();
		uiModel.addAttribute("nomade", nomade);
		uiModel.addAttribute("friends", relationService.findMyFriends(nomade));
		uiModel.addAttribute("demands", relationService.findReceivedDemand(nomade));
		return "relations/friends";
	}
	
	@RequestMapping(value = "/remove/{idUser}")
   	@ResponseBody
   	public String remove(@PathVariable("idUser") String idUser, Model uiModel) {
		UserNomade nomade = security.getUserNomade();
		BigInteger bigInteger = new BigInteger(idUser);
       	UserNomade userNomade = userservice.findUserNomade(bigInteger);
       	try {
			Relation relation = relationService.findMyFriendUnique(userNomade, nomade).get(0);
			relationService.deleteRelation(relation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	return "true";
       }
	
	@RequestMapping(value = "/accept/{idUser}")
   	@ResponseBody
   	public String accept(@PathVariable("idUser") String idUser, Model uiModel) {
		UserNomade nomade = security.getUserNomade();
		BigInteger bigInteger = new BigInteger(idUser);
       	UserNomade userNomade = userservice.findUserNomade(bigInteger);
       	try {
			Relation relation = relationService.findByNomadeAndNomade2(userNomade, nomade).get(0);
			relation.setFriendState(FriendState.AMI);
			relationService.updateRelation(relation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	return "true";
       }
}
