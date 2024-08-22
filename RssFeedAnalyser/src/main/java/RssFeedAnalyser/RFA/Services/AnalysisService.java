package RssFeedAnalyser.RFA.Services;

import RssFeedAnalyser.RFA.Constants.Constants;
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
import org.springframework.stereotype.Service;

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

        for (String url: urls)
        {
            URL feedUrl = new URL(url);
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(feedUrl));

            for (SyndEntry entry: feed.getEntries())
            {
                String title = entry.getTitle();
                String link = entry.getLink();
                List<String> entryTopics = parseNounsFromTitle(title.toLowerCase());
                NewsArticle newsArticle = buildNewsArticle(title, link);


                for (String topic: entryTopics)
                {
                    if(!Constants.stopWords.contains(topic))
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
        saveAnalysisResult(popularTopicsList, uuid);

        return uuid;
    }

    private NewsArticle buildNewsArticle(String title, String link)
    {
        return NewsArticle.builder()
                .title(title)
                .link(link)
                .build();
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
            PopularTopic popularTopic = PopularTopic.builder()
                    .topic(topic)
                    .newsArticles(topicsMap.get(topic).stream().toList())
                    .build();

            popularTopicsList.add(popularTopic);
        }
        return popularTopicsList;
    }

    private UUID generateAnalysisUUID()
    {
        return UUID.randomUUID();
    }

    private AnalysisResult buildAnalysisResultEntity(List<PopularTopic> popularTopicList, UUID uuid)
    {
        return AnalysisResult.builder()
                .uuid(uuid)
                .popularTopics(popularTopicList)
                .build();
    }

    private void saveAnalysisResult(List<PopularTopic> popularTopicList, UUID uuid)
    {
        AnalysisResult analysisResult = buildAnalysisResultEntity(popularTopicList, uuid);
        analysisResultRepository.save(analysisResult);
    }
}
