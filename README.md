Are you managing multiple projects hosted at GitHub? Wish there was a way to see all the issues for your projects? Then you've come to the right place.

issue-aggregator is a [Spring Boot](http://projects.spring.io/spring-boot/) application that uses the power of [Spring Social GitHub](http://projects.spring.io/spring-social-github/) to query for issues across multiple repositories.

## Getting Started

You first need to [install Spring Boot](https://github.com/spring-projects/spring-boot#installing-the-cli). I won't show that here because the instructions behind that link are pretty detailed. I'll just assume you got things set up right.

The next step is registering a GitHub [oauth token](http://spring.io/understanding/oauth). To do that follow these steps:

1. Go to https://github.com/settings/applications. (If you don't have a GitHub profile, this app isn't much use, ehh?)
2. Underneath **Personal Access Tokens** click [Create new token](https://github.com/settings/tokens/new).
3. You'll probably get prompted for your password. (That way, your co-worker can't sneak in during your coffee break and create a token without you knowing.)
4. In **Token Description** enter something like **issue-aggregator**.
5. Click **Create Token**. 
6. You'll be taken back to the list of apps, and should see a new, cryptic string with an icon offering to copy it to your clipboard similar to that shown below.

![](images/test-token.png)

Create a new file right next to app.groovy. Call it **application.properties**. Grab that cryptic bit of text from up above, and add it to your new file.

```properties
token=<new cryptic text>
```

By assigning it to token, it will grant this application access to your GitHub issues.

## Run the application
Now you can run the application.

    spring run app.groovy
    
You should see something like this:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::             (v0.5.0.M6)

2013-11-13 07:32:53.892  INFO 39907 --- [       runner-0] o.s.boot.SpringApplication               : Starting application on retina with PID 39907 (/Users/gturnquist/.m2/repository/org/springframework/boot/spring-boot/0.5.0.M6/spring-boot-0.5.0.M6.jar started by gturnquist)
2013-11-13 07:32:53.916  INFO 39907 --- [       runner-0] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@475deba3: startup date [Wed Nov 13 07:32:53 CST 2013]; root of context hierarchy
2013-11-13 07:32:54.347  INFO 39907 --- [       runner-0] f.a.AutowiredAnnotationBeanPostProcessor : JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
2013-11-13 07:32:54.817  INFO 39907 --- [       runner-0] o.apache.catalina.core.StandardService   : Starting service Tomcat
2013-11-13 07:32:54.817  INFO 39907 --- [       runner-0] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/7.0.42
2013-11-13 07:32:54.882  INFO 39907 --- [ost-startStop-1] org.apache.catalina.loader.WebappLoader  : Unknown loader org.springframework.boot.cli.compiler.ExtendedGroovyClassLoader$DefaultScopeParentClassLoader@1517843d class org.springframework.boot.cli.compiler.ExtendedGroovyClassLoader$DefaultScopeParentClassLoader
2013-11-13 07:32:54.887  INFO 39907 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2013-11-13 07:32:54.887  INFO 39907 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 971 ms
2013-11-13 07:32:55.092  INFO 39907 --- [ost-startStop-1] o.apache.tomcat.util.digester.Digester   : TLD skipped. URI: http://www.springframework.org/tags is already defined
2013-11-13 07:32:55.096  INFO 39907 --- [ost-startStop-1] o.apache.tomcat.util.digester.Digester   : TLD skipped. URI: http://www.springframework.org/tags/form is already defined
2013-11-13 07:32:55.134  INFO 39907 --- [ost-startStop-1] o.apache.tomcat.util.digester.Digester   : TLD skipped. URI: http://www.springframework.org/spring-social/social/tags is already defined
2013-11-13 07:32:55.192  INFO 39907 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring FrameworkServlet 'dispatcherServlet'
2013-11-13 07:32:55.192  INFO 39907 --- [ost-startStop-1] o.s.web.servlet.DispatcherServlet        : FrameworkServlet 'dispatcherServlet': initialization started
2013-11-13 07:32:55.276  INFO 39907 --- [ost-startStop-1] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2013-11-13 07:32:55.446  INFO 39907 --- [ost-startStop-1] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/view],methods=[],params=[],headers=[],consumes=[],produces=[],custom=[]}" onto public java.lang.String io.spring.guideissues.IssueAggregator.view(java.util.Map<java.lang.String, java.lang.Object>)
2013-11-13 07:32:55.446  INFO 39907 --- [ost-startStop-1] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/],methods=[],params=[],headers=[],consumes=[],produces=[],custom=[]}" onto public java.lang.String io.spring.guideissues.IssueAggregator.index()
2013-11-13 07:32:55.522  INFO 39907 --- [ost-startStop-1] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2013-11-13 07:32:55.522  INFO 39907 --- [ost-startStop-1] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2013-11-13 07:32:55.962  INFO 39907 --- [ost-startStop-1] o.s.web.servlet.DispatcherServlet        : FrameworkServlet 'dispatcherServlet': initialization completed in 770 ms
Resolving dependencies..
2013-11-13 07:32:58.334  INFO 39907 --- [       runner-0] o.s.boot.SpringApplication               : Started application in 4.87 seconds
```

You can now visit a non-templated version with basic HTML at <http://localhost:8080>. I'm still working at getting templates working.

## Customizing the organizations and repos that are reported

Right now, it looks at github.com/spring-guides to find repos. But thanks to this:

```groovy
    @Value('${org:spring-guides}')
    String org
```

…you can customize the organization. Just put this in your **application.properties** file.

```properties
org=<my org>
```

These are the repos that I watch:

```groovy
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
```

You just need to change the list.
