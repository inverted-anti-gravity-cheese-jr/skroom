package pl.pg.eti.kio.skroom;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import pl.pg.eti.kio.skroom.tags.ResourceBundleTag;

@SpringBootApplication
@ComponentScan({"pl.pg.eti.kio.skroom", "pl.pg.eti.kio.skroom.controller", "pl.pg.eti.kio.skroom.model"})
public class SkroomServer {

	public static void main(String[] args) {
		ResourceBundleTag.changeRootPath("/");
		SpringApplication.run(SkroomServer.class, args);
	}
}
