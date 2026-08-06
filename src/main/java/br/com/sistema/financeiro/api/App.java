package br.com.sistema.financeiro.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aqui é o bootstrap da aplicação, onde o Spring Boot é inicializado e a aplicação é executada.
 * Não alterem esse arquivo sem conversas com a equipe.
 * 
 * @author Luan004
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
