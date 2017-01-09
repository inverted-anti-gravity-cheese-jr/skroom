package pl.pg.eti.kio.skroom;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@SessionAttributes("testAttr2")
public class Test {

	@RequestMapping("/test")
	public String test(HttpSession session, @ModelAttribute("testAttr2") ModelAttr attr2) {
		//String attr = (String) session.getAttribute("testAttr");
		session.setAttribute("testAttr", "koopah");
		return attr2.getId();
	}
}
