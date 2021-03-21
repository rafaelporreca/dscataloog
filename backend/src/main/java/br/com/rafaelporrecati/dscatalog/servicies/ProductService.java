package br.com.rafaelporrecati.dscatalog.servicies;

import br.com.rafaelporrecati.dscatalog.dto.CategoryDTO;
import br.com.rafaelporrecati.dscatalog.dto.ProductDTO;
import br.com.rafaelporrecati.dscatalog.entities.Category;
import br.com.rafaelporrecati.dscatalog.entities.Product;
import br.com.rafaelporrecati.dscatalog.repositories.CategoryRepository;
import br.com.rafaelporrecati.dscatalog.repositories.ProductRepository;
import br.com.rafaelporrecati.dscatalog.servicies.exceptions.DatabaseException;
import br.com.rafaelporrecati.dscatalog.servicies.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository ProductRepository, CategoryRepository categoryRepository) {
        this.productRepository = ProductRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> list = productRepository.findAll(pageRequest);
        Page<ProductDTO> listDto = list.map(obj -> new ProductDTO(obj));
        return listDto;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity,entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto,entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }



    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
//            Product entity = productRepository.getOne(id);
            Product entity = productRepository.getOne(id);
            copyDtoToEntity(dto,entity);
            productRepository.save(entity);
            return new ProductDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();
        for(CategoryDTO catDto : dto.getCategories()){
            Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}
