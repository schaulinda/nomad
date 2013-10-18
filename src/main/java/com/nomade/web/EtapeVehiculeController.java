package com.nomade.web;

import com.nomade.domain.EtapeVehicule;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/etapevehicules")
@Controller
@RooWebScaffold(path = "etapevehicules", formBackingObject = EtapeVehicule.class)
public class EtapeVehiculeController {
}
