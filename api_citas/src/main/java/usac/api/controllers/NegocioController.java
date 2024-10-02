package usac.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.models.Negocio;
import usac.api.models.dto.NegocioDTO;
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
    public ResponseEntity<?> getMethodName() {
        try {
            Negocio data = negocioService.obtenerNegocio();
            boolean hasExtension =b64Service.hasExtension(data.getLogo()); 
            String logo = hasExtension ? data.getLogo() : b64Service.addExtension(data.getLogo());
            NegocioDTO negocioDTO = new NegocioDTO(data.getId(), data.getNombre(), logo);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", negocioDTO, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @PatchMapping("/private/negocio")
    public ResponseEntity<?> updateMethodName(@RequestBody Negocio negocio) {
        try {
            Negocio data = negocioService.actualizarNegocio(negocio);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
    
}
