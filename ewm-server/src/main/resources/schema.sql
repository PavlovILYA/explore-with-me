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
    annotation VARCHAR(2000) UNIQUE NOT NULL, --
    category_id BIGINT NOT NULL, --
    created_on TIMESTAMP NOT NULL,
    description VARCHAR(7000), --
    event_date TIMESTAMP NOT NULL, --
    initiator_id BIGINT NOT NULL,
    location_latitude DOUBLE PRECISION NOT NULL, --
    location_longitude DOUBLE PRECISION NOT NULL, --
    paid BOOLEAN NOT NULL, --
    participant_limit INT DEFAULT 0, --
    published_on TIMESTAMP,
    request_moderation BOOLEAN DEFAULT TRUE, --
    state VARCHAR(50) NOT NULL,
    title VARCHAR(120) UNIQUE NOT NULL, --
    --     views
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    FOREIGN KEY (initiator_id) REFERENCES users(id) ON DELETE CASCADE
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
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (compilation_id) REFERENCES compilations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participation_requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    created_on TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE
);