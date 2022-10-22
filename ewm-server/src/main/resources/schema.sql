CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    annotation VARCHAR(255) UNIQUE NOT NULL, --
    category_id BIGINT NOT NULL, --
    created_on VARCHAR(50) NOT NULL,
    description TEXT, --
    event_date VARCHAR(50) NOT NULL, --
    initiator_id BIGINT NOT NULL,
    location POINT, --
    paid BOOLEAN NOT NULL, --
    participant_limit INT DEFAULT 0, --
    published_on VARCHAR(50) NOT NULL,
    request_moderation BOOLEAN DEFAULT TRUE, --
    state VARCHAR(50) NOT NULL,
    title TEXT UNIQUE NOT NULL, --
    --     views
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (initiator_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    title TEXT UNIQUE NOT NULL,
    pinned BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events_compilations (
    event_id BIGINT,
    compilation_id BIGINT,
    PRIMARY KEY (event_id, compilation_id),
    FOREIGN KEY (event_id) REFERENCES events(id),
    FOREIGN KEY (compilation_id) REFERENCES compilations(id)
);

CREATE TABLE IF NOT EXISTS participation_requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    created_on VARCHAR(50),
    status VARCHAR(50),
    FOREIGN KEY (event_id) REFERENCES events(id),
    FOREIGN KEY (requester_id) REFERENCES users(id),
    PRIMARY KEY (id)
);