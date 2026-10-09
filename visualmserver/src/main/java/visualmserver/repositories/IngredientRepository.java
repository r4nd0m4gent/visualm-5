package visualmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import visualmserver.models.Ingredient;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, String> {

    Ingredient getIngredientById(int id);
    List<Ingredient> getIngredientByName(String name);
    List<Ingredient> findAllByNameIsContaining(String name);

    Optional<Ingredient> findByNameAndType(String name, String type);

    @Query(value = "SELECT COALESCE(MAX(id), 0) FROM ingredient", nativeQuery = true)
    int findMaxId();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO ingredient (id, name, type) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void insertIngredientWithId(int id, String name, String type);

    @Transactional
    @Modifying
    Integer deleteById(int id);
}
