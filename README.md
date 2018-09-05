# wiptool

*A capability-centric Web tool to assist project managers in assembling teams.*

Overview
---
This Web application is powered by a RESTful API on the backend. The API is developed using Java whereas, the client-side interface of the application(front-end) is developed using AngularJS and HTML. The backend API is further designed to interact with PostgreSQL database and the API of project management tool- Redmine. The following figure presents an overview of the architecture of the tool.

<img src="https://github.com/vvksdatta/capabilityTool/blob/master/src/main/resources/assets/frontend/photos/BlockDiagram.png" data-canonical-src="https://github.com/vvksdatta/capabilityTool/blob/master/src/main/resources/assets/frontend/photos/BlockDiagram.png" width="500" height="500"/>

Diving into the details
---

The backend It's a little bit of opinionated glue code which bangs together a set of libraries which have
historically not sucked:

* [Jetty](http://www.eclipse.org/jetty/) for HTTP servin'.
* [Jersey](http://jersey.java.net/) for REST modelin'.
* [Jackson](https://github.com/FasterXML/jackson) for JSON parsin' and generatin'.
* [Logback](http://logback.qos.ch/) for loggin'.
* [Hibernate Validator](http://hibernate.org/validator/) for validatin'.
* [Metrics](http://metrics.dropwizard.io) for figurin' out what your application is doin' in production.
* [JDBI](http://www.jdbi.org) and [Hibernate](http://www.hibernate.org/orm/) for databasin'.
* [Liquibase](http://www.liquibase.org/) for migratin'.

Read more at [dropwizard.io](http://www.dropwizard.io).

How to start the wiptool application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/wiptool-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
