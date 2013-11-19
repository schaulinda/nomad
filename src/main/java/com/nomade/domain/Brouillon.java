package com.nomade.domain;

public class Brouillon {
	
	
	/*@RequestMapping("/profil")
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

	}*/

}
