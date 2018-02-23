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

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoClient;
import com.mongodb.session.ClientSession;

/**
 * @author Christoph Strobl
 * @since 2018/02
 */
public class ClientSessionTests {

	MongoTemplate template;
	MongoClient client;

	@Before
	public void setUp() {

		client = new MongoClient();
		template = new MongoTemplate(client, "reflective-client-session-tests");
		template.getDb().getCollection("test").drop();

		template.getDb().getCollection("test").insertOne(new Document("_id", "id-1").append("value", "spring"));

	}

	@Test
	public void xxx() {

		Document x1 = template.findOne(new Query(), Document.class, "test");

		System.out.println("x1: " + x1);

		// ClientSession session = client.startSession(ClientSessionOptions.builder().causallyConsistent(true).build());
		// template.setSession(session);
		//
		ClientSession session = client.startSession(ClientSessionOptions.builder().causallyConsistent(true).build());

		Document x2 = template.execute(() -> session, ops -> {
			return ops.findOne(new Query(), Document.class, "test");
		});

		System.out.println("x2: " + x2);
		// Document x2 = template.findOne(new Query(), Document.class, "test");
		//
		// System.out.println("x2: " + x2);
		//
		// System.out.println("session.: " + session.getOperationTime());

	}

}
