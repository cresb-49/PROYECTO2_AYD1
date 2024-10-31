package usac.api.models.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import usac.api.models.HorarioEmpleado;
import usac.api.models.Rol;
import usac.api.models.Usuario;
import usac.api.models.request.UpdateEmpleadoRequest;

public class UpdateEmpleadoRequestTest {

    @InjectMocks
    private UpdateEmpleadoRequest updateEmpleadoRequest;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Prueba para verificar que el método setId asigna el valor correctamente.
     */
    @Test
    void testSetId() {
        updateEmpleadoRequest.setId(1L);
        assertEquals(1L, updateEmpleadoRequest.getId());
    }

    /**
     * Prueba para verificar que el método setUsuario asigna el valor
     * correctamente.
     */
    @Test
    void testSetUsuario() {
        Usuario usuario = new Usuario();
        updateEmpleadoRequest.setUsuario(usuario);
        assertEquals(usuario, updateEmpleadoRequest.getUsuario());
    }

    /**
     * Prueba para verificar que el método setRol asigna el valor correctamente.
     */
    @Test
    void testSetRol() {
        Rol rol = new Rol();
        updateEmpleadoRequest.setRol(rol);
        assertEquals(rol, updateEmpleadoRequest.getRol());
    }

    /**
     * Prueba para verificar que el método setHorarios asigna el valor
     * correctamente.
     */
    @Test
    void testSetHorarios() {
        List<HorarioEmpleado> horarios = List.of(new HorarioEmpleado());
        updateEmpleadoRequest.setHorarios(horarios);
        assertEquals(horarios, updateEmpleadoRequest.getHorarios());
    }

    /**
     * Prueba para verificar que la validación falla cuando el id es nulo.
     */
    @Test
    void testIdNoPuedeSerNulo() {
        updateEmpleadoRequest.setId(null);
        Set<ConstraintViolation<UpdateEmpleadoRequest>> violations = validator.validate(updateEmpleadoRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El id del empleado no puede ser nulo")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el usuario es nulo.
     */
    @Test
    void testUsuarioNoPuedeSerNulo() {
        updateEmpleadoRequest.setUsuario(null);
        Set<ConstraintViolation<UpdateEmpleadoRequest>> violations = validator.validate(updateEmpleadoRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El usuario no puede ser nulo")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el rol es nulo.
     */
    @Test
    void testRolNoPuedeSerNulo() {
        updateEmpleadoRequest.setRol(null);
        Set<ConstraintViolation<UpdateEmpleadoRequest>> violations = validator.validate(updateEmpleadoRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El rol no puede ser nulo")));
    }

    /**
     * Prueba para verificar que la validación falla cuando los horarios son
     * nulos.
     */
    @Test
    void testHorariosNoPuedeSerNulo() {
        updateEmpleadoRequest.setHorarios(null);
        Set<ConstraintViolation<UpdateEmpleadoRequest>> violations = validator.validate(updateEmpleadoRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Los horarios del empleado no puede ser nulos.")));
    }

    /**
     * Prueba para verificar que los valores válidos pasan las validaciones.
     */
    @Test
    void testValoresValidos() {
        updateEmpleadoRequest.setId(1L);
        updateEmpleadoRequest.setUsuario(new Usuario());
        updateEmpleadoRequest.setRol(new Rol());
        updateEmpleadoRequest.setHorarios(List.of(new HorarioEmpleado()));

        Set<ConstraintViolation<UpdateEmpleadoRequest>> violations = validator.validate(updateEmpleadoRequest);
        assertTrue(violations.isEmpty());
    }
}
