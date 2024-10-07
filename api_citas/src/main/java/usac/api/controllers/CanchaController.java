package usac.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.models.Cancha;
import usac.api.services.CanchaService;
import usac.api.tools.transformers.ApiBaseTransformer;


@RestController
@RequestMapping("/api/cancha")
public class CanchaController {
    @Autowired
    private CanchaService canchaService;

    @GetMapping("/public/canchas")
    public ResponseEntity<?> getCanchas() {
        try {
            List<Cancha> data = canchaService.getCanchas();
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @GetMapping("/public/cancha/{id}")
    public ResponseEntity<?> getCanchaById(@PathVariable Long id) {
        try {
            Cancha data = canchaService.getCanchaById(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @DeleteMapping("/private/cancha/{id}")
    public ResponseEntity<?> deleteCanchaById(@PathVariable Long id) {
        try {
            canchaService.deleteCanchaById(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", null, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
}
