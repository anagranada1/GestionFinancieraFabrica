package com.finanzas.gestion_financiera.dto;

import com.finanzas.gestion_financiera.entity.Category.TipoCategoria;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String nombre;
    private TipoCategoria tipo;
}
