CREATE TABLE phones (
	time INTEGER,
	registration_id TEXT
);

CREATE UNIQUE INDEX phonesRegistrationId 
ON phones (registration_id);
