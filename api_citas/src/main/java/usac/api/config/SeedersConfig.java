/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import usac.api.models.Dia;
import usac.api.models.Negocio;
import usac.api.models.Rol;
import usac.api.models.Usuario;
import usac.api.repositories.RolRepository;
import usac.api.services.DiaService;
import usac.api.services.NegocioService;
import usac.api.services.UsuarioService;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class SeedersConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private NegocioService negocioService;
    @Autowired
    private DiaService diaService;

    public Rol insertarRol(Rol rol) throws Exception {
        try {
            Optional<Rol> opRol = this.rolRepository.findOneByNombre(rol.getNombre());
            if (opRol.isPresent()) {
                return opRol.get();
            }
            return this.rolRepository.save(rol);
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    /*

    public EstadoEnvio insertarEstadoEnvio(EstadoEnvio estadoEnvio) throws Exception {
        try {
            Optional<EstadoEnvio> opRol = this.estadoEnvioRepository.findOneByNombre(estadoEnvio.getNombre());
            if (opRol.isPresent()) {
                return opRol.get();
            }
            return this.estadoEnvioRepository.save(estadoEnvio);
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    public Categoria insertarCategoria(Categoria cat) throws Exception {
        try {
            Optional<Categoria> opCat = this.categoriaRepository.findOneByNombre(cat.getNombre());
            if (opCat.isPresent()) {
                return opCat.get();
            }
            return this.categoriaRepository.save(cat);
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    public TiendaConfig insertarTiendaConfig(TiendaConfig config) throws Exception {
        try {
            TiendaConfig conf = this.tiendaConfigReporitory.findFirstByOrderByIdAsc().orElse(null);
            if (conf == null) {
                return this.tiendaConfigReporitory.save(config);
            }
            return conf;

        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    public Permiso insertarPermisoSiNoExiste(Permiso pa) {
        Permiso permiso = this.permisoRepository.findOneByNombre(pa.getNombre()).orElse(null);
        if (permiso == null) {
            return this.permisoRepository.save(
                    pa);
        }
        return permiso;
    }
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            // Creacion de los dias de la semana
            Dia lunes = new Dia("Lunes");
            Dia martes = new Dia("Martes");
            Dia miercoles = new Dia("Miercoles");
            Dia jueves = new Dia("Jueves");
            Dia viernes = new Dia("Viernes");
            Dia sabado = new Dia("Sabado");
            Dia domingo = new Dia("Domingo");
            // Se insertan los dias
            try {
                lunes = this.diaService.insertarDia(lunes);
                martes = this.diaService.insertarDia(martes);
                miercoles = this.diaService.insertarDia(miercoles);
                jueves = this.diaService.insertarDia(jueves);
                viernes = this.diaService.insertarDia(viernes);
                this.diaService.insertarDia(sabado);
                this.diaService.insertarDia(domingo);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            // siders roles
            this.insertarRol(new Rol("CLIENTE"));
            this.insertarRol(new Rol("ADMIN"));
            this.insertarRol(new Rol("EMPLEADO"));

            // Seeder de usuarios del sistema
            Usuario admin = new Usuario("456123789", "3322114455669",
                    "89456123",
                    "elrincondelgamer77@gmail.com",
                    "admin", "admin",
                    "12345");
            
            Usuario admin2 = new Usuario(
                    "777666555", 
                    "1234567891234", 
                    "12345678", 
                    "carlosbpac@gmail.com", 
                    "Carlos", 
                    "Pac", 
                    "12345");

            try {
                this.usuarioService.crearAdministrador(admin);
                this.usuarioService.crearAdministrador(admin2);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            // Se crea el modelo del negocio de la APP
            //Cargamos la imagen que esta en resources/images/logo.svg y la convertimos a base64
            String logo = cargarImagenComoBase64();
            Negocio negocio = new Negocio(logo, "TiendaAyD", false, "2da calle XXX-XXX-XX Quetgo");
            try {
                ArrayList<Dia> horario = new ArrayList<>();
                horario.add(lunes);
                horario.add(martes);
                horario.add(miercoles);
                horario.add(jueves);
                horario.add(viernes);
                this.negocioService.CrearNegocio(negocio,horario);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            
            /*
            Categoria categoria = new Categoria("Hogar");
            this.insertarCategoria(categoria);

            //estado de envio
            EstadoEnvio estadoEnvio = new EstadoEnvio("PENDIENTE");
            this.insertarEstadoEnvio(estadoEnvio);
            EstadoEnvio estadoEnvio2 = new EstadoEnvio("ENTREGADO");
            this.insertarEstadoEnvio(estadoEnvio2);

            // IMAGEN DEFAULT DE LA TIENDA
            byte[] img = getClass().getResourceAsStream("/img/logo.png").readAllBytes();

            TiendaConfig tiendaConfig = new TiendaConfig("TiendaAyD1", img, 12.00,
                    "2da calle XXX-XXX-XX Quetgo",
                    "image/png", 25.0);
            this.insertarTiendaConfig(tiendaConfig);
       
            Usuario user1 = new Usuario("Carlos",
                    "Pac", "carlosbpac@gmail.com",
                    null, "12345",
                    null, null, false);

            Usuario ayudante = new Usuario("Luis",
                    "Monterroso", "luismonteg1@hotmail.com",
                    null, "12345",
                    null, null, false);

           
            // Crear los usuarios, un catch por usuario para que ignore las * excepciones que puedan haber
            
            try {
                this.usuarioService.crearAdministrador(admin);
            } catch (Exception e) {
            }
            try {
                this.usuarioService.crearUsuarioNormal(user1);

            } catch (Exception e) {
            }
            try {
                this.usuarioService.crearAyudante(
                        new UsuarioAyudanteRequest(ayudante, new ArrayList<>()));
            } catch (Exception e) {
            }

            // Creacion de todos los permisos que tiene el sistema
            for (PermisoEnum permiso : PermisoEnum.values()) {

                Permiso insercion = this.insertarPermisoSiNoExiste(
                        new Permiso(
                                permiso.getNombrePermiso(),
                                permiso.getRuta()));
                try {
                    // Asignacion de permisos a los usuarios
                    Usuario ayudante2 = this.usuarioService.getByEmail(
                            "luismonteg1@hotmail.com");
                    this.usuarioService.agregarPermisoUsuario(ayudante2,
                            insercion);
                } catch (Exception e) {
                }
            }  */
        } catch (Exception ex) {
            Logger.getLogger(SeedersConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String cargarImagenComoBase64() {
        // Cargar el archivo desde resources
        try (InputStream inputStream = getClass().getResourceAsStream("/images/logo.svg")) {
            if (inputStream == null) {
                throw new IOException("No se pudo encontrar el archivo logo.svg en el directorio /resources/images");
            }

            // Leer todos los bytes del archivo
            byte[] bytes = inputStream.readAllBytes();

            // Convertir a Base64
            return Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar y convertir la imagen a Base64", e);
        }
    }
}
