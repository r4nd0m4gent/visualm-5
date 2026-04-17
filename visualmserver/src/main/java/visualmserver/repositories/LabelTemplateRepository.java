package visualmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import visualmserver.models.LabelTemplate;

import java.util.List;

@Repository
public interface LabelTemplateRepository extends JpaRepository<LabelTemplate, Integer> {

    List<LabelTemplate> findByOrgAdminId(int orgAdminId);

    LabelTemplate findById(int id);
}
