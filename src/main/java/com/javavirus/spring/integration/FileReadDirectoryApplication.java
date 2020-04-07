package com.javavirus.spring.integration;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.file.Files;
import org.springframework.integration.file.transformer.FileToStringTransformer;

import com.javavirus.spring.integration.filter.LastModifiedFileFilter;
import com.javavirus.spring.integration.processor.FileProcessor;

@SpringBootApplication
public class FileReadDirectoryApplication {
	private static final String READ_DIRECTORY = "/Users/javavirus/filereaddirectory/";

    public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(FileReadDirectoryApplication.class, args);
	}

	@Bean
	public IntegrationFlow integrationFlow() {
	    return IntegrationFlows.from(Files.inboundAdapter(new File(READ_DIRECTORY)).
	                    filter(new LastModifiedFileFilter()),
	            c -> c.poller(Pollers.fixedDelay(1000))).
	    		transform(fileToStringTransformer()).
	            handle("fileProcessor", "process").
	            get();
	}
	@Bean
	public FileToStringTransformer fileToStringTransformer() {
		return new FileToStringTransformer();
	}

	@Bean
	public FileProcessor fileProcessor() {
		return new FileProcessor();
	}
	

}
