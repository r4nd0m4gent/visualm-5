package visualmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import visualmserver.models.OrgAdmin;

@Repository
public interface OrgAdminRepository extends JpaRepository<OrgAdmin, Integer> {
    OrgAdmin findByAccessToken(String accessToken);
    OrgAdmin findByEmail(String email);
}
