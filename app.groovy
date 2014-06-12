/*
 * Copyright 2012-2014 the original author or authors.
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
@Grab('org.jsoup:jsoup:1.6.1')

import org.jsoup.nodes.Document
import org.jsoup.Jsoup

import org.springframework.social.github.api.*
import org.springframework.social.github.api.impl.*
import org.springframework.web.client.HttpClientErrorException


/**
 * Scan all Spring Guides for open issues and show them in one place
 *
 * @author Greg Turnquist
 */
@Grab("thymeleaf-spring4")
@Grab("spring-web")
@Grab("jackson-databind")
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

    @Bean
    GitHubTemplate githubTemplate() {
        new GitHubTemplate(githubToken)
    }

    @Autowired
    GitHubTemplate githubTemplate

    def issues(def repos) {
        repos.collect { repoName ->
            githubTemplate.repoOperations().getIssues(org, repoName).findAll { it.state == "open" }.sort { it.number }.collect {
                [repo: repoName, issue: it]
            }
        }.findAll { it.size() > 0 }.flatten()
    }

    @RequestMapping("/")
    String index(Map<String, Object> model) {
        // Scan for all guides
        Document doc = Jsoup.connect("http://spring.io/guides").get()

        def repos = doc.select("a.guide--title")
                .findAll { !it.attr("href").contains("tutorials") }
                .collect {
            "gs-" + (it.attr("href") - "/guides/gs/" - "/")
        }

        model.put("issues", issues(repos))
		"home"
    }

}
