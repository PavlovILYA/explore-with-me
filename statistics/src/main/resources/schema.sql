CREATE TABLE IF NOT EXISTS hits (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    app VARCHAR(255) NOT NULL,
    uri VARCHAR(255) NOT NULL,
    ip VARCHAR(50) NOT NULL,
    date TIMESTAMP NOT NULL ,
    PRIMARY KEY (id)
);