package dev.patika.veterinary.management.system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@OpenAPIDefinition(info = @Info(title = "Veterinary app",version = "1.0",description = "Veterinary app"))
class VeterinaryManagementSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
