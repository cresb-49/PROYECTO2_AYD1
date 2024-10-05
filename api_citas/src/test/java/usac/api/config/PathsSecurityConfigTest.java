package usac.api.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.junit.jupiter.api.extension.ExtendWith;
import usac.api.filters.JwtRequestFilter;
import usac.api.services.authentification.AuthenticationService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Clase de pruebas unitarias para PathsSecurityConfig.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PathsSecurityConfigTest {

    @InjectMocks
    private PathsSecurityConfig pathsSecurityConfig;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Test para verificar que el PasswordEncoder no sea nulo.
     */
    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = pathsSecurityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    /**
     * Test para verificar la configuración de seguridad HTTP.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testConfigureHttpSecurity() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class);

        // Invocamos el método protegido configure(HttpSecurity)
        pathsSecurityConfig.configure(httpSecurity);

        // Verificamos que el filtro JWT fue agregado correctamente
        verify(httpSecurity).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Test para verificar la configuración de AuthenticationManagerBuilder.
     */
    @Test
    void testConfigureAuthenticationManagerBuilder() throws Exception {
        pathsSecurityConfig.configure(authenticationManagerBuilder);

        // Verificamos que el AuthenticationManagerBuilder utiliza el servicio de autenticación
        verify(authenticationManagerBuilder).userDetailsService(authenticationService);
    }

    /**
     * Test para verificar que el AuthenticationManager no sea nulo.
     */
    @Test
    void testAuthenticationManagerBean() throws Exception {
        assertNotNull(authenticationManager);
    }
}
