package RssFeedAnalyser.RFA.Controllers;

import RssFeedAnalyser.RFA.Requests.AnalysisRequest;
import RssFeedAnalyser.RFA.Response.AnalysisResponse;
import RssFeedAnalyser.RFA.Services.AnalysisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analyse/new")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<AnalysisResponse> analysis(@RequestBody AnalysisRequest requestBody)
    {
        List<String> urls = requestBody.getUrls();
        UUID analysisId = analysisService.AnalyseFeeds(urls);
        AnalysisResponse analysisResponse = new AnalysisResponse(analysisId);
        return new ResponseEntity<AnalysisResponse>(analysisResponse, HttpStatus.OK);
    }
}
