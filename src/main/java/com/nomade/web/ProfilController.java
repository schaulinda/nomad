package com.nomade.web;

import com.nomade.domain.Profil;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/profils")
@Controller
@RooWebScaffold(path = "profils", formBackingObject = Profil.class)
public class ProfilController {
}
