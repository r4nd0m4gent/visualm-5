package visualmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import visualmserver.models.Material;
import visualmserver.models.SaveStatus;
import visualmserver.models.User;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface MaterialsRepository extends JpaRepository<Material, Long> {
    Material getMaterialBySequenceNumber(Long sequenceNumber);
    Material getMaterialBySequenceNumberPublished(Long sequenceNumberPublished);
    List<Material> getMaterialsByUser (User user);
    List<Material> getMaterialsBySaveStatus(SaveStatus saveStatus);
    @Modifying
    @Transactional
    Long deleteMaterialBySequenceNumber(Long sequenceNumber);
}
