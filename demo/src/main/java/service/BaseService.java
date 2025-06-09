package service;

import model.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface BaseService<ID extends Serializable,T extends BaseEntity<ID>>  {

     T save (T entity );
     T getByID (ID id );
     List<T> saveAll (List<T> entitiesList );


}
