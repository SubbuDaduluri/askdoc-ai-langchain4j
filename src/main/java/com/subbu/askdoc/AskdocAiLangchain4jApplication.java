package com.subbu.askdoc;

import com.subbu.askdoc.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AskdocAiLangchain4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(AskdocAiLangchain4jApplication.class, args);
	}

}
