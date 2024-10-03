package usac.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.Dia;
import usac.api.repositories.DiaRepository;

@Service
public class DiaService extends usac.api.services.Service {
    @Autowired
    private DiaRepository diaRepository;
    
    public Dia getDiaByNombre(String nombre) throws Exception {
        Dia dia = this.diaRepository.findOneByNombre(nombre).orElse(null);
        if (dia == null) {
            throw new Exception("Dia no encontrado.");
        }
        return dia;
    }

    public Dia getDiaById(Long id) throws Exception {
        Dia dia = this.diaRepository.findById(id).orElse(null);
        if (dia == null) {
            throw new Exception("Dia no encontrado.");
        }
        return dia;
    }

    public List<Dia> getDias() {
        return (List<Dia>) this.diaRepository.findAll();
    }

    public Dia insertarDia(Dia dia) throws Exception {
        this.validarModelo(dia);
        return this.diaRepository.save(dia);
    }
}
