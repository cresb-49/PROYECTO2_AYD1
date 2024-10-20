package usac.api.services;

import java.util.Base64;

@org.springframework.stereotype.Service
public class B64Service extends Service {

    // En esta funcion validamos que la cadena de string tenga el inicio
    // data:image/png;base64, para que el navegador pueda interpretar la imagen
    public boolean hasExtension(String base64) {
        return base64.startsWith("data:image");
    }

    // En esta funcion agregamos el inicio data:image/png;base64, a la cadena de
    // string
    public String addExtension(String base64) {
        return "data:image/png;base64," + base64;
    }

    /**
     * Convierte una cadena Base64 a un arreglo de bytes.
     *
     * @param base64Image La cadena en formato Base64 que representa la imagen.
     * @return Un arreglo de bytes que representa la imagen.
     */
    public static byte[] base64ToByteArray(String base64Image) {
        // Eliminar el prefijo 'data:image/*;base64,' si es necesario
        if (base64Image.contains(",")) {
            base64Image = base64Image.split(",")[1];
        }
        // Decodificar la cadena Base64
        return Base64.getDecoder().decode(base64Image);
    }
}
