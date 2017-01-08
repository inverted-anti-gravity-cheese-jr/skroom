package pl.pg.eti.kio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/*
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ResourceConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/BOOT-INF/classes/WEB-INF");
	}
}
*/

@Controller
public class TestTest {
	
	@RequestMapping(value = "/testview2", method = RequestMethod.GET)
	public String test() {
		System.out.println("WAL SIE BOOT test2");
		return "hello";
	}
	
	/*
	@RequestMapping(value = "/testview", method = RequestMethod.GET)
	public ModelAndView test() {
		return new ModelAndView("greet");
	}*/
}