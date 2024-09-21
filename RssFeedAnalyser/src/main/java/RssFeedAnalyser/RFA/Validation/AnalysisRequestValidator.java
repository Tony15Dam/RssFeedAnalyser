package RssFeedAnalyser.RFA.Validation;

import RssFeedAnalyser.RFA.Exception.SizeLimitMinimumException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class AnalysisRequestValidator {
    public void validateAnalysisRequest(List<String> urls)
    {
        if (urls.size() < 2)
        {
            throw new SizeLimitMinimumException("Request should contain at least two urls");
        }
    }
}
