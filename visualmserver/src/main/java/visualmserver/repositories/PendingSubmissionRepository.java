package visualmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import visualmserver.models.PendingSubmission;

@Repository
public interface PendingSubmissionRepository extends JpaRepository<PendingSubmission, Long> {
}
