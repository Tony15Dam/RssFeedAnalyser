package RssFeedAnalyser.RFA.Services;

import RssFeedAnalyser.RFA.Models.AnalysisResult;
import RssFeedAnalyser.RFA.Models.NewsArticle;
import RssFeedAnalyser.RFA.Models.PopularTopic;
import RssFeedAnalyser.RFA.Repositories.AnalysisResultRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class AnalysisService {

    private static final int numberOfStoredTopics = 3;
    @Autowired
    private AnalysisResultRepository analysisResultRepository;

    public UUID AnalyseFeeds(List<String> urls) throws Exception
    {
        Set<String> allTopicsSet = new HashSet<>();
        Map<String, Set<NewsArticle>> topicsMap = new HashMap<>();
        Set<String> stopWords = loadStopWords();

        for (String url: urls)
        {
            URL feedUrl = new URL(url);
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(feedUrl));

            for (SyndEntry entry: feed.getEntries())
            {
                String title = entry.getTitle();
                String link = entry.getLink();
                List<String> entryTopics = parseNounsFromTitle(title.toLowerCase());
                NewsArticle newsArticle = NewsArticle.buildNewsArticle(title, link);


                for (String topic: entryTopics)
                {
                    if(!stopWords.contains(topic))
                    {
                        allTopicsSet.add(topic);
                        if (!topicsMap.containsKey(topic))
                        {
                            topicsMap.put(topic, new HashSet<>());
                        }
                        topicsMap.get(topic).add(newsArticle);
                    }
                }
            }
        }

        List<PopularTopic> popularTopicsList = buildPopularTopicsList(allTopicsSet, topicsMap);
        UUID uuid = generateAnalysisUUID();
        saveAnalysisResult(uuid, popularTopicsList);

        return uuid;
    }

    private Set<String> loadStopWords() throws Exception {
        Set<String> stopWords = new HashSet<>();

        // Load the stopwords.txt file from resources
        InputStream resource = new ClassPathResource("stopwords.txt").getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource));

        String line;
        while ((line = reader.readLine()) != null)
        {
            stopWords.add(line.trim().toLowerCase());
        }
        reader.close();

        return stopWords;
    }

    private List<String> parseNounsFromTitle(String title)
    {
        return Arrays.stream(title.split("\\W+"))
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> determinePopularTopics(Set<String> allTopicsSet, Map<String, Set<NewsArticle>> topicsMap)
    {
        List<String> sortedTopics = new ArrayList<>(allTopicsSet.stream().toList());

        sortedTopics.sort((topic1, topic2) ->
        {
           int articleCount1 = topicsMap.get(topic1).size();
           int articleCount2 = topicsMap.get(topic2).size();
           return Integer.compare(articleCount2, articleCount1);
        });

        return sortedTopics.stream()
                .limit(numberOfStoredTopics)
                .collect(Collectors.toList());
    }

    private List<PopularTopic> buildPopularTopicsList(Set<String> allTopicsSet, Map<String, Set<NewsArticle>> topicsMap)
    {
        List<String> popularTopicsStrings = determinePopularTopics(allTopicsSet, topicsMap);
        List<PopularTopic> popularTopicsList = new ArrayList<>();
        for (String topic: popularTopicsStrings)
        {
            final Integer overlaps = topicsMap.get(topic).size() - 1;
            // Only build topics that have overlaps, i.e. more than one newsArticle
            if (overlaps > 0)
            {
                PopularTopic popularTopic = PopularTopic
                        .buildPopularTopic(topic, topicsMap.get(topic).stream().toList());

                popularTopicsList.add(popularTopic);
            }
        }
        return popularTopicsList;
    }

    private UUID generateAnalysisUUID()
    {
        return UUID.randomUUID();
    }

    private void saveAnalysisResult(UUID uuid, List<PopularTopic> popularTopicList)
    {
        AnalysisResult analysisResult = AnalysisResult.buildAnalysisResult(uuid, popularTopicList);
        analysisResultRepository.save(analysisResult);
    }
}
