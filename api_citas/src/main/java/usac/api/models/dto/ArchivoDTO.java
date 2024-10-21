/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package usac.api.models.dto;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 *
 * @author luid
 */
@Component
public class ArchivoDTO {

    private HttpHeaders headers;
    private byte[] archivo;

    public ArchivoDTO(HttpHeaders headers, byte[] archivo) {
        this.headers = headers;
        this.archivo = archivo;
    }

    public ArchivoDTO() {
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

}
