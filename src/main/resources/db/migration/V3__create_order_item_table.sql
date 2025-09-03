-- Create order_item table for commerce API

CREATE TABLE order_item (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE order_item ADD CONSTRAINT f0221b22293373f4ff22_fk
    FOREIGN KEY (order_id) REFERENCES "order"(id);

ALTER TABLE order_item ADD CONSTRAINT a133c3334a4484055033_fk
    FOREIGN KEY (product_id) REFERENCES product(id);

CREATE INDEX b244d44459559155144_ix ON order_item (order_id);
CREATE INDEX c355e555606602662255_ix ON order_item (product_id);
CREATE UNIQUE INDEX "d466f66670770266633_ui" ON order_item (order_id, product_id);
