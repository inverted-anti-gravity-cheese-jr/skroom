package pl.pg.eti.kio.skroom;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class ModelAttr {
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ModelAttr(String id) {
		super();
		this.id = id;
	}
	
	

}
