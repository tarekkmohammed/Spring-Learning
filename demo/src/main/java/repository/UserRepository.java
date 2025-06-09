package repository;


import model.user.User;
import org.springframework.stereotype.Repository;
import repository.base.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<Integer, User> {

}
