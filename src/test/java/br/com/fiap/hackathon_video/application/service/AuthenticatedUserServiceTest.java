package br.com.fiap.hackathon_video.application.service;

import br.com.fiap.hackathon_video.application.ports.outbound.TokenValidationPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticatedUserServiceTest {

    @Mock
    private TokenValidationPort tokenValidationPort;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthenticatedUserService authenticatedUserService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveRetornarUsernameAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("teste_user");

        String username = authenticatedUserService.getAuthenticatedUsername();

        assertThat(username).isEqualTo("teste_user");
    }

    @Test
    void deveRetornarUserIdAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getCredentials()).thenReturn("token_valido_123");
        when(tokenValidationPort.extractUserId("token_valido_123")).thenReturn("id-do-usuario");

        String userId = authenticatedUserService.getAuthenticatedUserId();

        assertThat(userId).isEqualTo("id-do-usuario");
    }

    @Test
    void deveLancarExcecaoQuandoAutenticacaoForNula() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThatThrownBy(() -> authenticatedUserService.verifyAuthentication())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não autenticado");
    }

    @Test
    void deveLancarExcecaoQuandoOUserNaoEstiverAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThatThrownBy(() -> authenticatedUserService.verifyAuthentication())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não autenticado");
    }

    @Test
    void deveLancarExcecaoQuandoAsCredenciaisForemNulas() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getCredentials()).thenReturn(null);

        assertThatThrownBy(() -> authenticatedUserService.getAuthenticatedUserId())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Token JWT ausente na autenticação");
    }
}