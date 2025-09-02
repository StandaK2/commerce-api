package cz.rohlik.commerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * Utility methods for testing. Provides MockMvc helpers and UUID generation for predictable test
 * data.
 */
public final class TestUtils {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper().registerModule(new JavaTimeModule());

    private TestUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Adds Bearer authentication header to MockMvc request.
     *
     * @param request The MockMvc request builder
     * @param accessToken The access token
     * @return The request builder with auth header
     */
    public static MockHttpServletRequestBuilder addBearerAuthHeader(
            MockHttpServletRequestBuilder request, String accessToken) {
        return request.header("Authorization", "Bearer " + accessToken);
    }

    /**
     * Adds Accept header to MockMvc request.
     *
     * @param request The MockMvc request builder
     * @param value The Accept header value
     * @return The request builder with Accept header
     */
    public static MockHttpServletRequestBuilder addAcceptHeader(
            MockHttpServletRequestBuilder request, String value) {
        return request.header("Accept", value);
    }

    /**
     * Adds JSON content to MockMvc request.
     *
     * @param request The MockMvc request builder
     * @param content The JSON content as string
     * @return The request builder with JSON content
     */
    public static MockHttpServletRequestBuilder addJsonContent(
            MockHttpServletRequestBuilder request, String content) {
        return request.contentType(MediaType.APPLICATION_JSON).content(content);
    }

    /**
     * Adds JSON content to MockMvc request by serializing an object.
     *
     * @param request The MockMvc request builder
     * @param object The object to serialize as JSON
     * @return The request builder with JSON content
     * @throws RuntimeException if serialization fails
     */
    public static MockHttpServletRequestBuilder addJsonContent(
            MockHttpServletRequestBuilder request, Object object) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(object);
            return addJsonContent(request, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Creates a UUID from an integer for predictable test data.
     *
     * @param number The number to convert (must be non-negative)
     * @return UUID created from the number
     */
    public static UUID createUuidFromNumber(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative");
        }

        String uuidString = String.format("00000000-0000-0000-0000-%012d", (long) number);
        return UUID.fromString(uuidString);
    }
}
