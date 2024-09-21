package RssFeedAnalyser.RFA.Requests;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Data
public class AnalysisRequest {

    private List<String> urls;
}
