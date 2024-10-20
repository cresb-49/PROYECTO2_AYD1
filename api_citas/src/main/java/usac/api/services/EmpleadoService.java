package usac.api.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.Dia;
import usac.api.models.Empleado;
import usac.api.models.HorarioEmpleado;
import usac.api.models.Usuario;
import usac.api.repositories.EmpleadoRepository;
import usac.api.repositories.HorarioEmpleadoRepository;
import usac.api.repositories.UsuarioRepository;

@Service
public class EmpleadoService extends usac.api.services.Service {
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private HorarioEmpleadoRepository horarioEmpleadoRepository;

    @Transactional(rollbackOn = Exception.class)
    public Empleado createEmpleado(Empleado empleado) throws Exception {
        // Validamos el modelo
        this.validarModelo(empleado);
        // Creamos el empleado
        Empleado empleadoCreado = empleadoRepository.save(empleado);
        if (empleadoCreado != null && empleadoCreado.getId() != null) {
            return empleadoCreado;
        }
        throw new Exception("No se pudo crear el empleado");
    }

    public List<Empleado> getEmpleados() {
        List<Empleado> empleados = this.ignorarEliminados(empleadoRepository.findAll());
        empleados = this.ignorarEliminados(empleados);
        return empleados;
    }

    @Transactional(rollbackOn = Exception.class)
    public void eliminarEmpleado(Long id) throws Exception {
        Empleado empleado = this.empleadoRepository.findById(id).orElse(null);
        if (empleado == null) {
            throw new Exception("No se encontro el empleado");
        }
        Usuario usuario = empleado.getUsuario();
        if (usuario == null) {
            throw new Exception("No se encontro el usuario asociado al empleado");
        }
        empleadoRepository.deleteEmpleadoById(empleado.getId());
        usuarioRepository.deleteUsuarioById(usuario.getId());
    }

    public Empleado getEmpleadoById(Long id) {
        Empleado empleado = this.empleadoRepository.findById(id).orElse(null);
        return empleado;
    }

    public HorarioEmpleado obtenerHorarioDiaEmpleado(Dia dia, Empleado empleado) {
        return horarioEmpleadoRepository.findByDiaAndEmpleado(dia, empleado);
    }

    public List<Empleado> getEmpleadosByRolId(Long rolId) {
        return empleadoRepository.findEmpleadosByRolId(rolId);
    }
}
