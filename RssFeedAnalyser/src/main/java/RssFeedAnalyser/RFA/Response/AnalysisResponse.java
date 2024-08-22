package RssFeedAnalyser.RFA.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalysisResponse {

    private UUID uuid;
}
