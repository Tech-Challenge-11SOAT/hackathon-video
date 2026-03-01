package br.com.fiap.hackathon_video.infrastructure.security;

import br.com.fiap.hackathon_video.infrastructure.security.exception.ExpiredTokenException;
import br.com.fiap.hackathon_video.infrastructure.security.exception.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private final String SECRET = "minha-chave-secreta-muito-segura-taligado";

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secret", SECRET);
    }

    private String createToken(String subject, String userId, long expirationMillis) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("idUser", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    @Test
    void deveValidarTokenComSucesso() {
        String token = createToken("testuser", UUID.randomUUID().toString(), 100000);
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertThat(isValid).isTrue();
    }

    @Test
    void deveLancarExcecaoParaTokenExpiradoNaValidacao() {
        String token = createToken("testuser", UUID.randomUUID().toString(), -1000);
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    void deveLancarExcecaoParaTokenInvalidoNaValidacao() {
        assertThatThrownBy(() -> jwtTokenProvider.validateToken("token-invalido-qualquer"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Token JWT inválido");
    }

    @Test
    void deveCairNoCathGenericoAoTentarValidarTokenNulo() {
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(null))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Erro ao processar token");
    }

    @Test
    void deveExtrairUsername() {
        String token = createToken("admin_test", UUID.randomUUID().toString(), 100000);
        String username = jwtTokenProvider.extractUsername(token);
        assertThat(username).isEqualTo("admin_test");
    }

    @Test
    void deveExtrairUsedId() {
        String userId = UUID.randomUUID().toString();
        String token = createToken("testuser", userId, 100000);
        String extractedId = jwtTokenProvider.extractUserId(token);
        assertThat(extractedId).isEqualTo(userId);
    }

    @Test
    void devePropagarExcecoesAoFalharExtracaoDeClaims() {
        String expiredToken = createToken("testuser", UUID.randomUUID().toString(), -1000);

        assertThatThrownBy(() -> jwtTokenProvider.extractUsername(expiredToken)).isInstanceOf(ExpiredTokenException.class);
        assertThatThrownBy(() -> jwtTokenProvider.extractUserId(expiredToken)).isInstanceOf(ExpiredTokenException.class);

        assertThatThrownBy(() -> jwtTokenProvider.extractUsername("invalido")).isInstanceOf(InvalidTokenException.class);
        assertThatThrownBy(() -> jwtTokenProvider.extractUserId("invalido")).isInstanceOf(InvalidTokenException.class);
    }
}