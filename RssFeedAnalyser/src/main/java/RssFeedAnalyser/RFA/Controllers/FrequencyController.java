package RssFeedAnalyser.RFA.Controllers;

import RssFeedAnalyser.RFA.Models.PopularTopic;
import RssFeedAnalyser.RFA.Response.FrequencyResponse;
import RssFeedAnalyser.RFA.Services.FrequencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/frequency")
public class FrequencyController {
    @Autowired
    private FrequencyService frequencyService;

    @GetMapping("/{uuid}")
    public ResponseEntity<FrequencyResponse> getPopularTopics(@PathVariable UUID uuid)
    {
        List<PopularTopic> popularTopics = frequencyService.getAnalysisResult(uuid);
        FrequencyResponse frequencyResponse = new FrequencyResponse(popularTopics);
        return new ResponseEntity<FrequencyResponse>(frequencyResponse, HttpStatus.OK);
    }
}
