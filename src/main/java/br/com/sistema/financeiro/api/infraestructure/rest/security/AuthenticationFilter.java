package br.com.sistema.financeiro.api.infraestructure.rest.security;

import br.com.sistema.financeiro.api.domain.session.Session;
import br.com.sistema.financeiro.api.domain.session.SessionRepository;
import br.com.sistema.financeiro.api.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Configuration
public class AuthenticationFilter extends OncePerRequestFilter {

    private final SessionRepository repo;

    public AuthenticationFilter(SessionRepository repo) {
        this.repo = repo;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws
        ServletException,
        IOException
    {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            String rawToken = token.substring(7).trim();

            Optional<Session> sessionOptional = repo.findByToken(rawToken);

            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                User user = session.getUser();
                request.setAttribute("auth", user);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(
        HttpServletRequest request
    ) {
        return request.getRequestURI().equals("/login");
    }
}
