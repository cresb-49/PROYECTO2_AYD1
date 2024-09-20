/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author Luis Monterroso
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ApIecommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApIecommerceApplication.class, args);
    }
}
