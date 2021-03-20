package br.com.rafaelporrecati.dscatalog.resources;

import br.com.rafaelporrecati.dscatalog.dto.CategoryDTO;
import br.com.rafaelporrecati.dscatalog.entities.Category;
import br.com.rafaelporrecati.dscatalog.servicies.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(){
        List<CategoryDTO> list = categoryService.findAll();
        return ResponseEntity.ok().body(list);
    }
}
