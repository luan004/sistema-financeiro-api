package br.com.sistema.financeiro.api.controller;

import br.com.sistema.financeiro.api.model.Session;
import br.com.sistema.financeiro.api.model.User;
import br.com.sistema.financeiro.api.repository.AccountRepository;
import br.com.sistema.financeiro.api.repository.SessionRepository;
import br.com.sistema.financeiro.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAccountWhenAuthenticated() throws Exception {
        User user = userRepository.save(new User("Alice", "alice@email.com", "123456"));
        Session session = sessionRepository.save(new Session(user, "session-token-123"));

        String payload = """
                {
                  "description": "Conta principal"
                }
                """;

        mockMvc.perform(post("/accounts")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + session.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Conta principal"));
    }

    @Test
    void shouldRequirePaginationParametersWhenListingAccounts() throws Exception {
        User user = userRepository.save(new User("Alice", "alice@email.com", "123456"));
        Session session = sessionRepository.save(new Session(user, "session-token-123"));

        mockMvc.perform(get("/accounts")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + session.getToken()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("O parâmetro 'page' é obrigatório."));
    }

    @Test
    void shouldReturnUnauthorizedWhenNoTokenProvided() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "description": "Conta principal"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Não autenticado"));
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
        mockMvc.perform(get("/accounts")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());
    }
}