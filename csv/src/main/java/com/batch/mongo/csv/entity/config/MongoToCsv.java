package com.batch.mongo.csv.entity.config;

import java.util.Date;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableBatchProcessing
@Configuration
@EnableAutoConfiguration
//@PropertySource(value="${external.app.properties}", ignoreResourceNotFound=false)
public class MongoToCsv implements CommandLineRunner{
	
	@Autowired
	private JobProperties jobProperties;
	
	
	
	public static void main(String[] args) {
		System.out.println("Main method");
		SpringApplication.run(MongoToCsv.class, args);
	}

	public JobProperties getJobProperties() {
		return jobProperties;
	}

	public void setJobProperties(JobProperties jobProperties) {
		this.jobProperties = jobProperties;
	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println("run method " );		
	}
	
	
}
