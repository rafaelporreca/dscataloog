package br.com.rafaelporrecati.dscatalog.tests.factory;

import br.com.rafaelporrecati.dscatalog.dto.ProductDTO;
import br.com.rafaelporrecati.dscatalog.entities.Category;
import br.com.rafaelporrecati.dscatalog.entities.Product;

import java.time.Instant;

public class ProductFactory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone","Phone desc" , 800.0, "http://imagem", Instant.now());
        product.getCategories().add(new Category(1L,null));
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product,product.getCategories());
    }

    public static ProductDTO createProductDTO(Long id){
        ProductDTO dto = createProductDTO();
        dto.setId(id);
        return dto;
    }
}
