package usac.api.config;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luis Monterroso
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String hostFront;

    public String getHostFront() {
        return hostFront;
    }

    public void setHostFront(String hostFront) {
        this.hostFront = hostFront;
    }
}
