package com.nomade.web;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nomade.domain.BeanHistorique;
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
import com.nomade.service.ParcoursService;

@RequestMapping("/infopratiques")
@Controller
@RooWebScaffold(path = "infopratiques", formBackingObject = InfoPratique.class)
public class InfoPratiqueController {

	@Autowired
	Security securite;
	@Autowired
	EtapeVoyageService voyageService;
	@Autowired
	EtapeVehiculeService vehiculeService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	@Autowired
	ParcoursService parcoursService;

	private void beanHistoriqueDecoration(Model uiModel, UserNomade nomade) {
		List<DangerPratique> listDanger = dangerPratiqueService.findByNomadeOrderByCreated(nomade);
		List<InfoPratique> listInfo = infoPratiqueService.findByNomadeOrderByCreated(nomade);

		BeanHistorique beanHistorique = new BeanHistorique();
		beanHistorique.setListDanger(listDanger);
		beanHistorique.setListInfo(listInfo);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
	}

	@RequestMapping("/save")
	public String save(InfoPratique infoPratique, Model uiModel,
			HttpServletRequest request) {

		UserNomade userNomade = securite.getUserNomade();

		double[] location = new double[] { infoPratique.getLocationLng(),
				infoPratique.getLocationLat() };
		infoPratique.setGeoLocation(location);
		infoPratique.setNomade(userNomade);
		infoPratique.setIcon(infoDecoration(infoPratique.getSelecteur1()));
		infoPratiqueService.saveInfoPratique(infoPratique);
		beanHistoriqueDecoration(uiModel, userNomade);
		uiModel.addAttribute("infoPratique", new InfoPratique());
		uiModel.addAttribute("saveInfoDanger", "saveInfoDanger");
		return "public/info";
	}

	public String infoDecoration(String typeInfo) {

		String linkIcon = "info";

		if (typeInfo.equals("toilettes")) {
			linkIcon = linkIcon + "/toillete.png";
		}
		if (typeInfo.equals("pointEau")) {
			linkIcon = linkIcon + "/pointEau.png";
		}
		if (typeInfo.equals("faune")) {
			linkIcon = linkIcon + "/curiositeFone.png";
		}
		if (typeInfo.equals("douches")) {
			linkIcon = linkIcon + "/douche.png";
		}
		if (typeInfo.equals("stationnementGratuite")) {
			linkIcon = linkIcon + "/airStationementGratuit.png";
		}
		if (typeInfo.equals("stationnementPayant")) {
			linkIcon = linkIcon + "/aireStationementPayant.png";
		}
		if (typeInfo.equals("jardin")) {
			linkIcon = linkIcon + "/plage.png";// to be change with the real
												// jardin icon
		}
		if (typeInfo.equals("camping")) {
			linkIcon = linkIcon + "/reseauNomad.png";
		}
		if (typeInfo.equals("pointRencontre")) {
			linkIcon = linkIcon + "/pointRencontre.png";
		}
		if (typeInfo.equals("station")) {
			linkIcon = linkIcon + "/stationEssence.png";
		}
		if (typeInfo.equals("vidange")) {
			linkIcon = linkIcon + "/vidangeCampingCar.png";
		}
		if (typeInfo.equals("shipping")) {
			linkIcon = linkIcon + "/shipping.png";
		}
		if (typeInfo.equals("rassemblement")) {
			linkIcon = linkIcon + "/rassemblement.png";
		}
		if (typeInfo.equals("garage")) {
			linkIcon = linkIcon + "/garage.png";
		}
		if (typeInfo.equals("flore")) {
			linkIcon = linkIcon + "/curiositeFlore.png";
		}
		if (typeInfo.equals("geologique")) {
			linkIcon = linkIcon + "/curiositeGeologiaue.png";
		}
		if (typeInfo.equals("historique")) {
			linkIcon = linkIcon + "/historique.png";
		}
		if (typeInfo.equals("monuments")) {
			linkIcon = linkIcon + "/curiositeMonument.png";
		}
		if (typeInfo.equals("marins")) {
			linkIcon = linkIcon + "/curiositeFond.png";
		}
		if (typeInfo.equals("lac")) {
			linkIcon = linkIcon + "/curiositeLac.png";
		}
		if (typeInfo.equals("plage")) {
			linkIcon = linkIcon + "/plage.png";
		}
		if (typeInfo.equals("randonnee")) {
			linkIcon = linkIcon + "/departRandonne.png";
		}
		return linkIcon;
	}

	@RequestMapping(value = "votePlus/{idInfo}")
	@ResponseBody
	public String votePositif(@PathVariable("idInfo") String idInfo,
			Model uiModel) {
		BigInteger bigInteger = new BigInteger(idInfo);
		InfoPratique infoPratique = infoPratiqueService
				.findInfoPratique(bigInteger);
		UserNomade userNomade = securite.getUserNomade();

		if (infoPratiqueService.hasVoted(userNomade, infoPratique)) {

			return "" + infoPratique.getVotePositif();

		}

		infoPratique.incrementVotePositif();
		infoPratique.getListVotants().add(userNomade);
		infoPratiqueService.updateInfoPratique(infoPratique);
		return "" + infoPratique.getVotePositif();
	}

	@RequestMapping(value = "voteMinus/{idInfo}")
	@ResponseBody
	public String voteMinus(@PathVariable("idInfo") String idInfo, Model uiModel) {
		BigInteger bigInteger = new BigInteger(idInfo);
		InfoPratique infoPratique = infoPratiqueService.findInfoPratique(bigInteger);
		UserNomade userNomade = securite.getUserNomade();
		if (infoPratiqueService.hasVoted(userNomade, infoPratique)) {

			return "" + infoPratique.getVoteNegatif();

		}

		infoPratique.incrementVoteNegatif();
		infoPratique.getListVotants().add(userNomade);
		infoPratiqueService.updateInfoPratique(infoPratique);
		return "" + infoPratique.getVoteNegatif();
	}

	@RequestMapping(value = "detail/{idInfo}")
	public String detail(
			@PathVariable("idInfo") String idInfo,
			@RequestParam(value = "cameFrom", required = false) String cameFrom,
			Model uiModel) {
		BigInteger bigInteger = new BigInteger(idInfo);
		InfoPratique infoPratique = infoPratiqueService
				.findInfoPratique(bigInteger);
		
		BigInteger id = infoPratique.getNomade().getId();

		if ("map".equals(cameFrom)) {
			uiModel.addAttribute("back", "itineraire");
		} else {
			if ("nomad".equals(cameFrom)) {
				uiModel.addAttribute("back", "nomad");
				uiModel.addAttribute("idNomad", ""+id);
			}else{
					uiModel.addAttribute("back", "formfinditineraire");		
			}
		}

		uiModel.addAttribute("infoPratique", infoPratique);
		return "public/infoDetail";
	}

	@RequestMapping("/infoSuiv/{id}/{page}")
	public String nomad(@PathVariable("id") String id,
			@PathVariable("page") int page, HttpServletRequest request,
			Model uiModel) {
		UserNomade nomade = userService.findUserNomade(new BigInteger(id));
		List<InfoPratique> listDanger = infoPratiqueService.findByNomadeOrderByCreated(nomade);
		BeanHistorique beanHistorique = new BeanHistorique();
		beanHistorique.setListInfo(listDanger);
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
		return "public/nomad";
	}
}
