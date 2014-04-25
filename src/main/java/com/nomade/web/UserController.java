package com.nomade.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.gridfs.GridFSDBFile;
import com.nomade.domain.BeanHistorique;
import com.nomade.domain.BeanNomadeManager;
import com.nomade.domain.BeanNoteBookManager;
import com.nomade.domain.BeanRegister;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.Country;
import com.nomade.domain.DangerPratique;
import com.nomade.domain.Gender;
import com.nomade.domain.InfoPratique;
import com.nomade.domain.Langue;
import com.nomade.domain.Nationality;
import com.nomade.domain.PasswordReset;
import com.nomade.domain.RoleName;
import com.nomade.domain.UserNomade;
import com.nomade.domain.VehiculeState;
import com.nomade.domain.VehiculeType;
import com.nomade.email.NotificationService;
import com.nomade.security.NomadeUserDetailsService;
import com.nomade.security.Security;
import com.nomade.service.DangerPratiqueService;
import com.nomade.service.EtapeVehiculeService;
import com.nomade.service.EtapeVoyageService;
import com.nomade.service.InfoPratiqueService;
import com.nomade.service.ParcoursService;
import com.nomade.service.VoyageService;
import com.nomade.tools.ImageUtil;
import com.nomade.tools.ValideEmailUtil;

@RequestMapping("/users")
@Controller
@RooWebScaffold(path = "users", formBackingObject = UserNomade.class)
public class UserController {

	@Autowired
	NotificationService notificationService;
	@Autowired
	NomadeUserDetailsService nomadeUserDetailsService;
	@Autowired
	Security securite ;
	@Autowired
	ImageUtil imageUtil;
	
	@Autowired
	EtapeVoyageService etapeVoyageService;
	@Autowired
	EtapeVehiculeService etapeVehiculeService;
	@Autowired
	InfoPratiqueService infoPratiqueService;
	@Autowired
	DangerPratiqueService dangerPratiqueService;
	@Autowired
	ParcoursService parcoursService;
	@Autowired
	VoyageService voyageService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
private void beanHistoriqueDecoration(Model uiModel, UserNomade nomade) {
		
		BeanHistorique beanHistorique = new BeanHistorique();
		beanHistorique.setListInfo(infoPratiqueService.findByNomadeOrderByCreated(nomade));
		List<DangerPratique> listDanger = dangerPratiqueService.findByNomadeOrderByCreated(nomade);
		beanHistorique.setListDanger(listDanger);
			
		beanHistorique.setNomade(nomade);
		uiModel.addAttribute("beanHistorique", beanHistorique);
	}
	
