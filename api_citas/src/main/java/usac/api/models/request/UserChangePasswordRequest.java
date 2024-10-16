package usac.api.models.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

@Component
public class UserChangePasswordRequest {

    @NotNull(message = "El id no puede ser nulo.")
    private Long id;

    // @NotBlank(message = "La email no puede estar vacía.")
    // @NotNull(message = "La email no puede ser nula.")
    // private String email;

    @NotBlank(message = "La contraseña actual no puede estar vacía.")
    @NotNull(message = "La contraseña actual no puede ser nula.")
    private String password;

    @Size(min = 1, max = 250, message = "La contraseña nueva debe tener entre 1 y 250 caracteres.")
    @NotBlank(message = "La contraseña nueva no puede estar vacía.")
    @NotNull(message = "La contraseña nueva no puede ser nula.")
    private String newPassword;

    // public UserChangePasswordRequest(Long id, String email, String password, String newPassword) {
    //     this.id = id;
    //     this.email = email;
    //     this.password = password;
    //     this.newPassword = newPassword;
    // }

    public UserChangePasswordRequest() {
    }

    public UserChangePasswordRequest(Long id, String newPassword, String password) {
        this.id = id;
        this.newPassword = newPassword;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // public String getEmail() {
    //     return email;
    // }

    // public void setEmail(String email) {
    //     this.email = email;
    // }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
