package com.chilly.demo.config;
import com.chilly.demo.model.Book;
import java.util.List;
import java.util.ArrayList;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "book")
public class BookConfig {
    private String defaultAuthor;
    private Double maxPrice;
    private List<Book> defaultBooks = new ArrayList<>();
}
