package br.com.rafaelporrecati.dscatalog.tests.integration;

import br.com.rafaelporrecati.dscatalog.dto.ProductDTO;
import br.com.rafaelporrecati.dscatalog.servicies.ProductService;
import br.com.rafaelporrecati.dscatalog.servicies.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    private long existingId;
    private long nomExistingId;
    private long countTotalProducts;
    private long countPcGamerProducts;
    private PageRequest pageRequest;


    @BeforeEach
    void setUp(){
        pageRequest = PageRequest.of(0,10) ;
        existingId = 1L;
        nomExistingId = 1000L;
        countTotalProducts = 25L;
        countPcGamerProducts = 21L;

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

    }

    @Test
    public void deleteShouldDoThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nomExistingId);
        });

    }

    @Test
    public void findAllPagedShouldReturnAllProductsProductsWhenNameDoesNotExist(){
        String name = "Camera";

        Page<ProductDTO> result = service.findAllPaged(0L,name, pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnProductsWhenNameExitsIgnoringCase(){
        String name = "pc gaMer";

        Page<ProductDTO> result = service.findAllPaged(0L,name, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(countPcGamerProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnAllProductsProductsWhenNameIsEmpty(){
        String name = "";

        Page<ProductDTO> result = service.findAllPaged(0L,name, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }
}
