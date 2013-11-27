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

@Grab("org.springframework.social:spring-social-github:1.0.0.BUILD-SNAPSHOT")

import org.springframework.social.github.api.*
import org.springframework.social.github.api.impl.*
import org.springframework.web.client.HttpClientErrorException

/**
 * Scan all Spring Guides for open issues and show them in one place
 *
 * @author Greg Turnquist
 */
@Grab("thymeleaf-spring3")
@Grab("spring-boot-starter-actuator")
@Grab("spring-boot-starter-security")
@Grab("spring-web")
@Controller
@Log
class IssueAggregator {

    /**
     * This needs to be supplied by application.properties, a file NOT to be put under source control
     */
    @Value('${github.access.token}')
    String githubToken

    @Value('${org:spring-guides}')
    String org

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
            "tut-web", "tut-rest", "tut-data",
            "gs-consuming-rest-ios", "gs-messaging-stomp-websocket", "gs-consuming-rest-backbone",
            "gs-consuming-rest-restjs", "gs-consuming-rest-angularjs", "gs-consuming-rest-jquery",
            "gs-consuming-rest-sencha", "draft-gs-messaging-stomp-websocket-angular", "gs-rest-service-cors",
            "draft-gs-template"
    ]

    @Bean
    GitHubTemplate githubTemplate() {
        new GitHubTemplate(githubToken)
    }

    @Autowired
    GitHubTemplate githubTemplate

    def issues() {
        repos.collect { repoName ->
            githubTemplate.repoOperations().getIssues(org, repoName).findAll { it.state == "open" }.sort { it.number }.collect {
                [repo: repoName, issue: it]
            }
        }.findAll { it.size() > 0 }.flatten()
    }

    @RequestMapping("/")
    String index(Map<String, Object> model) {
		model.put("issues", issues())
		"home"
    }

}
