package cz.rohlik.commerce.application.common.command;

import java.util.UUID;

/**
 * Standard result type for commands that create or identify entities.
 *
 * @param id The UUID of the created or referenced entity
 */
public record IdResult(UUID id) {

    /**
     * Creates an IdResult with the given UUID.
     *
     * @param id The entity ID
     * @return IdResult instance
     */
    public static IdResult of(UUID id) {
        return new IdResult(id);
    }
}
