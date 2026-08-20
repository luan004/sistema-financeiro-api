package br.com.sistema.financeiro.api.controller;

import br.com.sistema.financeiro.api.model.Session;
import br.com.sistema.financeiro.api.model.User;
import br.com.sistema.financeiro.api.repository.SessionRepository;
import br.com.sistema.financeiro.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldRequirePaginationParametersWhenListingMovements() throws Exception {
        User user = userRepository.save(new User("Alice", "alice@email.com", "123456"));
        Session session = sessionRepository.save(new Session(user, "session-token-123"));

        mockMvc.perform(get("/movements")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + session.getToken()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("O parâmetro 'page' é obrigatório."));
    }
}