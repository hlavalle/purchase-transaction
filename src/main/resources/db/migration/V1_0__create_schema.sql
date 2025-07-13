CREATE TABLE transaction (
    id UUID PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    amount NUMERIC(17,2),
    timestamp TIMESTAMP WITH TIME ZONE
)