CREATE TABLE PROJECTS (
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	NAME TEXT,
	DESCRIPTION TEXT,
	DEFAULT_SPRINT_LENGTH INTEGER
);

CREATE TABLE SPRINTS (
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	NAME TEXT,
	START_DAY DATE,
	END_DAY DATE,
	PROJECT_ID INTEGER,
	FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS(ID)
);

CREATE TABLE USER_STORY_STATUS (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    STATUS_NAME TEXT,
    COLOR TEXT,
    IS_ARCHIVE INTEGER
);

CREATE TABLE USER_STORIES (
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	NAME TEXT,
	AS_A_STORY TEXT,
	I_WANT_TO_STORY TEXT,
	SO_THAT_STORY TEXT,
    DESCRIPTION TEXT,
	PRIORITY INTEGER,
	STORY_POINTS INTEGER,
	STATUS_ID INTEGER,
	PROJECT_ID INTEGER,
	FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS(ID),
    FOREIGN KEY (STATUS_ID) REFERENCES USER_STORY_STATUS(ID)
);

CREATE TABLE USERS (
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	NAME TEXT,
	EMAIL TEXT,
	AVATAR TEXT,
	ROLE INTEGER
);

CREATE TABLE USERS_SECURITY (
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	USER_ID INTEGER,
	PASSWORD TEXT,
	SALT TEXT,
	SECURE_QUESTION TEXT,
	SECURE_ANSWER TEXT,
    ACCEPTED INTEGER, /* 0 - hasn't been accepted by the admin yet, cannot log in, 1 - has been accepted, can log in */
	FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);

CREATE TABLE USERS_SETTINGS (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    USER_ID INTEGER,
    RECENT_PROJECT_ID INTEGER,
	USER_STORIES_PER_PAGE INTEGER,
    FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
    FOREIGN KEY (RECENT_PROJECT_ID) REFERENCES PROJECTS(ID)
);

CREATE TABLE USER_ROLES_IN_PROJECT (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    ROLE TEXT,
    COLOR TEXT,
    PRIVILIGES INTEGER /* 0 - cannot edit project preferences, 1 - can edit project preferences */
);

CREATE TABLE USERS_PROJECTS (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    USER_ID INTEGER,
    PROJECT_ID INTEGER,
    USER_ROLE_ID INTEGER,
    FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
    FOREIGN KEY (PROJECT_ID) REFERENCES PROJECTS(ID)
    FOREIGN KEY (USER_ROLE_ID) REFERENCES USER_ROLES_IN_PROJECT(ID)
);

CREATE TABLE TASKS (
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	NAME TEXT,
	ASSIGNEE INTEGER,
	TASK_STATUS INTEGER,
	STORY_POINTS INTEGER,
	PROJECT_ID INTEGER,
	FOREIGN KEY(ASSIGNEE) REFERENCES USERS(ID),
	FOREIGN KEY(PROJECT_ID) REFERENCES PROJECTS(ID)
);
