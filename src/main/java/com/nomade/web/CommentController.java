package com.nomade.web;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import com.nomade.domain.BeanHistorique;
import com.nomade.domain.Comment;
import com.nomade.domain.DangerPratique;
import com.nomade.domain.EtapeVehicule;
import com.nomade.domain.EtapeVoyage;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.UserNomade;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.InfoPratiqueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/comments")
@Controller
@RooWebScaffold(path = "comments", formBackingObject = Comment.class)
public class CommentController {
	
	@Autowired
	EtapeVoyageService etapeVoyageService;
	@Autowired
	EtapeVehiculeService etapeVehiculeService;
	@Autowired
	InfoPratiqueService infoPratiqueService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	@Autowired
	Security security;
	
	@RequestMapping("/addEtapeVoy")
	public String add(@RequestParam("etapeId")String etapeId, 
			@RequestParam("page")int page, 
			@RequestParam("commentaire")String commentaire, BeanHistorique beanHistorique, HttpServletRequest request, Model uiModel) {
		
		EtapeVoyage voyage = etapeVoyageService.findEtapeVoyage(new BigInteger(etapeId));
		UserNomade nomade = voyage.getNomade();
		
		Comment comment = new Comment();
		comment.setCommentaire(commentaire);
		comment.setNomade(security.getUserNomade());
		
		voyage.getComments().add(comment);
		etapeVoyageService.updateEtapeVoyage(voyage);
		
		Page<EtapeVoyage> listEtapeVoy = etapeVoyageService.findByNomade(
				nomade, 0);
		beanHistorique.setListEtapeVoy(listEtapeVoy);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
		
		return "public/nomad";
	}
	
	@RequestMapping("/addEtapeVeh")
	public String addveh(@RequestParam("etapeVehId")String etapeId, 
			@RequestParam("pageVeh")int page, 
			@RequestParam("commentaireVeh")String commentaire, BeanHistorique beanHistorique, HttpServletRequest request, Model uiModel) {
		
		EtapeVehicule voyage = etapeVehiculeService.findEtapeVehicule(new BigInteger(etapeId));
		UserNomade nomade = voyage.getNomade();
		
		Comment comment = new Comment();
		comment.setCommentaire(commentaire);
		comment.setNomade(security.getUserNomade());
		
		voyage.getComments().add(comment);
		etapeVehiculeService.updateEtapeVehicule(voyage);
		
		Page<EtapeVehicule> listEtapeVeh = etapeVehiculeService.findByNomade(
				nomade, 0);
		beanHistorique.setListEtapeVeh(listEtapeVeh);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
		
		return "public/nomad";
	}
	
	@RequestMapping("/addInfo")
	public String adddanger(@RequestParam("InfoId")String etapeId, 
			@RequestParam("pageInfo")int page, 
			@RequestParam("commentaireInfo")String commentaire, BeanHistorique beanHistorique, HttpServletRequest request, Model uiModel) {
		
		InfoPratique info = infoPratiqueService.findInfoPratique(new BigInteger(etapeId));
		UserNomade nomade = info.getNomade();
		
		Comment comment = new Comment();
		comment.setCommentaire(commentaire);
		comment.setNomade(security.getUserNomade());
		
		info.getComments().add(comment);
		infoPratiqueService.updateInfoPratique(info);
		
		Page<InfoPratique> listInfo = infoPratiqueService.findByNomade(
				nomade, 0);
		beanHistorique.setListInfo(listInfo);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
		
		return "public/nomad";
	}
	
	//add comment on detail info
	@RequestMapping("/addCommentInfo")
	public String addCommentInfo(@RequestParam("InfoId")String etapeId,  
			@RequestParam("commentaireInfo")String commentaire, @RequestParam("cameFrom")String cameFrom, HttpServletRequest request, Model uiModel) {
		
		InfoPratique info = infoPratiqueService.findInfoPratique(new BigInteger(etapeId));
		UserNomade nomade = info.getNomade();
		
		Comment comment = new Comment();
		comment.setCommentaire(commentaire);
		comment.setNomade(security.getUserNomade());
		
		info.getComments().add(comment);
		infoPratiqueService.updateInfoPratique(info);
		
		if("map".equals(cameFrom)){
       		uiModel.addAttribute("back", "itineraire");
       	}else{
       		uiModel.addAttribute("back", "formfinditineraire");
       	}
		
		InfoPratique infoPratique = infoPratiqueService.findInfoPratique(info.getId());
       	uiModel.addAttribute("infoPratique", infoPratique);
       	uiModel.addAttribute("nomade", nomade);
		return "public/infoDetail";
	}
	
	//add comment on detail danger
	@RequestMapping("/addCommentDanger")
	public String addCommentDanger(@RequestParam("InfoId")String etapeId,  
			@RequestParam("commentaireInfo")String commentaire, @RequestParam("cameFrom")String cameFrom, HttpServletRequest request, Model uiModel) {
		
		DangerPratique danger = dangerPratiqueService.findDangerPratique(new BigInteger(etapeId));
		UserNomade nomade = danger.getNomade();
		
		Comment comment = new Comment();
		comment.setCommentaire(commentaire);
		comment.setNomade(security.getUserNomade());
		
		danger.getComments().add(comment);
		dangerPratiqueService.updateDangerPratique(danger);
		
		if("map".equals(cameFrom)){
       		uiModel.addAttribute("back", "itineraire");
       	}else{
       		uiModel.addAttribute("back", "formfinditineraire");
       	}
		
		DangerPratique dangerPratique = dangerPratiqueService.findDangerPratique(danger.getId());
       	uiModel.addAttribute("dangerPratique", dangerPratique);
       	uiModel.addAttribute("nomade", nomade);
		return "public/dangerDetail";
	}
	
	@RequestMapping("/addDanger")
	public String addInfo(@RequestParam("dangerId")String etapeId, 
			@RequestParam("pageDanger")int page, 
			@RequestParam("commentaireDanger")String commentaire, BeanHistorique beanHistorique, HttpServletRequest request, Model uiModel) {
		
		DangerPratique danger = dangerPratiqueService.findDangerPratique(new BigInteger(etapeId));
		UserNomade nomade = danger.getNomade();
		
		Comment comment = new Comment();
		comment.setCommentaire(commentaire);
		comment.setNomade(security.getUserNomade());
		
		danger.getComments().add(comment);
		dangerPratiqueService.updateDangerPratique(danger);
		
		Page<DangerPratique> listDanger = dangerPratiqueService.findByNomade(
				nomade, 0);
		beanHistorique.setListDanger(listDanger);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
		
		return "public/nomad";
	}
}
