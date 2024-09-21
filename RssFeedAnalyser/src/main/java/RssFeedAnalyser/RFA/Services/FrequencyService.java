package RssFeedAnalyser.RFA.Services;

import RssFeedAnalyser.RFA.Exception.NoAnalysisIdException;
import RssFeedAnalyser.RFA.Models.AnalysisResult;
import RssFeedAnalyser.RFA.Models.PopularTopic;
import RssFeedAnalyser.RFA.Repositories.AnalysisResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class FrequencyService {
    @Autowired
   private AnalysisResultRepository analysisResultRepository;

    public List<PopularTopic> getAnalysisResult(UUID uuid) throws NoAnalysisIdException
    {
        AnalysisResult analysisResult = analysisResultRepository.findByUuid(uuid);
        if (analysisResult == null)
        {
            throw new NoAnalysisIdException("Could not find analysisResult with UUID" + uuid + " ");
        }
        return analysisResult.getPopularTopics();
    }
}
