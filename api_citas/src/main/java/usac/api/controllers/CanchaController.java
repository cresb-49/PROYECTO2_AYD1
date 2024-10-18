package usac.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.models.Cancha;
import usac.api.models.HorarioCancha;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.request.CanchaRequest;
import usac.api.models.request.ReservacionCanchaRequest;
import usac.api.services.CanchaService;
import usac.api.services.ReservaService;
import usac.api.tools.transformers.ApiBaseTransformer;

@RestController
@RequestMapping("/api/cancha")
public class CanchaController {

    @Autowired
    private CanchaService canchaService;
    @Autowired
    private ReservaService reservaService;

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

    @PostMapping("/private/cancha")
    public ResponseEntity<?> createCancha(@RequestBody CanchaRequest canchaRequest) {
        try {
            Cancha cancha = canchaRequest.getCancha();
            List<HorarioCancha> horarios = canchaRequest.getHorarios();
            Cancha data = canchaService.crearCancha(cancha, horarios);
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

    @PatchMapping("/private/cancha")
    public ResponseEntity<?> updateCancha(@RequestBody CanchaRequest canchaRequest) {
        try {
            System.out.println(canchaRequest);
            Cancha cancha = canchaRequest.getCancha();
            List<HorarioCancha> horarios = canchaRequest.getHorarios();
            Cancha data = canchaService.actualizarCancha(cancha, horarios);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @PatchMapping("/cliente/reservarCancha")
    public ResponseEntity<?> reservarCancha(@RequestBody ReservacionCanchaRequest reservacionCanchaRequest) {
        try {
            ArchivoDTO data = reservaService.reservaCancha(reservacionCanchaRequest);
            // Devolvemos el PDF en la respuesta HTTP
            return new ResponseEntity<>(data.getArchivo(), data.getHeaders(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
}