	@RequestMapping("/register")
	public String register(@Valid BeanRegister beanRegister,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {

		if (!beanRegister.getPassword().equals(
				beanRegister.getConfirmPassword())) {
			bindingResult.rejectValue("password",
					"password_diff_confirmPassword", "password_diff_confirmPassword");

		}

		List<UserNomade> list = userService.findByUserName(beanRegister
				.getUserName());
		if (list != null && list.size() > 0) {
			bindingResult.rejectValue("userName", "username_already_exist",
					"username already exist!");

		}

		List<UserNomade> list1 = userService.findByEmail(beanRegister
				.getEmail());
		if (list1 != null && list1.size() > 0) {
			bindingResult.rejectValue("email", "email_already_exist",
					"email already exist!");

		}
		if (beanRegister.getUserName().equals(beanRegister.getPassword()) == true) {
			bindingResult.rejectValue("password", "password_as_username",
					"password can't be same as username");
		}
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("beanRegister", beanRegister);
			return "login";
		}
		uiModel.asMap().clear();
		Set<RoleName> roleNames = new HashSet<RoleName>();
		roleNames.add(RoleName.ROLE_SIMPLE_USER);
		UserNomade nomade = new UserNomade(beanRegister.getUserName(),
				beanRegister.getPassword(), true, false, roleNames);
		nomade.getCompte().setEmail(beanRegister.getEmail());

		userService.saveUserNomade(nomade);

		StringBuilder stringBuilder = new StringBuilder()
				.append("we are glad to see you between our communeauty, Active your profil by cliking the link bellow.")
				.append("\n\n").append("https://")
				.append(httpServletRequest.getServerName())
				//.append(":").append(httpServletRequest.getServerPort())
				.append("/users/activate/").append(nomade.getId());

		try {
			notificationService.sendMessage(beanRegister.getEmail(),
					stringBuilder.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//nomade.setDisableLogin(true);
			userService.saveUserNomade(nomade);
		}
		return "succesRegister";
	}
	
	
	@RequestMapping("/activate/{id}")
	public String activate(@PathVariable("id") BigInteger id, Model uiModel,
			HttpServletRequest httpServletRequest) {
		UserNomade nomade;
		try {
			nomade = userService.findUserNomade(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			uiModel.addAttribute("error", "bad link!");
			return "accountActivate";
		}
		nomade.setDisableLogin(false);
		userService.updateUserNomade(nomade);
		boolean log = autoLogin(nomade.getUserName(), httpServletRequest);//do something with log later
		return "redirect:/users/private/profil";
	}

	/**
	 * Automatic login after successful registration.
	 * 
	 * @param username
	 */
	public boolean autoLogin(String username, HttpServletRequest httpServletRequest) {
		try {
			
			UserDetails userDetails = nomadeUserDetailsService
					.loadUserByUsername(username);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					userDetails, SecurityContextHolder.getContext()
							.getAuthentication().getCredentials(),userDetails.getAuthorities());
			
			
			// Place the new Authentication object in the security context.
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@RequestMapping("/private/{page}")
	public String pagePersoForm(@PathVariable("page") String page,
			Model uiModel, HttpServletRequest httpServletRequest,
			@RequestParam(value = "message", required = false) String message) {


		uiModel.addAttribute("message", message);
		uiModel.addAttribute("nomade", securite.getUserNomade());
		if (page.equals("passwordReset")) {
			uiModel.addAttribute("passwordReset", new PasswordReset());
		}
		return "profil/" + page;

	}
	
	/*@RequestMapping(method = RequestMethod.GET, value = "updateVehState")
	public @ResponseBody
	String updateVehState(Model uiModel, HttpServletRequest httpServletRequest,
			HttpServletResponse response, @RequestParam("vehState") String vehState){
		
		UserNomade userNomade = securite.getUserNomade();
		userNomade.getVehicule().setVehiculeState(VehiculeState.valueOf(vehState));
		userService.updateUserNomade(userNomade);
		return "good";
	}*/
	
	@RequestMapping("/updateVehState/{vehState}")
	public @ResponseBody String updateVehState(@PathVariable("vehState") String vehState, Model uiModel, HttpServletRequest httpServletRequest,
			HttpServletResponse response){
		
		UserNomade userNomade = securite.getUserNomade();
		userNomade.getVehicule().setVehiculeState(VehiculeState.valueOf(vehState));
		userService.updateUserNomade(userNomade);
		return userNomade.getVehicule().getIcon();
	}

	// this function has to be refactoring in another class
	@RequestMapping(method = RequestMethod.POST, value = "updateField")
	public @ResponseBody
	String updateField(Model uiModel, HttpServletRequest httpServletRequest,
			HttpServletResponse response, @RequestParam("name") String name,
			@RequestParam("pk") String pk,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "value[]", required = false) String[] values) {

		UserNomade userNomade = securite.getUserNomade();

		if (name.equals("username") && value != null) {
			List<UserNomade> findByUserName = userService.findByUserName(value);
			if (findByUserName != null && findByUserName.size() > 0
					&& !findByUserName.get(0).equals(userNomade.getUserName())) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return "username ready used";

			} else {
				userNomade.setUserName(value);
				userService.updateUserNomade(userNomade);
				return "";
			}
		}
		if (name.equals("website") && value != null) {
			userNomade.getProfil().setWebsite(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("butVoyage") && value != null) {
			userNomade.getProfil().setButVoyage(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("metier") && value != null) {
			userNomade.getProfil().setMetier(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("visitedCountry") && values != null) {
			Set<Country> visitedCountry = new HashSet<Country>();
			for (String s : values) {
				visitedCountry.add(Country.valueOf(s));
			}
			userNomade.getProfil().setVisitedCountry(visitedCountry);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("langues") && values != null) {
			Set<Langue> langues = new HashSet<Langue>();
			for (String s : values) {
				langues.add(Langue.valueOf(s));
			}
			userNomade.getProfil().setLangues(langues);
			userService.updateUserNomade(userNomade);
			return "";
		}

		if (name.equals("email") && value != null) {

			List<UserNomade> findByEmail = userService.findByEmail(value);
			if (findByEmail != null && findByEmail.size() > 0
					&& !findByEmail.get(0).equals(userNomade.getUserName())) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return "email ready used";

			} else {
				if (!ValideEmailUtil.isValid(value)) {

					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return "adress email invalid";
				}

				else {
					userNomade.getCompte().setEmail(value);
					userService.updateUserNomade(userNomade);
					return "";
				}
			}
		}

		if (name.equals("fullName") && value != null) {
			userNomade.getCompte().setFullName(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("gender") && value != null) {
			userNomade.getCompte().setGender(Gender.valueOf(value));
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("birthDate") && value != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date convertedDate;
			try {
				convertedDate = dateFormat.parse(value);
				userNomade.getCompte().setBirthDate(convertedDate);
				userService.updateUserNomade(userNomade);
				return "";
			} catch (ParseException e) {

				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return "bad date format";
			}

		}
		if (name.equals("adress") && value != null) {
			userNomade.getCompte().setAdress(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("phoneNumber") && value != null) {
			userNomade.getCompte().setPhoneNumber(value);
			userService.updateUserNomade(userNomade);
			return "";
		}

		if (name.equals("nationality") && value != null) {
			System.out.print("nationality: " + value);
			userNomade.getCompte().setNationality(Nationality.valueOf(value));
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("vehiculeName") && value != null) {
			userNomade.getVehicule().setVehiculeName(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("model") && value != null) {
			userNomade.getVehicule().setModel(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("couleur") && value != null) {
			userNomade.getVehicule().setCouleur(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("description") && value != null) {
			userNomade.getVehicule().setDescription(value);
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("anneMiseEnService") && value != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date convertedDate;
			try {
				convertedDate = dateFormat.parse(value);
				System.out.print("convertedDate: " + convertedDate);
				userNomade.getVehicule().setAnneMiseEnService(convertedDate);
				userService.updateUserNomade(userNomade);
				return "";
			} catch (ParseException e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return "bad date format";
			}
		}
		if (name.equals("vehiculeType") && value != null) {

			userNomade.getVehicule().setVehiculeType(
					VehiculeType.valueOf(value));
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("vehiculeState") && value != null) {

			userNomade.getVehicule().setVehiculeState(
					VehiculeState.valueOf(value));
			userService.updateUserNomade(userNomade);
			return "";
		}
		if (name.equals("country") && value != null) {

			userNomade.getVehicule().setForSellCountry(Country.valueOf(value));
			userService.updateUserNomade(userNomade);
			return "";
		}

		return "";
	}

	@RequestMapping("/security")
	public String security(UserNomade userNomade, Model uiModel,
			HttpServletRequest httpServletRequest) {

		UserNomade userNomade2 = securite.getUserNomade();
		userNomade2.getCompte().setConfidentiality(
				userNomade.getCompte().getConfidentiality());
		userNomade2.getCompte().setNewsletter(
				userNomade.getCompte().getNewsletter());
		userNomade2.getCompte().setNotifications(
				userNomade.getCompte().getNotifications());
		userNomade2.getCompte().setCommercialParteners(
				userNomade.getCompte().getCommercialParteners());
		userService.updateUserNomade(userNomade2);

		uiModel.addAttribute("passwordReset", new PasswordReset());
		uiModel.addAttribute("nomade", securite.getUserNomade());
		uiModel.addAttribute("msg1", "security parameter update!");
		return "profil/passwordReset";

	}
	
	
	@RequestMapping("/selectImg/{id}")
	public String selectImg(@PathVariable("id") String id, Model uiModel,
			HttpServletRequest request) {
		
		
		UserNomade nomade = securite.getUserNomade();
		String stringPage = request.getSession(true).getAttribute("backLink").toString();
		String entityId = request.getSession(true).getAttribute("entityId").toString();
		Object commentAttribute = request.getSession(true).getAttribute("commentId");
		String commentId = null ;
		if(commentAttribute != null){
			commentId=commentAttribute.toString();
		}
		//render previous page with marker an historik
		BeanNomadeManager beanNomadeManager = new BeanNomadeManager();

		beanHistoriqueDecoration(uiModel, nomade);

		beanNomadeManager.setMarker(voyageService.buildNomadMakers(request));
		beanNomadeManager.setMe(true);
		beanNomadeManager.setNomade(nomade);

		uiModel.addAttribute("beanNomadeManager", beanNomadeManager);
		//---------------end----------
		
		if(stringPage==null){
			return "redirect:/";
		}
		
		if(stringPage.equals("profil")){
			String thumbailImg = thumbailImg(id);
			nomade.getProfil().setFile(thumbailImg);
			userService.updateUserNomade(nomade);
			uiModel.addAttribute("nomade", securite.getUserNomade());
			return "profil/profil";
		}
		
		if(stringPage.equals("vehicule")){
			String thumbailImg = thumbailImg(id);
			nomade.getVehicule().setPhoto(thumbailImg);
			userService.updateUserNomade(nomade);
			uiModel.addAttribute("nomade", securite.getUserNomade());
			return "profil/vehicule";
		}
		
		if(stringPage.equals("carnet")){
			BeanNoteBookManager beanNoteBookManager = new BeanNoteBookManager();
			beanNoteBookManager.setVoyageEnCours(voyageService.existingVoyage(nomade));
			beanNoteBookManager.getEtapeVoyage().setUserPhoto(id);
			uiModel.addAttribute("beanNoteBookManager", beanNoteBookManager);
			uiModel.addAttribute("nomade", securite.getUserNomade());
			uiModel.addAttribute("onglet", "carnet");
			return "voyages/carnet";
		}
		
		if(stringPage.equals("infoPratique")){
			InfoPratique infoPratique = new InfoPratique();
			infoPratique.setPhoto(id);
			uiModel.addAttribute("infoPratique", infoPratique);
			uiModel.addAttribute("nomade", securite.getUserNomade());
			
			return "public/info";
		}
		if(stringPage.equals("dangerPratique")){
			DangerPratique dangerPratique = new DangerPratique();
			dangerPratique.setPhoto(id);
			uiModel.addAttribute("dangerPratique", dangerPratique);
			uiModel.addAttribute("nomade", securite.getUserNomade());
			
			return "public/danger";
		}
		if("forumSubTopicView".equals(stringPage)){
			return "redirect:/forum/subtopics/"+encodeUrlPathSegment(entityId.toString(), request)+"?imageId="+encodeUrlPathSegment(id, request);
		}

		if("forumDiscussionView".equals(stringPage)){
			return "redirect:/forum/discussions/"+encodeUrlPathSegment(entityId.toString(), request)+"?imageId="+encodeUrlPathSegment(id, request);
		}
		if("forumUpdateDiscussionView".equals(stringPage)){
			return "redirect:/forum/discussions/"+encodeUrlPathSegment(entityId, request)+"/form?imageId="+encodeUrlPathSegment(id, request);
		}
		if("forumUpdateCommentView".equals(stringPage)){
			return "redirect:/forum/discussions/"+encodeUrlPathSegment(entityId, request)+"/comments/"+encodeUrlPathSegment(commentId, request)+"/form?imageId="+encodeUrlPathSegment(id, request);
		}
		
		return "redirect:/";
	}

	
	private String thumbailImg(String id){
		
		GridFSDBFile gridFSDBFile = imageUtil.get(id);
		InputStream inputStream = gridFSDBFile.getInputStream();
		String filename = gridFSDBFile.getFilename();
		String contentType = gridFSDBFile.getContentType();
		contentType = StringUtils.split(contentType, "/")[1];
		
		String savethumbail;
		try {//render thumbnail
			BufferedImage imBuff = ImageIO.read(inputStream);
			BufferedImage scaledImg = Scalr.resize(imBuff, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
		               150, 100, Scalr.OP_ANTIALIAS);
			ByteArrayOutputStream bas = new ByteArrayOutputStream();
			ImageIO.write(scaledImg, contentType, bas);
			InputStream isImg = new ByteArrayInputStream(bas.toByteArray());
			 savethumbail = imageUtil.savethumbail(isImg, contentType, filename);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			savethumbail=null;
		}
		
		return savethumbail;
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("userNomade_accountexpiration_date_format",
				"dd-MM-yyyy HH:mm");
		uiModel.addAttribute("userNomade_credentialexpiration_date_format",
				"dd-MM-yyyy HH:mm");
	}

	void populateEditForm(Model uiModel, UserNomade userNomade) {
		uiModel.addAttribute("userNomade", userNomade);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("countrys", Arrays.asList(Country.values()));
		uiModel.addAttribute("langues", Arrays.asList(Langue.values()));
	}

	@ModelAttribute("countrys")
	public Collection<Country> populateCountrys() {
		return Arrays.asList(Country.values());
	}

	@ModelAttribute("vehiculeTypes")
	public Collection<VehiculeType> vehiculeTypes() {
		return Arrays.asList(VehiculeType.values());
	}

	@ModelAttribute("vehiculeStates")
	public Collection<VehiculeState> vehiculeStates() {
		return Arrays.asList(VehiculeState.values());
	}

	@ModelAttribute("nationalitys")
	public Collection<Nationality> nationality() {
		return Arrays.asList(Nationality.values());
	}

	@ModelAttribute("lan")
	public Collection<Langue> populateLangue() {
		return Arrays.asList(Langue.values());
	}

	@ModelAttribute("genders")
	public Collection<Gender> genders() {
		return Arrays.asList(Gender.values());
	}

	@ModelAttribute("confidentialitys")
	public Collection<Confidentiality> confidentialitys() {
		return Arrays.asList(Confidentiality.values());
	}
}
