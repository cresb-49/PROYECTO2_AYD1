/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import usac.api.models.Rol;
import usac.api.repositories.RolRepository;

/**
 *
 * @author Luis Monterroso
 */
public class RolServiceTest {

    @InjectMocks
    private RolService rolService;

    @Mock
    private RolRepository rolRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba exitosa para buscar un rol por nombre.
     */
    @Test
    void testGetRolByNombre_Success() throws Exception {
        // Simulación de un rol existente
        Rol rol = new Rol();
        rol.setNombre("ADMIN");

        // Simulamos la búsqueda por nombre
        when(rolRepository.findOneByNombre("ADMIN")).thenReturn(Optional.of(rol));

        // Llamamos al método getRolByNombre
        Rol result = rolService.getRolByNombre("ADMIN");

        // Verificaciones
        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre());
        verify(rolRepository, times(1)).findOneByNombre("ADMIN");
    }

    /**
     * Prueba que lanza excepción cuando el rol no es encontrado.
     */
    @Test
    void testGetRolByNombre_RolNoEncontrado() {
        // Simular que no se encuentra el rol
        when(rolRepository.findOneByNombre("USUARIO")).thenReturn(Optional.empty());

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            rolService.getRolByNombre("USUARIO");
        });

        // Verificar el mensaje de la excepción
        assertEquals("Rol no encontrado.", exception.getMessage());
        verify(rolRepository, times(1)).findOneByNombre("USUARIO");
    }

    /**
     * Prueba que verifica que solo se llama una vez al repositorio cuando se
     * busca un rol.
     */
    @Test
    void testGetRolByNombre_RepositoryCallOnce() throws Exception {
        // Simulación de un rol existente
        Rol rol = new Rol();
        rol.setNombre("USER");

        // Simulamos la búsqueda por nombre
        when(rolRepository.findOneByNombre("USER")).thenReturn(Optional.of(rol));

        // Llamar al método y verificar que el repositorio se llama solo una vez
        rolService.getRolByNombre("USER");

        verify(rolRepository, times(1)).findOneByNombre("USER");
    }
}
