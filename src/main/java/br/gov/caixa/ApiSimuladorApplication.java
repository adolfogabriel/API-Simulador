package br.gov.caixa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@OpenAPIDefinition(info = @Info(title = "API_Simulador",
        version = "1.0", description = "Simulacão SAC e PRICE HackCaixa"))
public class ApiSimuladorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSimuladorApplication.class, args);
	}

}
