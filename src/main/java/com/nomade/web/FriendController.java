package com.nomade.web;

import java.math.BigInteger;

import com.nomade.domain.DangerPratique;
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
   	public String voteMinus(@PathVariable("idUser") String idUser, Model uiModel) {
       	BigInteger bigInteger = new BigInteger(idUser);
       	UserNomade userNomade = userservice.findUserNomade(bigInteger);
       	Relation relation = new Relation(security.getUserNomade(), userNomade, FriendState.EN_ATTENTE);
       	relationService.saveRelation(relation);
       	
       	return "true";
       }
}
