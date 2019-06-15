/*package com.batch.mongo.csv.entity.config;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import com.batch.mongo.csv.entity.MongoDBEntity;

@Configuration
public class ApplicationConfig2 {

	@Autowired
	private JobBuilderFactory jobBuilderFactoryCsv;
	@Autowired
	private StepBuilderFactory stepBuilderFactoryCsv;

	@Bean
	public FlatFileItemReader<MongoDBEntity> readerCsv() {
		return new FlatFileItemReaderBuilder<MongoDBEntity>()
                .name("file-reader")
                .resource(new FileSystemResource(
        				"c://outputs//temp.all.csv"))
                .targetType(MongoDBEntity.class)
                .linesToSkip(1)
                .delimited().delimiter(",").names(new String[]{"id", "name" , "dob", "descritpion", "mailid", "state"})
                .build();
	}

	@Bean
	public FlatFileItemWriter<MongoDBEntity> writerCsv() {
		System.out.println("Writer");
		FlatFileItemWriter<MongoDBEntity> writer = new FlatFileItemWriter<MongoDBEntity>();
		writer.setResource(new FileSystemResource(
				"c://outputs//temp.all3.csv"));
		writer.setLineAggregator(new DelimitedLineAggregator<MongoDBEntity>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<MongoDBEntity>() {
					{
						setNames(new String[] {"id", "name" , "dob", "description", "mailid", "state" });
					}
				});
			}
		});

		return writer;
	}

	@Bean
	public Step step2() {
		return stepBuilderFactoryCsv.get("step2")
				.<MongoDBEntity, MongoDBEntity> chunk(10).reader(readerCsv())
				.writer(writerCsv()).build();
	}

	@Bean
	public Job exportUserJobCsv() {
		return jobBuilderFactoryCsv.get("exportUserJobCsv")
				.incrementer(new RunIdIncrementer()).flow(step2()).end()
				.build();
	}
	
	@Bean
	public CustomConversions mongoCustomConversions() {
		return new CustomConversions(Collections.emptyList());
	}

}
*/