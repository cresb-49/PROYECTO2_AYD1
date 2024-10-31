package usac.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.mockito.Mockito.*;

class ServletInitializerTest {

    @Test
    void testConfigure() {
        // Arrange
        ServletInitializer initializer = new ServletInitializer();
        SpringApplicationBuilder mockBuilder = mock(SpringApplicationBuilder.class);

        // Act
        initializer.configure(mockBuilder);

        // Assert
        verify(mockBuilder, times(1)).sources(ApIecommerceApplication.class);
    }
}
