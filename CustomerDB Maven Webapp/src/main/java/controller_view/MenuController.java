package controller_view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class MenuController {

	@RequestMapping(value = "menu", method = RequestMethod.GET)
	public ModelAndView toMenuPage(){
		return new ModelAndView("jsp/menu");
	}
	
	@RequestMapping(value = "tagstore", method = RequestMethod.GET)
	public ModelAndView toTagstorePage(){
		return new ModelAndView("jsp/tagstore");
	}
	
	@RequestMapping(value = "systemdynamicvalues", method = RequestMethod.GET)
	public ModelAndView toSystemdynamicvaluesPage(){
		return new ModelAndView("jsp/systemdynamicvalues");
	}

}
