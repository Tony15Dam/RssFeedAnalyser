package RssFeedAnalyser.RFA.Repositories;

import RssFeedAnalyser.RFA.Models.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {
    AnalysisResult findByUuid(UUID uuid);
}
