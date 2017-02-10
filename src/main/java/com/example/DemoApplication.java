package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
public class DemoApplication {

    private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @Bean
    @Profile("reset")
    ApplicationRunner reset(ContactRepo repo) {
        return args -> {
            repo.deleteAll();
            Stream.of("Alice", "Bob", "Eve")
                    .map(Contact::new)
                    .forEach(repo::save);

            logger.warn("DB reset is done");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RepositoryRestResource
interface ContactRepo extends JpaRepository<Contact, Long> {

}

@Entity
class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String name;

    protected Contact() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact(String name) {

        this.name = name;
    }
}













