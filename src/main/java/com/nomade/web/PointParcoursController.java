package com.nomade.web;

import com.nomade.domain.PointPacours;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/pointpacourses")
@Controller
@RooWebScaffold(path = "pointpacourses", formBackingObject = PointPacours.class)
public class PointParcoursController {
}
