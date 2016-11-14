INSERT INTO USERS VALUES (
	NULL, /* autoincremented value */
	"admin",
	"admin@foo.com",
	"null",
	0
);

INSERT INTO USERS_SECURITY VALUES (
	NULL, /* autoincremented value */
	1,
	"admin",
	"SALT",
	"Secure question",
	"Secure answer",
    1
);

INSERT INTO USER_ROLES_IN_PROJECT VALUES (
    NULL, /* autoincremented value */
    'Project Owner',
    '#3F51B5',
    1
);

INSERT INTO USER_ROLES_IN_PROJECT VALUES (
    NULL, /* autoincremented value */
    'Tester',
    '#F44336',
    0
);

INSERT INTO USER_ROLES_IN_PROJECT VALUES (
    NULL, /* autoincremented value */
    'Programmer',
    '#8BC34A',
    0
);

INSERT INTO USER_ROLES_IN_PROJECT VALUES (
    NULL, /* autoincremented value */
    'Analyst',
    '#FFC107',
    0
);