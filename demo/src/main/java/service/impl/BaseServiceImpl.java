package service.impl;

import model.base.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.base.BaseRepository;
import service.BaseService;

import java.io.Serializable;
import java.util.List;

@Service
public abstract class BaseServiceImpl<ID extends Serializable, T extends BaseEntity<ID>> implements BaseService<ID, T> {

    @Autowired
    BaseRepository<ID, T> baseRepository;

    @Override
    public T save (T entity ){
        return baseRepository.save(entity);
    };

    @Override
    @Transactional
    public List<T> saveAll (List<T> entitiesList ){
        return baseRepository.saveAll(entitiesList);
    };







}
