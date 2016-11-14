package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersSecurityRecord;

/**
 * User security data model class to manage in app.
 *
 * @author Wojciech Stanisławski
 * @since 21.08.16
 */
public class UserSecurity {
	private int id;
	private String password;
	private String salt;
	private String secureQuestion;
	private String secureAnswer;
	private boolean accepted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSecureQuestion() {
		return secureQuestion;
	}

	public void setSecureQuestion(String secureQuestion) {
		this.secureQuestion = secureQuestion;
	}

	public String getSecureAnswer() {
		return secureAnswer;
	}

	public void setSecureAnswer(String secureAnswer) {
		this.secureAnswer = secureAnswer;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	@Override
	public String toString() {
		return "UserSecurity{" +
				"id=" + id +
				", password='" + password + '\'' +
				", salt='" + salt + '\'' +
				", secureQuestion='" + secureQuestion + '\'' +
				", secureAnswer='" + secureAnswer + '\'' +
				'}';
	}

	/**
	 * Method for converting database records into model classes.
	 *
	 * @param record Database record fetched by jOOQ
	 * @return Converted user
	 */
	public static UserSecurity fromDba(UsersSecurityRecord record) {
		UserSecurity userSecurity = new UserSecurity();

		userSecurity.setId(record.getId());
		userSecurity.setPassword(record.getPassword());
		userSecurity.setSalt(record.getSalt());
		userSecurity.setSecureQuestion(record.getSecureQuestion());
		userSecurity.setSecureAnswer(record.getSecureAnswer());
		userSecurity.setAccepted(record.getAccepted() == 1);

		return userSecurity;
	}
}
