package com.example.demo;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.AzureCliCredentialBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DemoApplication.class);
        application.addBootstrapRegistryInitializer(registry ->
                registry.register(TokenCredential.class, context -> new AzureCliCredentialBuilder().build()));

        application.run(args);
    }

}
