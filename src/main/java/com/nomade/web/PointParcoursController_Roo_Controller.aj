// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nomade.web;

import com.nomade.domain.PointPacours;
import com.nomade.service.EtapeService;
import com.nomade.service.ParcoursService;
import com.nomade.service.PointParcoursService;
import com.nomade.web.PointParcoursController;
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

privileged aspect PointParcoursController_Roo_Controller {
    
    @Autowired
    PointParcoursService PointParcoursController.pointParcoursService;
    
    @Autowired
    EtapeService PointParcoursController.etapeService;
    
    @Autowired
    ParcoursService PointParcoursController.parcoursService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String PointParcoursController.create(@Valid PointPacours pointPacours, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, pointPacours);
            return "pointpacourses/create";
        }
        uiModel.asMap().clear();
        pointParcoursService.savePointPacours(pointPacours);
        return "redirect:/pointpacourses/" + encodeUrlPathSegment(pointPacours.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String PointParcoursController.createForm(Model uiModel) {
        populateEditForm(uiModel, new PointPacours());
        return "pointpacourses/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String PointParcoursController.show(@PathVariable("id") BigInteger id, Model uiModel) {
        uiModel.addAttribute("pointpacours", pointParcoursService.findPointPacours(id));
        uiModel.addAttribute("itemId", id);
        return "pointpacourses/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String PointParcoursController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("pointpacourses", pointParcoursService.findPointPacoursEntries(firstResult, sizeNo));
            float nrOfPages = (float) pointParcoursService.countAllPointPacourses() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("pointpacourses", pointParcoursService.findAllPointPacourses());
        }
        return "pointpacourses/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String PointParcoursController.update(@Valid PointPacours pointPacours, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, pointPacours);
            return "pointpacourses/update";
        }
        uiModel.asMap().clear();
        pointParcoursService.updatePointPacours(pointPacours);
        return "redirect:/pointpacourses/" + encodeUrlPathSegment(pointPacours.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String PointParcoursController.updateForm(@PathVariable("id") BigInteger id, Model uiModel) {
        populateEditForm(uiModel, pointParcoursService.findPointPacours(id));
        return "pointpacourses/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String PointParcoursController.delete(@PathVariable("id") BigInteger id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        PointPacours pointPacours = pointParcoursService.findPointPacours(id);
        pointParcoursService.deletePointPacours(pointPacours);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/pointpacourses";
    }
    
    void PointParcoursController.populateEditForm(Model uiModel, PointPacours pointPacours) {
        uiModel.addAttribute("pointPacours", pointPacours);
        uiModel.addAttribute("etapes", etapeService.findAllEtapes());
        uiModel.addAttribute("parcourses", parcoursService.findAllParcourses());
    }
    
    String PointParcoursController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
