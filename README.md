# wiptool

*A capability-centric Web tool to assist project managers in assembling teams.*

Overview
---
This Web application is powered by a RESTful API on the backend. The API is developed using Java whereas, the client-side interface of the application(front-end) is developed using AngularJS and HTML. The backend API is further designed to interact with PostgreSQL database and API of project management tool- Redmine. The following figure presents an overview of the architecture of the tool.

<img src="https://github.com/vvksdatta/capabilityTool/blob/master/src/main/resources/assets/frontend/photos/BlockDiagram.png" data-canonical-src="https://github.com/vvksdatta/capabilityTool/blob/master/src/main/resources/assets/frontend/photos/BlockDiagram.png" width="550" height="500"/>

Diving into the details of backend
---
**Dropwizard based RESTful API**

The central block presented in the above listed diagram is a key elememt that acts a bridge between [Redmine](http://www.redmine.org/) and [PostgreSQL](https://www.postgresql.org/) database. It is developed using [Dropwizard](https://www.dropwizard.io/1.2.0/docs/) framework, that implements all the functionalities needed for running a Web appliation. Dropwizard bundles multiple libraries together such as: 

* [Jetty](http://www.eclipse.org/jetty/) for HTTP services.
* [Jersey](http://jersey.java.net/) for RESTful web application.
* [Jackson](https://github.com/FasterXML/jackson) for parsing and generating JSON.
* [JDBI](http://www.jdbi.org) for database interactions.

How to start the wiptool application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/wiptool-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
