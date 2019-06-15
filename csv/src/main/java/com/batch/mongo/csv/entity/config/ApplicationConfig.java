package com.batch.mongo.csv.entity.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.batch.mongo.csv.entity.MongoDBEntity;

@Configuration
@ConfigurationProperties(prefix = "job")
public class ApplicationConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Value("${job.rundate}")
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date dateValue;

	public JobBuilderFactory getJobBuilderFactory() {
		return jobBuilderFactory;
	}

	public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
	}

	public StepBuilderFactory getStepBuilderFactory() {
		return stepBuilderFactory;
	}

	public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
		this.stepBuilderFactory = stepBuilderFactory;
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Bean
	public MongoItemReader<MongoDBEntity> reader() throws ParseException {
		Date endValue = getResultEndDate();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Criteria dateC = Criteria.where("dob")
				.gt(LocalDate.parse(dateFormat.format(dateValue), dtf))
				.lt(LocalDate.parse(dateFormat.format(endValue), dtf));
		Query qu = new Query();
		qu.addCriteria(dateC);
		System.out.println("start values is "+ dateFormat.format(dateValue));
		System.out.println("End values is "+ dateFormat.format(endValue));
		System.out.println("REader");
		System.out.println("date value is " + dateValue);
		MongoItemReader<MongoDBEntity> reader = new MongoItemReader<MongoDBEntity>();
		reader.setTemplate(mongoTemplate);
		// String querr = "{dob:{$gt:"+da+")}}";
		// System.out.println(querr);
		reader.setQuery(qu);
		reader.setTargetType(MongoDBEntity.class);
		reader.setTargetType((Class<? extends MongoDBEntity>) MongoDBEntity.class);
		reader.setSort(new HashMap<String, Sort.Direction>() {
			{
				put("_id", Direction.ASC);
			}
		});
		return reader;

	}

	private Date getResultEndDate() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-mm-dd hh:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(dateValue);
		//c.setTime(dateFormat.parse(dateValue));
		c.add(Calendar.DAY_OF_YEAR, 2);
		return c.getTime();
	}

	@Bean
	public FlatFileItemWriter<MongoDBEntity> writer() {
		System.out.println("Writer");
		FlatFileItemWriter<MongoDBEntity> writer = new FlatFileItemWriter<MongoDBEntity>();
		writer.setResource(new FileSystemResource("c://outputs//temp.all.csv"));
		writer.setLineAggregator(new DelimitedLineAggregator<MongoDBEntity>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<MongoDBEntity>() {
					{
						setNames(new String[] { "id", "name", "dob",
								"description", "mailid", "state" });
					}
				});
			}
		});

		return writer;
	}

	@Bean
	public Step step1() throws ParseException {
		return stepBuilderFactory.get("step1")
				.<MongoDBEntity, MongoDBEntity> chunk(10).reader(reader())
				.writer(writer()).build();
	}

	@Bean
	public Job exportUserJob() throws ParseException {
		return jobBuilderFactory.get("exportUserJob")
				.incrementer(new RunIdIncrementer()).flow(step1()).end()
				.build();
	}

	@Bean
	public CustomConversions mongoCustomConversions() {
		return new CustomConversions(Collections.emptyList());
	}

}
