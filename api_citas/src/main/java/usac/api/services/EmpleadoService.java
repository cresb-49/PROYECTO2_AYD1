package usac.api.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.Empleado;
import usac.api.repositories.EmpleadoRepository;

@Service
public class EmpleadoService extends usac.api.services.Service {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Transactional(rollbackOn = Exception.class)
    public Empleado createEmpleado(Empleado empleado) throws Exception {
        //Validamos el modelo
        this.validarModelo(empleado);
        //Creamos el empleado
        Empleado empleadoCreado = empleadoRepository.save(empleado);
        if (empleadoCreado != null && empleadoCreado.getId() != null) {
            return empleadoCreado;
        }
        throw new Exception("No se pudo crear el empleado");
    }

    public List<Empleado> getEmpleados() {
        List<Empleado> empleados = this.ignorarEliminados(empleadoRepository.findAll());
        return empleados;
    }
}
