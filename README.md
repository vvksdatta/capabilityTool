# CAST

***C**apability-centric **A**gile **S**upport **T**ool - A Web application to assist project managers in assembling teams.*

Overview
---
This Web application enables the integartion of capability assessments with regular project management routines. Such integration gives flexibility to managers for creating projects and keeping track of capabilities, sprint requirements and allocation routines, all from one tool while rest of the team members interact with regular project management platform and devote their complete focus towards accomplishing sprint goals.

This application is specially designed to comply with open-source project management tool [Redmine](http://www.redmine.org/).  

This Web application is powered by a RESTful API on the backend, developed using Java. On the other hand, the client-side interface of the application(front-end) is developed using AngularJS and HTML. The backend API is further designed to interact with 

* RESTful API of project management tool - [Redmine](http://www.redmine.org/). 
* [PostgreSQL](https://www.postgresql.org/) database to store capability information and project related details from Redmine.

The following figure presents an overview of the architecture of the tool.

<img src="https://github.com/vvksdatta/capabilityTool/blob/master/src/main/resources/assets/frontend/photos/BlockDiagram.png" data-canonical-src="https://github.com/vvksdatta/capabilityTool/blob/master/src/main/resources/assets/frontend/photos/BlockDiagram.png" width="550" height="500"/>

Diving into the details of the architecture
---
**RESTful API of Redmine**

The right most block in the above listed diagram represents the RESTful API of Redmine. With the help of an administrator's API access-key, it would be possible to communicate with Redmine using REST calls, for fetching and manipulating information of projects, people, issues and other entitities. Thus, the URL of Redmine server and API access-key of administartor are used by another block (**dropwizard based RESTful API**) to communicate with Redmine. These details are refereed from the [configuration file] (https://github.com/vvksdatta/capabilityTool/blob/master/config.yml).  

**PostgreSQL database**

A PostgreSQl database is integrated to the Web application to maintain a copy of information related to various entities on Redmine. This information is necessary in order to further establish relationships between entities like people and different apsects like programming skills, capabilities, etc.  

**Dropwizard based RESTful API**

The central block in the above listed diagram is a key elememt that acts as a bridge between [Redmine](http://www.redmine.org/) and [PostgreSQL](https://www.postgresql.org/) database. This block communicates with the RESTful API of Redmine in order to fetch and store information of various entities to the database. The block is developed using [Dropwizard](https://www.dropwizard.io/1.2.0/docs/) framework, that implements all the functionalities needed for running a Web appliation. Dropwizard bundles multiple libraries together such as: 

* [Jetty](http://www.eclipse.org/jetty/) for HTTP services.
* [Jersey](http://jersey.java.net/) for RESTful web application.
* [Jackson](https://github.com/FasterXML/jackson) for parsing and generating JSON.
* [JDBI](http://www.jdbi.org) for database interactions.
 
With the help of Jersey and Jackson services, the dropwizard project herein hosts multiple resources like projects, sprints, issues, etc. and contains multiple REST end points to serve and manipulate information related to these resources, using HTTP methods (GET, PUT, POST and DELETE). 

Further, the main method of dropwizard project spins up an HTTP server using [Jetty](http://www.eclipse.org/jetty/). This server hosts all the Web pages and other static files associated with the client-side interface of the application(front-end).  

How to start the wiptool application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/wiptool-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
