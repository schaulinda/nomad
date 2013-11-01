package com.nomade.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.nomade.domain.PasswordReset;
import com.nomade.domain.UserNomade;
import com.nomade.security.SecurityUtil;
import com.nomade.service.UserService;

@RequestMapping("/passwordresets")
@Controller
public class PasswordResetController {
	
	@Autowired
	UserService userService;
	
    @RequestMapping(method = RequestMethod.POST)
    public String update(@Valid PasswordReset passwordReset, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if(!passwordReset.passwordsEqual()){
        	ObjectError error = new ObjectError("newPassword","Both password entered are not identical");
			bindingResult.addError(error);
        }
        UserNomade user = new SecurityUtil().getUserNomade();

        if(!user.checkExistingPasword(passwordReset.getCurrentPassword())){
        	ObjectError error = new ObjectError("currentPassword","Current password not matching our record.");
			bindingResult.addError(error);
        }
    	if (bindingResult.hasErrors()) {
            uiModel.addAttribute("passwordReset", passwordReset);
            return "profil/passwordReset";
        }
        uiModel.asMap().clear();
        user.changePassword(passwordReset.getNewPassword());
        userService.updateUserNomade(user);
        
        uiModel.addAttribute("msg", "password change succesfull!");
        return "profil/passwordReset";
    }
    
   
    
}

