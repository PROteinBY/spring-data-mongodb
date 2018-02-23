/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mongodb.core;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate.SessionBoundMongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.session.ClientSession;

/**
 * @author Christoph Strobl
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SessionBoundMongoTemplateUnitTests {

	SessionBoundMongoTemplate template;

	@Mock MongoDbFactory factory;
	@Mock MongoCollection collection;
	@Mock MongoDatabase database;
	@Mock ClientSession clientSession;

	MappingMongoConverter converter;
	MongoMappingContext mappingContext;

	@Before
	public void setUp() {

		when(factory.getDb()).thenReturn(database);
		when(database.getCollection(any(), any())).thenReturn(collection);

		this.mappingContext = new MongoMappingContext();
		this.converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), mappingContext);
		this.template = new SessionBoundMongoTemplate(clientSession, factory, converter);
	}

	@Test // DATAMONGO-1880
	public void delegatesFindToFindWithSession() {

		template.prepareCollection(collection).find();

		verify(collection, never()).find();
		verify(collection).find(clientSession);
	}

}
