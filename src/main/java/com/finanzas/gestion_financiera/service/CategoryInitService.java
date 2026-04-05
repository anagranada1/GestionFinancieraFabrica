package com.finanzas.gestion_financiera.service;

import com.finanzas.gestion_financiera.entity.Category;
import com.finanzas.gestion_financiera.entity.User;
import com.finanzas.gestion_financiera.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryInitService {

    private final CategoryRepository categoryRepository;

    public void crearCategoriasPorDefecto(User user) {
        List<Category> categorias = List.of(
                crearCategoria("Salario",       Category.TipoCategoria.INGRESO, user),
                crearCategoria("Freelance",     Category.TipoCategoria.INGRESO, user),
                crearCategoria("Inversiones",   Category.TipoCategoria.INGRESO, user),
                crearCategoria("Otros ingresos",Category.TipoCategoria.INGRESO, user),
                crearCategoria("Alimentación",  Category.TipoCategoria.GASTO,   user),
                crearCategoria("Transporte",    Category.TipoCategoria.GASTO,   user),
                crearCategoria("Vivienda",      Category.TipoCategoria.GASTO,   user),
                crearCategoria("Salud",         Category.TipoCategoria.GASTO,   user),
                crearCategoria("Entretenimiento",Category.TipoCategoria.GASTO,  user),
                crearCategoria("Educación",     Category.TipoCategoria.GASTO,   user)
        );
        categoryRepository.saveAll(categorias);
    }

    private Category crearCategoria(String nombre, Category.TipoCategoria tipo, User user) {
        Category category = new Category();
        category.setNombre(nombre);
        category.setTipo(tipo);
        category.setUsuario(user);
        return category;
    }
}
