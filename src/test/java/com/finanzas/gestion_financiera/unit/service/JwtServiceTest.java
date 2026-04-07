package com.finanzas.gestion_financiera.unit.service;

import com.finanzas.gestion_financiera.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtService - Unit Tests")
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "clave-super-secreta-larga-para-jwt-minimo-32-caracteres-test");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
    }

    @Nested
    @DisplayName("generateToken()")
    class GenerateToken {

        @Test
        @DisplayName("Debe generar token JWT no nulo")
        void debeGenerarTokenNoNulo() {
            // Arrange
            String email = "test@email.com";

            // Act
            String token = jwtService.generateToken(email);

            // Assert
            assertNotNull(token);
            assertFalse(token.isEmpty());
        }

        @Test
        @DisplayName("Debe generar tokens diferentes para distintos emails")
        void debeGenerarTokensDiferentes() {
            // Arrange
            String email1 = "user1@email.com";
            String email2 = "user2@email.com";

            // Act
            String token1 = jwtService.generateToken(email1);
            String token2 = jwtService.generateToken(email2);

            // Assert
            assertNotEquals(token1, token2);
        }

        @Test
        @DisplayName("Token debe tener formato JWT válido (3 partes separadas por punto)")
        void tokenDebeSerFormatoJwt() {
            // Arrange
            String email = "test@email.com";

            // Act
            String token = jwtService.generateToken(email);

            // Assert
            String[] parts = token.split("\\.");
            assertEquals(3, parts.length, "JWT debe tener 3 partes: header.payload.signature");
        }
    }

    @Nested
    @DisplayName("extractEmail()")
    class ExtractEmail {

        @Test
        @DisplayName("Debe extraer el email correcto del token")
        void debeExtraerEmailCorrecto() {
            // Arrange
            String email = "juan@email.com";
            String token = jwtService.generateToken(email);

            // Act
            String extractedEmail = jwtService.extractEmail(token);

            // Assert
            assertEquals(email, extractedEmail);
        }

        @Test
        @DisplayName("Debe lanzar excepción con token inválido")
        void debeLanzarExcepcionConTokenInvalido() {
            // Arrange
            String invalidToken = "token.invalido.aqui";

            // Act & Assert
            assertThrows(Exception.class,
                    () -> jwtService.extractEmail(invalidToken));
        }
    }

    @Nested
    @DisplayName("isTokenValid()")
    class IsTokenValid {

        @Test
        @DisplayName("Debe retornar true para token válido")
        void debeRetornarTrueParaTokenValido() {
            // Arrange
            String token = jwtService.generateToken("test@email.com");

            // Act
            boolean result = jwtService.isTokenValid(token);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("Debe retornar false para token inválido")
        void debeRetornarFalseParaTokenInvalido() {
            // Arrange
            String invalidToken = "eyJhbGciOiJIUzI1NiJ9.invalid.signature";

            // Act
            boolean result = jwtService.isTokenValid(invalidToken);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("Debe retornar false para token expirado")
        void debeRetornarFalseParaTokenExpirado() {
            // Arrange
            JwtService expiredJwtService = new JwtService();
            ReflectionTestUtils.setField(expiredJwtService, "secret",
                    "clave-super-secreta-larga-para-jwt-minimo-32-caracteres-test");
            ReflectionTestUtils.setField(expiredJwtService, "expiration", -1000L);

            String token = expiredJwtService.generateToken("test@email.com");

            // Act
            boolean result = expiredJwtService.isTokenValid(token);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("Debe retornar false para token con firma diferente")
        void debeRetornarFalseParaTokenConFirmaDiferente() {
            // Arrange
            JwtService otherService = new JwtService();
            ReflectionTestUtils.setField(otherService, "secret",
                    "otra-clave-secreta-diferente-para-jwt-minimo-32-caracteres");
            ReflectionTestUtils.setField(otherService, "expiration", 86400000L);

            String token = otherService.generateToken("test@email.com");

            // Act
            boolean result = jwtService.isTokenValid(token);

            // Assert
            assertFalse(result);
        }
    }
}
