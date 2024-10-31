package usac.api.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;

import usac.api.models.Dia;
import usac.api.models.Rol;
import usac.api.models.Usuario;
import usac.api.repositories.DiaRepository;
import usac.api.repositories.PermisoRepository;
import usac.api.repositories.RolRepository;
import usac.api.repositories.UsuarioRepository;
import usac.api.services.*;

public class SeedersConfigTest {

    @InjectMocks
    private SeedersConfig seedersConfig;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private DiaRepository diaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PermisoRepository permisoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private RolService rolService;

    @Mock
    private NegocioService negocioService;

    @Mock
    private DiaService diaService;

    @Mock
    private CanchaService canchaService;

    @Mock
    private ServicioService servicioService;

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private B64Service b64Service;

    @Mock
    private ContextRefreshedEvent event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnApplicationEvent_WhenUsuariosExist_DoNotRunSeeders() {
        when(usuarioRepository.count()).thenReturn(1L);

        seedersConfig.onApplicationEvent(event);

        verify(usuarioRepository, times(1)).count();
        verifyNoInteractions(diaRepository, rolRepository, permisoRepository);
    }

    @Test
    void testOnApplicationEvent_WhenUsuariosNotExist_RunSeeders() throws Exception {
        when(usuarioRepository.count()).thenReturn(0L);

        // Mock roles
        when(rolRepository.findOneByNombre(any(String.class))).thenReturn(Optional.empty());
        when(rolRepository.save(any(Rol.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock days
        when(diaRepository.findOneByNombreIgnoreCase(any(String.class))).thenReturn(Optional.empty());
        when(diaRepository.save(any(Dia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock usuario creation
        when(usuarioService.crearAdministrador(any(Usuario.class))).thenReturn(null);
        when(usuarioService.crearUsuarioCliente(any(Usuario.class))).thenReturn(null);

        // Run the seeder
        seedersConfig.onApplicationEvent(event);

        // Verify day creation
        verify(diaRepository, times(7)).save(any(Dia.class));

        // Verify role creation
        verify(rolRepository, times(8)).save(any(Rol.class));

        // Verify user creation calls
        verify(usuarioService, times(2)).crearAdministrador(any(Usuario.class));
        verify(usuarioService, times(5)).crearUsuarioCliente(any(Usuario.class));
    }

    @Test
    void testInsertarDia_DiaAlreadyExists() throws Exception {
        Dia existingDia = new Dia("Lunes");
        when(diaRepository.findOneByNombreIgnoreCase("Lunes")).thenReturn(Optional.of(existingDia));

        Dia result = seedersConfig.insertarDia(new Dia("Lunes"));

        assertNotNull(result);
        assertEquals(existingDia, result);
        verify(diaRepository, never()).save(any(Dia.class));
    }

    @Test
    void testInsertarDia_NewDia() throws Exception {
        Dia newDia = new Dia("Lunes");
        when(diaRepository.findOneByNombreIgnoreCase("Lunes")).thenReturn(Optional.empty());
        when(diaRepository.save(any(Dia.class))).thenReturn(newDia);

        Dia result = seedersConfig.insertarDia(newDia);

        assertNotNull(result);
        assertEquals(newDia, result);
        verify(diaRepository, times(1)).save(newDia);
    }

    @Test
    void testInsertarRol_RolAlreadyExists() throws Exception {
        Rol existingRol = new Rol("ADMIN");
        when(rolRepository.findOneByNombre("ADMIN")).thenReturn(Optional.of(existingRol));

        Rol result = seedersConfig.insertarRol(new Rol("ADMIN"));

        assertNotNull(result);
        assertEquals(existingRol, result);
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void testInsertarRol_NewRol() throws Exception {
        Rol newRol = new Rol("ADMIN");
        when(rolRepository.findOneByNombre("ADMIN")).thenReturn(Optional.empty());
        when(rolRepository.save(any(Rol.class))).thenReturn(newRol);

        Rol result = seedersConfig.insertarRol(newRol);

        assertNotNull(result);
        assertEquals(newRol, result);
        verify(rolRepository, times(1)).save(newRol);
    }
}
