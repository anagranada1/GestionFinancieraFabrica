package com.finanzas.gestion_financiera.controller;

import com.finanzas.gestion_financiera.dto.CategoryRequest;
import com.finanzas.gestion_financiera.dto.CategoryResponse;
import com.finanzas.gestion_financiera.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> crear(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listar() {
        return ResponseEntity.ok(categoryService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.obtener(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoryService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
