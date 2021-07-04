package br.com.rafaelporrecati.dscatalog.tests.serviceis;


import br.com.rafaelporrecati.dscatalog.entities.Product;
import br.com.rafaelporrecati.dscatalog.repositories.ProductRepository;
import br.com.rafaelporrecati.dscatalog.servicies.ProductService;
import br.com.rafaelporrecati.dscatalog.servicies.exceptions.DatabaseException;
import br.com.rafaelporrecati.dscatalog.servicies.exceptions.ResourceNotFoundException;
import br.com.rafaelporrecati.dscatalog.tests.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;

    private long existingId;
    private long nomExistingId;
    private long dependentId;

    private Product product;
    private PageImpl<Product> page;


    @BeforeEach
    void setUp(){
        existingId = 1L;
        nomExistingId = 1000L;
        dependentId = 4L;

        product = ProductFactory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.find(ArgumentMatchers.any(),ArgumentMatchers.anyString(),ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nomExistingId)).thenReturn(Optional.empty());



        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nomExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        Mockito.verify(repository).deleteById(existingId);
    }

    @Test
    public void deleteShouldDoThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nomExistingId);
        });

        Mockito.verify(repository).deleteById(nomExistingId);
    }

    @Test
    public void deleteShouldDoThrowDatabaseExceptionWhenDependentId(){

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

        Mockito.verify(repository).deleteById(dependentId);
    }
}
