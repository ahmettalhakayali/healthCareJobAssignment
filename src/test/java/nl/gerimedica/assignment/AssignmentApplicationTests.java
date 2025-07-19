package nl.gerimedica.assignment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic application context test to ensure the Spring Boot application starts correctly.
 * This test verifies that all beans are properly configured and the application context loads.
 */
@SpringBootTest
@ActiveProfiles("test")
class AssignmentApplicationTests {

    @Test
    void contextLoads() {
        // This test will fail if the application context cannot start
        // due to configuration issues, missing beans, or other startup problems
        assertTrue(true, "Application context loaded successfully");
    }
}
