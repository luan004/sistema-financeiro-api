package br.com.sistema.financeiro.api.security;

import br.com.sistema.financeiro.api.model.Session;
import br.com.sistema.financeiro.api.model.User;
import br.com.sistema.financeiro.api.repository.SessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Configuration
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";

    private final SessionRepository repo;
    private final ObjectMapper objectMapper;

    public AuthenticationFilter(SessionRepository repo, ObjectMapper objectMapper) {
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(AUTH_HEADER);

        if (header == null) {
            writeUnauthorized(response);
            return;
        }

        String rawToken = header.trim();
        String bearerPrefix = "Bearer ";
        if (rawToken.regionMatches(true, 0, bearerPrefix, 0, bearerPrefix.length())) {
            rawToken = rawToken.substring(bearerPrefix.length()).trim();
        }
        Optional<Session> sessionOptional = repo.findByToken(rawToken);

        if (sessionOptional.isEmpty()) {
            writeUnauthorized(response);
            return;
        }

        User user = sessionOptional.get().getUser();
        request.setAttribute("auth", user);
        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        Map<String, Object> body = Map.of(
            "type", "about:blank",
            "title", "Não autenticado",
            "status", HttpStatus.UNAUTHORIZED.value(),
            "detail", "Autenticação obrigatória"
        );
        objectMapper.writeValue(response.getWriter(), body);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        return "OPTIONS".equalsIgnoreCase(method)
            || request.getRequestURI().equals("/login");
    }
}