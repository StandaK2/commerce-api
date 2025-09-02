# Database Migrations

This directory contains Flyway database migration scripts following the versioned migration pattern.

## Naming Convention

- **Versioned Migrations**: `V{VERSION}__{DESCRIPTION}.sql`
  - Example: `V1__create_product_table.sql`
  - Example: `V2__create_order_table.sql`

## Constraint Naming

Use **unique identifiers** for database constraints:

### Format: `{20-char-uuid}_{suffix}`

| Type | Suffix | Example |
|------|--------|---------|
| Foreign Key | `_fk` | `b2c3d4e5f6g7h8i9j0k1_fk` |
| Unique | `_ui` | `c3d4e5f6g7h8i9j0k1l2_ui` |
| Index | `_ix` | `d4e5f6g7h8i9j0k1l2m3_ix` |

```
-- Foreign Key
CONSTRAINT b2c3d4e5f6g7h8i9j0k1_fk FOREIGN KEY (product_id) REFERENCES product(id),

-- Unique Constraint
CONSTRAINT c3d4e5f6g7h8i9j0k1l2_ui UNIQUE (email),

-- Index
CREATE INDEX d4e5f6g7h8i9j0k1l2m3_ix ON product (name);
```

## Best Practices

- **Never modify existing migrations** - create new ones instead
- **Test migrations** both up and down if rollback is needed
- **Use explicit data types** for cross-database compatibility
- **Add indexes** for performance-critical queries
- **Foreign keys** for referential integrity
- **Use unique constraint names** for all database objects
