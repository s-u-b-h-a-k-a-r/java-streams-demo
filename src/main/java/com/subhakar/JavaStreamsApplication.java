package com.subhakar;

import com.subhakar.streams.repository.CustomerRepository;
import com.subhakar.streams.repository.OrderRepository;
import com.subhakar.streams.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class JavaStreamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaStreamsApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ProductRepository productRepository, OrderRepository orderRepository, CustomerRepository customerRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                log.info("Customers:");
                customerRepository.findAll()
                        .forEach(c -> log.info(c.toString()));

                log.info("Orders:");
                orderRepository.findAll()
                        .forEach(o -> log.info(o.toString()));

                log.info("Products:");
                productRepository.findAll()
                        .forEach(p -> log.info(p.toString()));
            }
        };
    }
}
