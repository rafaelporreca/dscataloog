package br.com.rafaelporrecati.dscatalog.repositories;

import br.com.rafaelporrecati.dscatalog.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
