package com.batch.mongo.csv.entity.config;

import org.springframework.batch.item.ItemProcessor;

import com.batch.mongo.csv.entity.MongoDBEntity;

public class MongoDBProcessor implements
		ItemProcessor<MongoDBEntity, MongoDBEntity> {

	@Override
	public MongoDBEntity process(MongoDBEntity entity) throws Exception {
		return entity;
	}

}
