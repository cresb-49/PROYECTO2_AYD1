package usac.api.services;

import org.junit.jupiter.api.Test;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class B64ServiceTest {

    private final B64Service b64Service = new B64Service();

    @Test
    void testHasExtensionConExtension() {
        String base64Image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAA";
        assertTrue(b64Service.hasExtension(base64Image));
    }

    @Test
    void testHasExtensionSinExtension() {
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAABQAAA";
        assertFalse(b64Service.hasExtension(base64Image));
    }

    @Test
    void testAddExtension() {
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAABQAAA";
        String result = b64Service.addExtension(base64Image);
        assertTrue(result.startsWith("data:image/png;base64,"));
        assertEquals("data:image/png;base64," + base64Image, result);
    }

    @Test
    void testBase64ToByteArrayConPrefijo() {
        String base64Image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAA";
        byte[] result = B64Service.base64ToByteArray(base64Image);
        byte[] expectedBytes = Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAABQAAA");
        assertArrayEquals(expectedBytes, result);
    }

    @Test
    void testBase64ToByteArraySinPrefijo() {
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAABQAAA";
        byte[] result = B64Service.base64ToByteArray(base64Image);
        byte[] expectedBytes = Base64.getDecoder().decode(base64Image);
        assertArrayEquals(expectedBytes, result);
    }
}
