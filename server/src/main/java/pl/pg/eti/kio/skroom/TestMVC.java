package pl.pg.eti.kio.skroom;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMVC {

	@RequestMapping("/test")
	public String g() { return "g"; }
}
