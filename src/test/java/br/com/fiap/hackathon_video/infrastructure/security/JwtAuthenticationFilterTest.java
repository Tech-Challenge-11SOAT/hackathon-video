package br.com.fiap.hackathon_video.infrastructure.security;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.fiap.hackathon_video.application.ports.outbound.TokenValidationPort;
import br.com.fiap.hackathon_video.infrastructure.security.exception.ExpiredTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private TokenValidationPort tokenValidationPort;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveProcessarRequisicaoSemToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(tokenValidationPort, never()).validateToken(anyString());
        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void deveIgnorarTokenSemPrefixoBearer() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "TokenSemBearer token123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(tokenValidationPort, never()).validateToken(anyString());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void deveAutenticarUserQuandoTokenBearerForValido() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-valido-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenValidationPort.validateToken("token-valido-123")).thenReturn(true);
        when(tokenValidationPort.extractUsername("token-valido-123")).thenReturn("joao.teste");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(tokenValidationPort).validateToken("token-valido-123");
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("joao.teste");
    }

    @Test
    void deveTratarTokenExceptionsEContinuarACadeia() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-expirado");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenValidationPort.validateToken("token-expirado"))
                .thenThrow(new ExpiredTokenException("Expirou", new RuntimeException()));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void deveTratarGenericExceptions() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-com-bug");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenValidationPort.validateToken("token-com-bug"))
                .thenThrow(new RuntimeException("Bug paia no banco ou servidor"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void deveNaoAutenticarQuandoTokenForValidadoComoFalso() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-invalido-retorno-false");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenValidationPort.validateToken("token-invalido-retorno-false")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(tokenValidationPort).validateToken("token-invalido-retorno-false");
        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}