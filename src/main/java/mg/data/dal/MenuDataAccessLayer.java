package mg.data.dal;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import mg.boundaries.MenuBoundary;
import mg.data.entities.MenuEntity;

public interface MenuDataAccessLayer extends JpaRepository<MenuEntity, Long> {
	
	public List<MenuEntity> findAllByTimestampBetween(Date start, Date end);

	public MenuBoundary[] findAllByUserEmail(String userEmail, PageRequest of);

}
