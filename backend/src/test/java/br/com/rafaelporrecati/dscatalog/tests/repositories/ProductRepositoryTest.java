package br.com.rafaelporrecati.dscatalog.tests.repositories;

import br.com.rafaelporrecati.dscatalog.entities.Product;
import br.com.rafaelporrecati.dscatalog.repositories.ProductRepository;
import br.com.rafaelporrecati.dscatalog.tests.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nomExistingId;
    private long countTotalProducts;
    private long countPcGamerProducts;
    PageRequest pageRequest;

    @BeforeEach
    void setUp()throws Exception{
        pageRequest = PageRequest.of(0,10) ;

        existingId = 1L;
        nomExistingId = 1000L;
        countTotalProducts = 25L;
        countPcGamerProducts = 21L;

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExist(){
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldTrhowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
            repository.deleteById(nomExistingId);
        });
    }

    @Test
    public void savShouldPersistWithAutoincrementWhenIdIsNull(){
        Product product = ProductFactory.createProduct();
        product.setId(null);
        product = repository.save(product);

        Optional<Product> result = repository.findById(product.getId());

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertSame(result.get(),product);
    }

    @Test
    public void findShouldReturnProductsWhenNameExits(){
        String name = "PC Gamer";

        Page<Product> result = repository.find(null,name, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
    }

    @Test
    public void findShouldReturnProductsWhenNameExitsIgnoringCase(){
        String name = "pc gaMer";

        Page<Product> result = repository.find(null,name, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
    }

    @Test
    public void findShouldReturnAllProductsProductsWhenNameDoesNotExist(){
        String name = "Camera";

        Page<Product> result = repository.find(null,name, pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findShouldReturnAllProductsProductsWhenNameIsEmpty(){
        String name = "";

        Page<Product> result = repository.find(null,name, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

}
