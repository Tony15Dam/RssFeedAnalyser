package RssFeedAnalyser.RFA.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PopularTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String topic;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "popular_topic_news_article",
    joinColumns = @JoinColumn(name = "popular_topic_id"))
    private List<NewsArticle> newsArticles;

    private PopularTopic(String topic, List<NewsArticle> newsArticles)
    {
        this.topic = topic;
        this.newsArticles = newsArticles;
    }

    public static PopularTopic buildPopularTopic(String topic, List<NewsArticle> newsArticles)
    {
        return new PopularTopic(topic, newsArticles);
    }
}
