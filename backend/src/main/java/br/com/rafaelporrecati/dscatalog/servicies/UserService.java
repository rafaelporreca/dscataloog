package br.com.rafaelporrecati.dscatalog.servicies;

import br.com.rafaelporrecati.dscatalog.dto.RoleDTO;
import br.com.rafaelporrecati.dscatalog.dto.UserDTO;
import br.com.rafaelporrecati.dscatalog.dto.UserInsertDTO;
import br.com.rafaelporrecati.dscatalog.dto.UserUpdateDTO;
import br.com.rafaelporrecati.dscatalog.entities.Role;
import br.com.rafaelporrecati.dscatalog.entities.User;
import br.com.rafaelporrecati.dscatalog.repositories.RoleRepository;
import br.com.rafaelporrecati.dscatalog.repositories.UserRepository;
import br.com.rafaelporrecati.dscatalog.servicies.exceptions.DatabaseException;
import br.com.rafaelporrecati.dscatalog.servicies.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository UserRepository, RoleRepository roleRepository) {
        this.userRepository = UserRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(PageRequest pageRequest){
        Page<User> list = userRepository.findAll(pageRequest);
        Page<UserDTO> listDto = list.map(obj -> new UserDTO(obj));
        return listDto;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        copyDtoToEntity(dto,entity);
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }



    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = userRepository.getOne(id);
            copyDtoToEntity(dto,entity);
            userRepository.save(entity);
            return new UserDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for(RoleDTO roleDto : dto.getRoles()){
            Role role = roleRepository.getOne(roleDto.getId());
            entity.getRoles().add(role);
        }
    }
}
