package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Empleado;
import usac.api.services.EmpleadoService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EmpleadoControllerTest {

    @InjectMocks
    private EmpleadoController empleadoController;

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private Empleado mockEmpleado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmpleados() {
        // Setup
        List<Empleado> empleados = new ArrayList<>();
        empleados.add(mockEmpleado);
        when(empleadoService.getEmpleados()).thenReturn(empleados);

        // Execute
        var response = empleadoController.getEmpleados();

        // Verify
        assertNotNull(response);
        verify(empleadoService, times(1)).getEmpleados();
    }

    @Test
    void testGetEmpleadosError() {
        // Simular una excepción al llamar a getEmpleados
        when(empleadoService.getEmpleados()).thenThrow(new RuntimeException("Error en el servicio"));

        // Execute
        var response = empleadoController.getEmpleados();

        // Verify
        assertNotNull(response);
        verify(empleadoService, times(1)).getEmpleados(); // Verifica que se llame al método
    }

    @Test
    void testGetEmpleadoById() {
        try {
            // Setup
            Long id = 1L;
            when(empleadoService.getEmpleadoById(id)).thenReturn(mockEmpleado);
            
            // Execute
            var response = empleadoController.getEmpleadoById(id);
            
            // Verify
            assertNotNull(response);
            verify(empleadoService, times(1)).getEmpleadoById(id);
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetEmpleadoByIdError() {
        try {
            // Simular una excepción al llamar a getEmpleadoById
            Long id = 1L;
            when(empleadoService.getEmpleadoById(id)).thenThrow(new RuntimeException("Error en el servicio"));
            
            // Execute
            var response = empleadoController.getEmpleadoById(id);
            
            // Verify
            assertNotNull(response);
            verify(empleadoService, times(1)).getEmpleadoById(id); // Verifica que se llame al método
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testDeleteEmpleado() {
        try {
            // Setup
            Long id = 1L;
            
            // Execute
            var response = empleadoController.deleteEmpleado(id);
            
            // Verify
            assertNotNull(response);
            verify(empleadoService, times(1)).eliminarEmpleado(id);
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testDeleteEmpleadoError() {
        try {
            // Simular una excepción al llamar a eliminarEmpleado
            Long id = 1L;
            doThrow(new RuntimeException("Error en el servicio")).when(empleadoService).eliminarEmpleado(id);
            
            // Execute
            var response = empleadoController.deleteEmpleado(id);
            
            // Verify
            assertNotNull(response);
            verify(empleadoService, times(1)).eliminarEmpleado(id); // Verifica que se llame al método
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
