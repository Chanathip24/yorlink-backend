CREATE TABLE short_urls (
        id SERIAL PRIMARY KEY,
        alias VARCHAR(50) UNIQUE NOT NULL,
        original_url TEXT NOT NULL,

        type VARCHAR(20) NOT NULL DEFAULT 'normal'
            CHECK (type IN ('normal', 'scheduled', 'expiring', 'protected')),

        activation_date DATE NULL,
        expiration_date DATE NULL,
        maximum_clicks INT NULL,
        current_clicks INT DEFAULT 0,

        password_hash TEXT NULL,
        password_hint TEXT NULL,

        is_custom_alias BOOLEAN DEFAULT FALSE,

        created_at DATE DEFAULT CURRENT_DATE,
        updated_at DATE DEFAULT CURRENT_DATE
);