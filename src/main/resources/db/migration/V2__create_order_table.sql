-- Create order table for commerce API

CREATE TABLE "order" (
    id UUID PRIMARY KEY,
    status TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX c7ff8899f6d06041ccef_ix ON "order" (status);
CREATE INDEX e9110a11182262e3ee11_ix ON "order" (status, updated_at);
