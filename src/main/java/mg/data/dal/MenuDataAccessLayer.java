package mg.data.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.data.entities.MenuEntity;

public interface MenuDataAccessLayer extends JpaRepository<MenuEntity, Long> {

}
