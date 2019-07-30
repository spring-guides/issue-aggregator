package io.spring.guides.issueaggregator

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.net.URL
import org.springframework.web.method.support.ModelAndViewContainer

@SpringBootApplication
class IssueAggregatorApplication

fun main(args: Array<String>) {
    runApplication<IssueAggregatorApplication>(*args)
}

/**
 * Configuration properties to wire this application.
 */
@ConfigurationProperties(prefix = "description")
class Description() {

    lateinit var sections: MutableList<Section>

    override fun toString(): String {
        return sections.joinToString { it.toString() }
    }

    class Section() {

        lateinit var prefix: String
        lateinit var toStrip: String
        lateinit var section: String

        override fun toString(): String {
            return "$prefix $toStrip $section"
        }
    }
}

/**
 * DTO for extracting Github issue details.
 */
data class GithubIssue(
        val id: String,
        val url: URL,
        val repository_url: URL,
        val html_url: URL,
        val number: Int,
        val state: String,
        val title: String,
        var repo: String = "") {

    fun withRepo(repo: String): GithubIssue {
        this.repo = repo
        return this
    }
}

/**
 * Controller for rendering the website.
 */
@Controller
class IssueController(val description: Description, val webClientBuilder: WebClient.Builder) : InitializingBean {

    @Value("\${github.access.token}")
    lateinit var githubToken: String

    @Value("\${org:spring-guides}")
    lateinit var org: String

    @Value("\${site:http://spring.io}")
    lateinit var site: String

    @Value("\${css.selector:a.guide--title}")
    lateinit var cssSelector: String

    @Value("\${drone.site:http://travis-aggregator.guides.spring.io/}")
    lateinit var droneSite: String

    lateinit var webClient: WebClient

    @GetMapping("/")
    fun index(model: MutableMap<String, Any>): String {

        val repos = this.description.sections
                .flatMap { section ->
                    Jsoup.connect("$site${section.section}")
                            .get()
                            .select(cssSelector)
                            .filter { !isTutorial(it) && !isTopicalGuide(it) }
                            .map { section.prefix + (href(it).replace(section.toStrip, "").replace("/", "")) }
                }

        model["issues"] = findAllIssues(repos)
        model["site"] = this.site
        model["org"] = this.org
        model["droneSite"] = this.droneSite

        return "home"
    }

    /**
     * After {@link WebClient.Builder} is set, configure it with the {@literal Bearer} token.
     */
    override fun afterPropertiesSet() {
        
        webClient = webClientBuilder
                .defaultHeaders { it.setBearerAuth(githubToken) }
                .build()
    }

    /**
     * Take a list of repository names and find all open issues.
     */
    private fun findAllIssues(repos: List<String>): Flux<GithubIssue> {

        return Flux.fromIterable(repos)
                .flatMap { findIssuesByRepo(it) }
                .filter { it.state == "open" }
                .sort { issue1, issue2 ->
                    if (issue1.repo != issue2.repo) {
                        issue1.repo.compareTo(issue2.repo)
                    } else {
                        issue1.number.compareTo(issue2.number)
                    }
                }
    }

    /**
     * Contact github to find issues for a given repository, then sort by number.
     */
    private fun findIssuesByRepo(repoName: String): Flux<GithubIssue> {

        return webClient.get()
                .uri("https://api.github.com/repos/$org/$repoName/issues")
                .retrieve()
                .bodyToFlux(GithubIssue::class.java)
                .map { issue -> issue.withRepo(repoName) }
    }

    // Utility functions

    private fun href(it: Element) = it.attr("href")

    private fun isTutorial(it: Element) = href(it).contains("tutorials")

    private fun isTopicalGuide(it: Element) = href(it).contains("topicals")
}