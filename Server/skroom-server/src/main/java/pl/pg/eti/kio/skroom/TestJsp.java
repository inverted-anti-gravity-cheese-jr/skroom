package pl.pg.eti.kio.skroom;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("testAttr2")
public class TestJsp {
	/*
	@ModelAttribute("testAttr")
	public String defaultNull() { return "stert"; }
	*/
	@ModelAttribute("testAttr2")
	public ModelAttr defaultNull2() { return new ModelAttr(null); }

	@RequestMapping("/")
	public ModelAndView test(HttpSession session, @ModelAttribute("testAttr2") ModelAttr attr2) {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("item", attr2.getId());
		attr2.setId("zapisalo");
		return modelAndView;
	}
}
