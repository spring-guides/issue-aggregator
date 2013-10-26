/*
 * Copyright 2012-2013 the original author or authors.
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

package io.spring.guideissues

@Grab("org.springframework:spring-web:4.0.0.M3")
@Grab("org.springframework.social:spring-social-github:1.0.0.BUILD-SNAPSHOT")

import org.springframework.social.github.api.impl.*
import org.springframework.web.client.HttpClientErrorException

/**
 * Scan all Spring Guides for open issues and show them in one place
 *
 * @author Greg Turnquist
 */
@Configuration
@Log
class IssueAggregator implements CommandLineRunner {

	/**
	 * This needs to be supplied by application.properties, a file NOT to be put under source control
	 */
	@Value('${token}')
	String githubToken

	String[] repos = [
		"gs-accessing-data-gemfire", "gs-accessing-data-jpa", "gs-accessing-data-mongo", "gs-accessing-data-neo4j",
		"gs-accessing-facebook", "gs-accessing-twitter", "gs-actuator-service", "gs-android", "gs-async-method",
		"gs-authenticating-ldap", "gs-batch-processing", "gs-caching-gemfire", "gs-consuming-rest", "gs-consuming-rest-android",
		"gs-consuming-rest-xml-android", "gs-convert-jar-to-war", "gs-device-detection", "gs-gradle",
		"gs-gradle-android", "gs-handling-form-submission", "gs-managing-transactions", "gs-maven",
		"gs-maven-android", "gs-messaging-jms", "gs-messaging-rabbitmq", "gs-messaging-reactor",
		"gs-messaging-redis", "gs-register-facebook-app", "gs-register-twitter-app", "gs-relational-data-access",
		"gs-rest-hateoas", "gs-rest-service", "gs-scheduling-tasks", "gs-securing-web", "gs-serving-web-content",
		"gs-spring-boot", "gs-sts", "gs-uploading-files",
		"tut-web", "tut-rest", "tut-data"]

	@Bean
	GitHubTemplate githubTemplate() {
		new GitHubTemplate(githubToken)
	}

	@Autowired
	GitHubTemplate githubTemplate

	/**
	 * It appears that @Log log doesn't work in the closure down below, so it's captured here in a 'final'
	 */
	final def logger = log

	void run(String... args) {
		try {
			repos.each { repoName ->
				githubTemplate.repoOperations().getIssues("spring-guides", repoName).findAll { it.state == "open" }.each {
					logger.info "${repoName}: <${it.title}> at http://github.com/spring-guides/${repoName}/issues/${it.number}"
				}
			}
		} catch (HttpClientErrorException e) {
			log.info e.message
			e.responseHeaders.each {
				log.info "Header => ${it}"
			}
			e.responseBodyAsString
		}
	}
}