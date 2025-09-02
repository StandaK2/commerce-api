-- Create product table for commerce API

CREATE TABLE product (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    price NUMERIC NOT NULL,
    stock_quantity INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX a5dd6699d4be4e29aead_ix ON product (name);
CREATE INDEX b6ee7788e5cf5f30bbfe_ix ON product (stock_quantity);
CREATE INDEX d466f666717713773366_ix ON product (deleted_at);
