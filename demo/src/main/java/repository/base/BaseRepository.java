package repository.base;


import model.base.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
@NoRepositoryBean
public interface BaseRepository<ID extends Serializable,T extends BaseEntity<ID>> extends JpaRepository<T,ID> {
}
