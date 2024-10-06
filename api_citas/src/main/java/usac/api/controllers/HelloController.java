package usac.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.models.Dia;
import usac.api.services.DiaService;
import usac.api.tools.transformers.ApiBaseTransformer;


@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @Autowired
    private DiaService diaService;

    @GetMapping("/public/api")
    public ResponseEntity<?> getDias() {
        try {
            return new ApiBaseTransformer(HttpStatus.OK, "OK", "Hello from API", null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
    
}
