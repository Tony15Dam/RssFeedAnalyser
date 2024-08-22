package RssFeedAnalyser.RFA.Response;

import RssFeedAnalyser.RFA.Models.PopularTopic;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class FrequencyResponse {

    private List<PopularTopic> popularTopics;
}
