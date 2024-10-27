package usac.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.models.HorarioNegocio;
import usac.api.models.Negocio;
import usac.api.models.dto.NegocioPublicDTO;
import usac.api.models.dto.NegocioUpdateDTO;
import usac.api.services.B64Service;
import usac.api.services.NegocioService;
import usac.api.tools.transformers.ApiBaseTransformer;

@RestController
@RequestMapping("/api/negocio")
public class NegocioController {
    @Autowired
    private NegocioService negocioService;
    @Autowired
    private B64Service b64Service;

    @GetMapping("/public/negocio")
    public ResponseEntity<?> infoNegocio() {
        try {
            // Negocio data = negocioService.obtenerNegocio();
            // boolean hasExtension = b64Service.hasExtension(data.getLogo());
            // String logo = hasExtension ? data.getLogo() :
            // b64Service.addExtension(data.getLogo());
            // NegocioPublicDTO negocioDTO = new NegocioPublicDTO(data.getId(),
            // data.getNombre(), logo);
            Negocio data = negocioService.obtenerNegocio();
            boolean hasExtension = b64Service.hasExtension(data.getLogo());
            String logo = hasExtension ? data.getLogo() : b64Service.addExtension(data.getLogo());
            data.setLogo(logo);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @GetMapping("/private/negocio") // TODO: Debe ser ruta privada
    public ResponseEntity<?> getNegocio() {
        try {
            Negocio data = negocioService.obtenerNegocio();
            boolean hasExtension = b64Service.hasExtension(data.getLogo());
            String logo = hasExtension ? data.getLogo() : b64Service.addExtension(data.getLogo());
            data.setLogo(logo);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @PatchMapping("/private/negocio")
    public ResponseEntity<?> actualizarNegocio(@RequestBody NegocioUpdateDTO negocioDTO) {
        try {
            Negocio negocio = negocioDTO.getNegocio();
            List<HorarioNegocio> horarios = negocioDTO.getHorarios();
            // Actualiza el negocio y sus horarios
            Negocio data = negocioService.actualizarNegocio(negocio, horarios);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            // Imprimir el trace del error
            ex.printStackTrace();
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

}
