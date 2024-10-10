package usac.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.models.Servicio;
import usac.api.services.ServicioService;
import usac.api.tools.transformers.ApiBaseTransformer;



@RestController
@RequestMapping("/api/servicio")
public class ServicioController {
    @Autowired
    private ServicioService servicioService;

    @GetMapping("public/servicios")
    public ResponseEntity<?> getAllServicios() {
        try {
            Object data = servicioService.getServicios();
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @GetMapping("public/servicio/{id}")
    public ResponseEntity<?> getServicioById(Long id) {
        try {
            Object data = servicioService.getServicioById(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @GetMapping("public/servicios/nombre/{nombre}")
    public ResponseEntity<?> getServiciosLikeNombre(String nombre) {
        try {
            Object data = servicioService.getServiciosLikeNombre(nombre);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @DeleteMapping("private/servicio/{id}")
    public ResponseEntity<?> deleteServicio(Long id) {
        try {
            servicioService.eliminarServicio(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", null, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
    
    @PatchMapping("private/servicio")
    public ResponseEntity<?> updateServicio(Servicio servicio) {
        try {
            servicioService.actualizarServicio(servicio);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", null, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @PostMapping("private/servicio")
    public ResponseEntity<?> createServicio(Servicio servicio) {
        try {
            servicioService.crearServicio(servicio);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", null, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
}
