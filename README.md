# Issue Aggregator [![Build Status](https://travis-ci.org/spring-guides/issue-aggregator.svg?branch=master)](https://travis-ci.org/spring-guides/issue-aggregator)

Are you managing multiple projects hosted at GitHub? Wish there was a way to see all the issues for your projects? Then you've come to the right place.

issue-aggregator is a [Spring Boot](https://projects.spring.io/spring-boot/) application that uses the power of [Spring Social GitHub](https://projects.spring.io/spring-social-github/) to query for issues across multiple repositories.

> **NOTE:** See https://bit.ly/app-mgmt-tools-with-boot for a webinar that walks through this app.

[![Webinar showing this app](https://i.ytimg.com/vi/j3rrqOV68ik/mqdefault.jpg)](https://bit.ly/app-mgmt-tools-with-boot)

## Getting Started

You first need to [install Spring Boot](https://github.com/spring-projects/spring-boot#installing-the-cli). I won't show that here because the instructions behind that link are pretty detailed. I'll just assume you got things set up right.

The next step is registering a GitHub [oauth token](https://spring.io/understanding/oauth). To do that follow these steps:

1. Go to https://github.com/settings/applications. (If you don't have a GitHub profile, this app isn't much use, ehh?)
2. Underneath **Personal Access Tokens** click [Create new token](https://github.com/settings/tokens/new).
3. You'll probably get prompted for your password. (That way, your co-worker can't sneak in during your coffee break and create a token without you knowing.)
4. In **Token Description** enter something like **issue-aggregator**.
5. Click **Create Token**. 
6. You'll be taken back to the list of apps, and should see a new, cryptic string with an icon offering to copy it to your clipboard similar to that shown below.

![](images/test-token.png)

## Run the application
Now you can run the application.

    GITHUB_ACCESS_TOKEN=<passcode> spring run app.groovy
    
This works if you run it locally. If you are going to push your app to CloudFoundry, you need to set it as an environment variable. You can do something like this:

    gcf set-env <app_name> GITHUB_ACCESS_TOKEN <passcode>
    
Then you can push all the updates you want. If you roll the passcode, you can simply reissue the command.
    
You should see something like this:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::             (v1.0.0.RC1)

2013-11-13 07:32:53.892  INFO 39907 --- [       runner-0] o.s.boot.SpringApplication               : Starting application on retina with PID 39907 (/Users/gturnquist/.m2/repository/org/springframework/boot/spring-boot/0.5.0.M6/spring-boot-0.5.0.M6.jar started by gturnquist)
2013-11-13 07:32:53.916  INFO 39907 --- [       runner-0] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@475deba3: startup date [Wed Nov 13 07:32:53 CST 2013]; root of context hierarchy
2013-11-13 07:32:54.347  INFO 39907 --- [       runner-0] f.a.AutowiredAnnotationBeanPostProcessor : JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
2013-11-13 07:32:54.817  INFO 39907 --- [       runner-0] o.apache.catalina.core.StandardService   : Starting service Tomcat
2013-11-13 07:32:54.817  INFO 39907 --- [       runner-0] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/7.0.42
2013-11-13 07:32:54.882  INFO 39907 --- [ost-startStop-1] org.apache.catalina.loader.WebappLoader  : Unknown loader org.springframework.boot.cli.compiler.ExtendedGroovyClassLoader$DefaultScopeParentClassLoader@1517843d class org.springframework.boot.cli.compiler.ExtendedGroovyClassLoader$DefaultScopeParentClassLoader
2013-11-13 07:32:54.887  INFO 39907 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2013-11-13 07:32:54.887  INFO 39907 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 971 ms
...
Using default password for application endpoints: 5d79bc95-78cf-44a2-bd92-27bc9bd15264
...
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

You can now see the list of issues at <http://localhost:8080>.

So, you finally make it to the issue list? It may look a little bare, but it works. More is coming.

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
