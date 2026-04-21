package visualmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import visualmserver.models.User;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  User getUserById(int id);
  User getUserByEmail(String email);

  List<User> findByOrganisationAndAdmin(String organisation, boolean admin);

  @Transactional
  @Modifying
  Long deleteUserById(int id);
}
