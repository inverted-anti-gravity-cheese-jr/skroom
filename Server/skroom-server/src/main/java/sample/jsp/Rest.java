package sample.jsp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

	@RequestMapping("/rest")
	public String getHello() {
		return "hello";
	}
}
