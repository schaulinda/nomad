package com.nomade.web;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.web.multipart.MultipartFile;

import com.nomade.domain.BeanRegister;
import com.nomade.domain.Confidentiality;
import com.nomade.domain.Country;
import com.nomade.domain.Gender;
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
import com.nomade.tools.ImageUploadException;
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
				.append("\n\n").append("http://")
				.append(httpServletRequest.getServerName()).append(":")
				.append(httpServletRequest.getServerPort())
				.append("/users/activate/").append(nomade.getId());

		try {
			notificationService.sendMessage(beanRegister.getEmail(),
					stringBuilder.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			nomade.setDisableLogin(false);
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
		String message = "complete your profile";
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

	@RequestMapping("/profil")
	public String profil(UserNomade userNomade, Model uiModel,
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "photo", required = false) MultipartFile photo) {

		UserNomade userNomade2 = securite.getUserNomade();

		try {
			if (!photo.isEmpty()) {
				// validateImage(photo);
				ImageUtil.saveImage(userNomade2.getId() + "profil.jpg", photo);
				userNomade.getProfil().setFile(
						userNomade2.getId() + "profil.jpg");
				userNomade2.setProfil(userNomade.getProfil());
				userService.updateUserNomade(userNomade2);
			}
		} catch (ImageUploadException e) {
			String message = e.getMessage();
			return "redirect:/users/private/profil?message=" + message;
		}

		userNomade2.setProfil(userNomade.getProfil());
		userService.updateUserNomade(userNomade2);
		String message = "vos modifications on ete pris en compte..";
		return "redirect:/users/private/profil?message=" + message;

	}

	@RequestMapping("/compte")
	public String compte(UserNomade userNomade, Model uiModel,
			HttpServletRequest httpServletRequest) {
		UserNomade userNomade2 = securite.getUserNomade();
		if (!ValideEmailUtil.isValid(userNomade.getCompte().getEmail())) {
			String message = "adresse email invalid";
			return "redirect:/users/private/compte?message=" + message;
		}
		List<UserNomade> findByEmail = userService.findByEmail(userNomade
				.getCompte().getEmail());
		if (findByEmail != null
				&& findByEmail.size() > 0
				&& !findByEmail.get(0).getCompte().getEmail()
						.equals(userNomade2.getCompte().getEmail())) {
			String message = "email deja utilise";
			return "redirect:/users/private/compte?message=" + message;
		} else {

			userNomade2.setCompte(userNomade.getCompte());
			userService.updateUserNomade(userNomade2);
			String message = "vos modifications on ete pris en compte..";
			return "redirect:/users/private/compte?message=" + message;
		}

	}

	@RequestMapping(value = "/vehicule", method = RequestMethod.POST)
	public String vehicule(UserNomade userNomade, Model uiModel,
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "photo", required = false) MultipartFile photo) {

		UserNomade userNomade2 = securite.getUserNomade();

		try {
			if (!photo.isEmpty()) {
				// validateImage(photo);
				ImageUtil
						.saveImage(userNomade2.getId() + "vehicule.jpg", photo);
				userNomade.getVehicule().setPhoto(
						userNomade2.getId() + "vehicule.jpg");
			}
		} catch (ImageUploadException e) {

			userNomade2.setVehicule(userNomade.getVehicule());
			userService.updateUserNomade(userNomade2);
			String message = e.getMessage();
			return "redirect:/users/private/vehicule?message=" + message;
		}

		userNomade2.setVehicule(userNomade.getVehicule());
		userService.updateUserNomade(userNomade2);
		String message = "vos modifications on ete pris en compte..";
		return "redirect:/users/private/vehicule?message=" + message;

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
