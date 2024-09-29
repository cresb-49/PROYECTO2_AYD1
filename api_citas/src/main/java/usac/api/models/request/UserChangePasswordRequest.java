package usac.api.models.request;

import org.springframework.stereotype.Component;

@Component
public class UserChangePasswordRequest {
    private Long id;
    private String email;
    private String password;
    private String newPassword;

    public UserChangePasswordRequest(Long id,String email, String password, String newPassword) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
    }

    public UserChangePasswordRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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