package com.nomade.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nomade.domain.PasswordReset;
import com.nomade.domain.UserNomade;
import com.nomade.email.NotificationService;
import com.nomade.security.Security;
import com.nomade.service.UserService;
import com.nomade.tools.ValideEmailUtil;

@RequestMapping("/passwordresets")
@Controller
public class PasswordResetController {
	
	@Autowired
	UserService userService;
	@Autowired
	Security securite ;
	@Autowired
	NotificationService notificationService;
	
    @RequestMapping(method = RequestMethod.POST)
    public String update(@Valid PasswordReset passwordReset, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
      
    	if(!passwordReset.passwordsEqual()){
        	bindingResult.rejectValue("newPassword", "pwd_not_same", "Both password entered are not identical");
			
        }
        UserNomade user = securite.getUserNomade();

        if(!user.checkExistingPasword(passwordReset.getCurrentPassword())){
        	bindingResult.rejectValue("currentPassword", "pwd_not_indb", "Current password not matching our record.");
			
        }
    	if (bindingResult.hasErrors()) {
            uiModel.addAttribute("passwordReset", passwordReset);
            uiModel.addAttribute("nomade", securite.getUserNomade());
            return "profil/passwordReset";
        }
        uiModel.asMap().clear();
        user.changePassword(passwordReset.getNewPassword());
        userService.updateUserNomade(user);
        
        uiModel.addAttribute("msg", "password change succesfull!");
        uiModel.addAttribute("passwordReset", new PasswordReset());
        uiModel.addAttribute("nomade", securite.getUserNomade());
        return "profil/passwordReset";
    }
    
    @RequestMapping("/forgetPass")
    public String forgetPass( Model uiModel, HttpServletRequest httpServletRequest) {
    	
    	return "/forgetPassPage";
    }
    @Transactional
    @RequestMapping("/sendPass")
    public String sendPass(@RequestParam("email") String email, Model uiModel, HttpServletRequest httpServletRequest) {
    	
    	String msg;
    	if(!ValideEmailUtil.isValid(email)){
    		msg = "bad email!";
    		uiModel.addAttribute("msg", msg);
    		uiModel.addAttribute("email", email);
    		return "/forgetPassPage";
    	}
    	List<UserNomade> list = userService.findByEmail(email);
    	if(list!=null && list.size()>0){
    		UserNomade userNomade = list.get(0);
    		String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(7);
    		userNomade.changePassword(randomAlphanumeric);
    		StringBuilder stringBuilder = new StringBuilder()
			.append("You password have been reset, below the new password")
			.append("\n\n")
			.append("Password: ")
			.append(randomAlphanumeric);
    		
    		try {
				notificationService.sendMessage(email, stringBuilder.toString());
				userService.updateUserNomade(userNomade);
				uiModel.addAttribute("msg", "your password have been reset succesfully, check your mail");
				return "/sendPassSucced";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				uiModel.addAttribute("msg", "We encouter a problem, sorry! try later");
				uiModel.addAttribute("email", email);
				return "/forgetPassPage";
			}
			
    		
    	}else{
    		
    		uiModel.addAttribute("msg", "Sorry, no such adress in our system");
    		uiModel.addAttribute("email", email);
    		
    		return "/forgetPassPage";
    	}
    	
    	
    }
    
}

