package usac.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import usac.api.filters.JwtRequestFilter;
import usac.api.services.authentification.AuthenticationService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de pruebas para PathsSecurityConfig usando @SpringBootTest.
 */
@SpringBootTest
public class PathsSecurityConfigTest {

    @Autowired
    private PathsSecurityConfig pathsSecurityConfig;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Configuramos MockMvc con el contexto de la aplicación completa
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    /**
     * Test para verificar que el PasswordEncoder no sea nulo.
     */
    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = pathsSecurityConfig.passwordEncoder();
        assertNotNull(encoder, "El PasswordEncoder no debe ser nulo.");
    }

    /**
     * Test para verificar la configuración de seguridad HTTP.
     */
    @Test
    @WithMockUser(roles = "USUARIO")
    void testHttpSecurityWithoutJwtFilter() throws Exception {
        mockMvc.perform(get("/api/usuario/private/getUsuarios"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test para verificar la configuración de AuthenticationManagerBuilder.
     */
    @Test
    void testConfigureAuthenticationManagerBuilder() throws Exception {
        pathsSecurityConfig.configure(authenticationManagerBuilder);
        assertNotNull(authenticationManagerBuilder, "El AuthenticationManagerBuilder no debe ser nulo.");
    }

    /**
     * Test para verificar que el AuthenticationManager no sea nulo.
     */
    @Test
    void testAuthenticationManagerBean() throws Exception {
        assertNotNull(authenticationManager, "El AuthenticationManager no debe ser nulo.");
    }
}
