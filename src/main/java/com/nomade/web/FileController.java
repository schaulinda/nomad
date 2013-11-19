package com.nomade.web;

import com.nomade.domain.ImageInfo;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/files")
@Controller
@RooWebScaffold(path = "files", formBackingObject = ImageInfo.class)
public class FileController {
}
