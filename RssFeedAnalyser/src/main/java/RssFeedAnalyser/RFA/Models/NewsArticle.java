package RssFeedAnalyser.RFA.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NewsArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String title;
    private String link;

    // Special constructor used for static the static builder method
    private NewsArticle(String title, String link)
    {
        this.title = title;
        this.link = link;
    }

    public static NewsArticle buildNewsArticle(String title, String link)
    {
        return new NewsArticle(title, link);
    }

}
