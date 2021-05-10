package br.com.rafaelporrecati.dscatalog.servicies.validation;

import br.com.rafaelporrecati.dscatalog.dto.UserInsertDTO;
import br.com.rafaelporrecati.dscatalog.entities.User;
import br.com.rafaelporrecati.dscatalog.repositories.UserRepository;
import br.com.rafaelporrecati.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();
        User user = repository.findByEmail(dto.getEmail());
        if(user != null){
            list.add(new FieldMessage("email","E-mail já existe"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
