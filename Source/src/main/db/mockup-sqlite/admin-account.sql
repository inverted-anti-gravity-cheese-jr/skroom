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

INSERT INTO USERS_SETTINGS VALUES (
    NULL, /* autoincremented value */
    1,
    -1,
    10,
    25,
    5
);
