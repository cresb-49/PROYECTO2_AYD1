package usac.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.services.EmpleadoService;
import usac.api.tools.transformers.ApiBaseTransformer;


@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/private/empleados")
    public ResponseEntity<?> getEmpleados() {
        try {    
            return new ApiBaseTransformer(HttpStatus.OK, "OK", empleadoService.getEmpleados(), null, null).sendResponse();  
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
    
}
