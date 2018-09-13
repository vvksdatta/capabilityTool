# CAST

***C**apability-centric **A**gile **S**upport **T**ool - A Web application to assist project managers for configuring teams.*

Overview
---
This Web application enables the integartion of capability assessments with regular project management routines. Such integration gives flexibility to managers for creating projects and keeping track of capabilities, sprint requirements and allocation routines, all from one tool while rest of the team members interact with regular project management platform and devote their complete focus towards accomplishing sprint goals.

This application is specially designed to comply with open-source project management tool [Redmine](http://www.redmine.org/).  

This Web application is powered by a RESTful API on the backend, developed using Java. On the other hand, the client-side interface of the application(front-end) is developed using AngularJS and HTML. The backend API is further designed to interact with 

* RESTful API of project management tool - [Redmine](http://www.redmine.org/). 
* [PostgreSQL](https://www.postgresql.org/) database to store capability information and project related details from Redmine.

The following figure presents an overview of the architecture of the tool.

| <img src="https://github.com/vvksdatta/capabilityTool/blob/master/src/main/resources/assets/frontend/photos/BlockDiagram.png" data-canonical-src="https://github.com/vvksdatta/capabilityTool/blob/master/src/main/resources/assets/frontend/photos/BlockDiagram.png" width="550" height="500"/> |
|:--:| 
| *Block diagram of Web application* |

Diving into the details of architecture
---
**RESTful API of Redmine**

The right most block in the above listed diagram represents the RESTful API of Redmine. With the help of an administrator's API access-key on Redmine, it would be possible to communicate with Redmine using REST calls, for fetching and manipulating information of projects, people, issues and other entitities. Thus, the URL of Redmine server and API access-key of administartor are used by another block (**Dropwizard based RESTful API**) to communicate with Redmine. These details are refereed from the [configuration file](https://github.com/vvksdatta/capabilityTool/blob/master/config.yml).  

**PostgreSQL database**

A PostgreSQl database is integrated to the Web application to maintain a local copy of information related to various entities on Redmine. This information is necessary in order to further establish relationships between entities like people and different apsects like programming skills, capabilities, etc. For maintaining a consistency between data being stored on Redmine and database, a database schema similar to that of Redmine is implemented in the local instance of database. That is, the identifiers of various entities on Redmine, together with their respective values are retieved and stored in the database. This is accomplished by the next block.
 

**Dropwizard based RESTful API**

The central block in the above listed diagram is a key elememt that acts as a bridge between [Redmine](http://www.redmine.org/) and [PostgreSQL](https://www.postgresql.org/) database. This block communicates with the RESTful API of Redmine in order to fetch and store information of various entities to the database. The block is developed using [Dropwizard](https://www.dropwizard.io/1.2.0/docs/) framework, that implements all the functionalities needed for running a Web appliation. Dropwizard bundles multiple libraries together such as: 

* [Jetty](http://www.eclipse.org/jetty/) for HTTP services.
* [Jersey](http://jersey.java.net/) for RESTful web application.
* [Jackson](https://github.com/FasterXML/jackson) for parsing and generating JSON.
* [JDBI](http://www.jdbi.org) for database interactions.
 
With the help of Jersey and Jackson services, the dropwizard project herein hosts multiple resources like projects, sprints, issues, etc. and contains multiple REST end points to serve and manipulate information related to these resources, using HTTP methods (GET, PUT, POST and DELETE). 

Further, the main method of dropwizard project spins up an HTTP server using [Jetty](http://www.eclipse.org/jetty/). This server hosts all the Web pages and other static files associated with the client-side interface of the application(front-end).  

**Users**

The application's design is oriented towards the practices of managers. Multiple managers can create profiles and access this Web application.

Prerequisites for running the Web application
---
**1.** **JRE 9**
   * [Install JRE 9 on Windows](https://docs.oracle.com/javase/9/install/installation-jdk-and-jre-microsoft-windows-platforms.htm#GUID-2B9D2A17-176B-4BC8-AE2D-FD884161C958)
   * [Install JRE 9 on Linux](https://docs.oracle.com/javase/9/install/installation-jdk-and-jre-linux-platforms.htm#GUID-09D016D5-AB67-4552-9312-3B249180BD0F)
   * [Install JRE 9 on macOS](https://docs.oracle.com/javase/9/install/installation-jdk-and-jre-macos.htm#GUID-0071963E-D247-4D15-BF49-AD19C7260740)
   
**2.** **PostgreSQL** 
   * [Install PostgreSQL database version 9.5.14](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
   * When installation panel prompts to enter port number for the server to listen to, you can set it to the default port number 5432. Further, choose a password for superuser 'postgres'. These credentials will further be used to set up a server and communicate with the database.   
   * [Install PgAdmin 4](https://www.pgadmin.org/download/), an administration and management tool for PostgreSQL database
   * Now open PgAdmin 4 application to setup a server. Select the `Servers` group and right click > `Create` > `Server`.
   * In the Create-Server panel, under general tab, assign a name for the server. Then under Connection tab, the Host name/address shoud be set to localhost. Make sure that the port number is set to 5432 and then under password, enter the password for `postgres` user that was used while installing PostgreSQL. Next, save the configuration.   
   * Next, we create a new a database. Within the `Servers` group, select the server created in the previous step and right click > `Create` > `Database`. Enter a name for the new database and make sure that the Owner is set to `postgres` user. Then, save the details.
   * The server configuration details together with the details of newly created database will be further used in the configuration file ([config.yml](https://github.com/vvksdatta/capabilityTool/blob/master/config.yml)) for the Web application. 
   
**3.** **Redmine** 
   * [Install Redmine by setting up various components manually](https://www.redmine.org/projects/redmine/wiki/RedmineInstall) or alternately, if you choose to install Redmine stack with a all-in-one installer, you can use [BitNami's Redmine stack](https://bitnami.com/stack/redmine/installer). These [instructions](https://www.redmine.org/projects/redmine/wiki/How_to_install_Redmine_in_Linux_Windows_and_OS_X_using_BitNami_Redmine_Stack) with detailed screenshots could be helpful during the installation process. Currently, the developed Web applications works well with Redmine version 3.3.3.stable. By default, Redmine server runs on localhost over port 80.
   *  Sign-in into the administrator's account on Redmine and enable REST services by clicking `Administration` > `Settings` > `API` or `Administration` > `Settings` > `Authentication`. Then, select the two check-boxes: `Enable REST web service` & `Enable JSONP support` and save the settings.
   * In order to access Redmine data from the Web application, we need an API access key from an admininstrator's account on Redmine. This key can be generated by clicking `My account`. On the right side of the page, there are options for creating a key, displaying it  and resetting it. 
   * The URL of Redmine server and API access-key will further be used in the configuration file ([config.yml](https://github.com/vvksdatta/capabilityTool/blob/master/config.yml)) for the Web application. 
   
**4.** **Maven**
   * [Install build automation tool Maven](https://www.baeldung.com/install-maven-on-windows-linux-mac) for building the Web application. 

**5.** **Chrome or  Fire-Fox Web browser**

How to start the Web application
---
  * Update the following fields in configuration file ([config.yml](https://github.com/vvksdatta/capabilityTool/blob/master/config.yml))
    * Under database, update `password` and `url`.
    * Update `adminUserName` and `adminPassword` in order to change the defualt username and password details for Web application.
    * Update `redmineUrl` and `apiAccessKey`. 
  * Run `mvn clean install` to build the Web application. This generates a .jar file under target folder.
  * Start the application with `java -jar target/wiptool-0.0.1-SNAPSHOT.jar server config.yml`.
  * Access the Web application from a browser using the url `http://localhost:8080`.
