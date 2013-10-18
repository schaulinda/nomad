package com.nomade.web;

import com.nomade.domain.Vehicule;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/vehicules")
@Controller
@RooWebScaffold(path = "vehicules", formBackingObject = Vehicule.class)
public class VehiculeController {
}
