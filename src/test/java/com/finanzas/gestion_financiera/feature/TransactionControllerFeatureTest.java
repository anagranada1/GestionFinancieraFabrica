package com.finanzas.gestion_financiera.feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzas.gestion_financiera.config.SecurityConfig;
import com.finanzas.gestion_financiera.controller.TransactionController;
import com.finanzas.gestion_financiera.dto.TransactionRequest;
import com.finanzas.gestion_financiera.dto.TransactionResponse;
import com.finanzas.gestion_financiera.service.JwtService;
import com.finanzas.gestion_financiera.service.TransactionService;
import com.finanzas.gestion_financiera.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(SecurityConfig.class)
@DisplayName("Transaction Feature - API /api/v1/transacciones")
class TransactionControllerFeatureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Nested
    @DisplayName("POST /api/v1/transacciones")
    class CrearEndpoint {

        @Test
        @WithMockUser(username = "test@email.com")
        @DisplayName("Debe crear transacción de ingreso y retornar 200")
        void debeCrearTransaccionIngreso() throws Exception {
            // Arrange
            TransactionRequest request = new TransactionRequest();
            request.setTipo("INGRESO");
            request.setMonto(new BigDecimal("5000.00"));
            request.setFecha(LocalDate.of(2026, 4, 1));
            request.setCategoriaId(1L);

            TransactionResponse response = new TransactionResponse(
                    1L, "INGRESO", new BigDecimal("5000.00"),
                    LocalDate.of(2026, 4, 1), "Salario");
            when(transactionService.crear(any(TransactionRequest.class))).thenReturn(response);

            // Act & Assert
            mockMvc.perform(post("/api/v1/transacciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.tipo").value("INGRESO"))
                    .andExpect(jsonPath("$.monto").value(5000.00))
                    .andExpect(jsonPath("$.categoria").value("Salario"));
        }

        @Test
        @WithMockUser(username = "test@email.com")
        @DisplayName("Debe crear transacción de gasto y retornar 200")
        void debeCrearTransaccionGasto() throws Exception {
            // Arrange
            TransactionRequest request = new TransactionRequest();
            request.setTipo("GASTO");
            request.setMonto(new BigDecimal("250.75"));
            request.setFecha(LocalDate.of(2026, 4, 5));
            request.setCategoriaId(5L);

            TransactionResponse response = new TransactionResponse(
                    2L, "GASTO", new BigDecimal("250.75"),
                    LocalDate.of(2026, 4, 5), "Alimentación");
            when(transactionService.crear(any(TransactionRequest.class))).thenReturn(response);

            // Act & Assert
            mockMvc.perform(post("/api/v1/transacciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipo").value("GASTO"))
                    .andExpect(jsonPath("$.monto").value(250.75));
        }

        @Test
        @WithMockUser(username = "test@email.com")
        @DisplayName("Debe retornar 400 si el tipo es inválido")
        void debeRetornar400SiTipoInvalido() throws Exception {
            // Arrange
            String json = """
                    {
                        "tipo": "INVALIDO",
                        "monto": 100.00,
                        "fecha": "2026-04-01",
                        "categoriaId": 1
                    }
                    """;

            // Act & Assert
            mockMvc.perform(post("/api/v1/transacciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "test@email.com")
        @DisplayName("Debe retornar 400 si el monto es 0 o negativo")
        void debeRetornar400SiMontoInvalido() throws Exception {
            // Arrange
            String json = """
                    {
                        "tipo": "GASTO",
                        "monto": 0,
                        "fecha": "2026-04-01",
                        "categoriaId": 1
                    }
                    """;

            // Act & Assert
            mockMvc.perform(post("/api/v1/transacciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "test@email.com")
        @DisplayName("Debe retornar 400 si falta la fecha")
        void debeRetornar400SiFaltaFecha() throws Exception {
            // Arrange
            String json = """
                    {
                        "tipo": "GASTO",
                        "monto": 100.00,
                        "categoriaId": 1
                    }
                    """;

            // Act & Assert
            mockMvc.perform(post("/api/v1/transacciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "test@email.com")
        @DisplayName("Debe retornar 400 si falta la categoría")
        void debeRetornar400SiFaltaCategoria() throws Exception {
            // Arrange
            String json = """
                    {
                        "tipo": "INGRESO",
                        "monto": 100.00,
                        "fecha": "2026-04-01"
                    }
                    """;

            // Act & Assert
            mockMvc.perform(post("/api/v1/transacciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 401 si no está autenticado")
        void debeRetornar401SiNoAutenticado() throws Exception {
            // Arrange
            TransactionRequest request = new TransactionRequest();
            request.setTipo("INGRESO");
            request.setMonto(new BigDecimal("100"));
            request.setFecha(LocalDate.now());
            request.setCategoriaId(1L);

            // Act & Assert
            mockMvc.perform(post("/api/v1/transacciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/transacciones")
    class ListarEndpoint {

        @Test
        @WithMockUser(username = "test@email.com")
        @DisplayName("Debe listar transacciones del usuario autenticado")
        void debeListarTransacciones() throws Exception {
            // Arrange
            List<TransactionResponse> transacciones = List.of(
                    new TransactionResponse(1L, "INGRESO", new BigDecimal("5000"),
                            LocalDate.of(2026, 4, 1), "Salario"),
                    new TransactionResponse(2L, "GASTO", new BigDecimal("200"),
                            LocalDate.of(2026, 4, 2), "Alimentación")
            );
            when(transactionService.listar()).thenReturn(transacciones);

            // Act & Assert
            mockMvc.perform(get("/api/v1/transacciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].tipo").value("INGRESO"))
                    .andExpect(jsonPath("$[0].monto").value(5000))
                    .andExpect(jsonPath("$[1].tipo").value("GASTO"));
        }

        @Test
        @WithMockUser(username = "test@email.com")
        @DisplayName("Debe retornar lista vacía si no hay transacciones")
        void debeRetornarListaVacia() throws Exception {
            // Arrange
            when(transactionService.listar()).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/api/v1/transacciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Debe retornar 401 si no está autenticado")
        void debeRetornar401SiNoAutenticado() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/transacciones"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
