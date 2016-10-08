CREATE TABLE USERS_SECURITY (
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	USER_ID INTEGER,
	PASSWORD TEXT,
	SALT TEXT,
	SECURE_QUESTION TEXT,
	SECURE_ANSWER TEXT,
	FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);
