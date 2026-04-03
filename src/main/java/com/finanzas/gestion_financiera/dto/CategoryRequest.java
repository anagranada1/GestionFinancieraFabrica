package com.finanzas.gestion_financiera.dto;

import com.finanzas.gestion_financiera.entity.Category.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank
    private String nombre;

    @NotNull
    private TipoCategoria tipo;
}
