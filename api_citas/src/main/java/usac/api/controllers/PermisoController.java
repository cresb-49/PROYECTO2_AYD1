package usac.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.models.Permiso;
import usac.api.services.PermisoService;
import usac.api.tools.transformers.ApiBaseTransformer;

@RestController
@RequestMapping("/api/permiso")
public class PermisoController {
    @Autowired
    private PermisoService permisoService;

    @GetMapping("/private/restricted/permisos")
    public ResponseEntity<?> getPermisos() {
        try {
            List<Permiso> permisos = permisoService.getAllPermisos();
            return new ApiBaseTransformer(HttpStatus.OK, "OK", permisos, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
}
