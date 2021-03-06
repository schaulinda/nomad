// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.web;

import com.nomade.domain.EtapeVoyage;
import com.nomade.service.EtapeVoyageService;
import com.nomade.web.EtapeVoyageController;
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

privileged aspect EtapeVoyageController_Roo_Controller {
    
    @Autowired
    EtapeVoyageService EtapeVoyageController.etapeVoyageService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String EtapeVoyageController.create(@Valid EtapeVoyage etapeVoyage, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, etapeVoyage);
            return "etapevoyages/create";
        }
        uiModel.asMap().clear();
        etapeVoyageService.saveEtapeVoyage(etapeVoyage);
        return "redirect:/etapevoyages/" + encodeUrlPathSegment(etapeVoyage.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String EtapeVoyageController.createForm(Model uiModel) {
        populateEditForm(uiModel, new EtapeVoyage());
        return "etapevoyages/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String EtapeVoyageController.show(@PathVariable("id") BigInteger id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("etapevoyage", etapeVoyageService.findEtapeVoyage(id));
        uiModel.addAttribute("itemId", id);
        return "etapevoyages/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String EtapeVoyageController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("etapevoyages", etapeVoyageService.findEtapeVoyageEntries(firstResult, sizeNo));
            float nrOfPages = (float) etapeVoyageService.countAllEtapeVoyages() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("etapevoyages", etapeVoyageService.findAllEtapeVoyages());
        }
        addDateTimeFormatPatterns(uiModel);
        return "etapevoyages/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String EtapeVoyageController.update(@Valid EtapeVoyage etapeVoyage, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, etapeVoyage);
            return "etapevoyages/update";
        }
        uiModel.asMap().clear();
        etapeVoyageService.updateEtapeVoyage(etapeVoyage);
        return "redirect:/etapevoyages/" + encodeUrlPathSegment(etapeVoyage.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String EtapeVoyageController.updateForm(@PathVariable("id") BigInteger id, Model uiModel) {
        populateEditForm(uiModel, etapeVoyageService.findEtapeVoyage(id));
        return "etapevoyages/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String EtapeVoyageController.delete(@PathVariable("id") BigInteger id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        EtapeVoyage etapeVoyage = etapeVoyageService.findEtapeVoyage(id);
        etapeVoyageService.deleteEtapeVoyage(etapeVoyage);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/etapevoyages";
    }
    
    String EtapeVoyageController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
