// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.web;

import com.nomade.domain.UserNomade;
import com.nomade.service.UserService;
import com.nomade.web.UserController;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect UserController_Roo_Controller {
    
    @Autowired
    UserService UserController.userService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String UserController.create(@Valid UserNomade userNomade, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, userNomade);
            return "users/create";
        }
        uiModel.asMap().clear();
        userService.saveUserNomade(userNomade);
        return "redirect:/users/" + encodeUrlPathSegment(userNomade.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String UserController.createForm(Model uiModel) {
        populateEditForm(uiModel, new UserNomade());
        return "users/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String UserController.show(@PathVariable("id") BigInteger id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("usernomade", userService.findUserNomade(id));
        uiModel.addAttribute("itemId", id);
        return "users/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String UserController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("usernomades", userService.findUserNomadeEntries(firstResult, sizeNo));
            float nrOfPages = (float) userService.countAllUserNomades() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("usernomades", userService.findAllUserNomades());
        }
        addDateTimeFormatPatterns(uiModel);
        return "users/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String UserController.update(@Valid UserNomade userNomade, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, userNomade);
            return "users/update";
        }
        uiModel.asMap().clear();
        userService.updateUserNomade(userNomade);
        return "redirect:/users/" + encodeUrlPathSegment(userNomade.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String UserController.updateForm(@PathVariable("id") BigInteger id, Model uiModel) {
        populateEditForm(uiModel, userService.findUserNomade(id));
        return "users/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String UserController.delete(@PathVariable("id") BigInteger id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        UserNomade userNomade = userService.findUserNomade(id);
        userService.deleteUserNomade(userNomade);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/users";
    }
    
    String UserController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
