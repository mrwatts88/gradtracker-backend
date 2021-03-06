# UWM Capstone Project - RESTful Service

&nbsp;
----

The provided codebase is a [spring-boot](https://projects.spring.io/spring-boot/) based project that requires [git](https://git-scm.com/downloads), 
[java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) _(or greater)_ and 
[apache maven](https://maven.apache.org/download.cgi) be installed on your machine.

##### Concatenation
_The functionality to take two separate values and combines them together._
* Java 
    * [Implementation](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/main/java/edu/uwm/capstone/util/Concatenation.java)
        * [Unit Test](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/test/java/edu/uwm/capstone/util/ConcatenationUnitTest.java)
    * [Rest Controller](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/main/java/edu/uwm/capstone/controller/ConcatenationRestController.java) 
        * [Unit Test](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/test/java/edu/uwm/capstone/controller/ConcatenationRestControllerUnitTest.java)
        * [Component Test](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/test-component/java/edu/uwm/capstone/controller/ConcatenationRestControllerComponentTest.java)
        
##### Palindrome
_Determine whether or not the provided value is a word, phrase, or sequence that reads the same backward as forward._
* Java 
    * [Implementation](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/main/java/edu/uwm/capstone/util/Palindrome.java)
        * [Unit Test](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/test/java/edu/uwm/capstone/util/PalindromeUnitTest.java)
    * [Rest Controller](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/main/java/edu/uwm/capstone/controller/PalindromeRestController.java)
        * [Unit Test](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/test/java/edu/uwm/capstone/controller/PalindromeRestControllerUnitTest.java)
        * [Component Test](https://gitlab.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example/blob/master/src/test-component/java/edu/uwm/capstone/controller/PalindromeRestControllerComponentTest.java)
        
&nbsp;
---

### Building this Project

To perform a build and execute all unit tests:
```
mvn clean install
```

To execute all component tests:
```
mvn clean -P test-component test
```

&nbsp;
---

### Using this Project

To run the RESTful services:
```
mvn spring-boot:run
```

or you can execute the JAR that is created by the install command above via:
```
java -jar target/*.jar
```

By default, application.properties configures a local instance of H2 so that anyone can use this project immediately.
See `service/src/main/resources` for available profiles.

If you want to persist your data between service restarts, you will need to use MySQL.  
You can use MySQL in one of two ways:
- Use docker (preferred!)
  - Install [Docker for Windows](https://hub.docker.com/editions/community/docker-ce-desktop-windows) or [Docker for Mac](https://hub.docker.com/editions/community/docker-ce-desktop-mac)
  - Using a terminal inside this project, run `docker-compose up -d`
  - You're ready to go!
- Manually install MySQL on your local machine (there are some pitfalls, beware!)
  - Download [MySQL](https://dev.mysql.com/downloads/mysql/5.7.html)
  - Start MySQL on port 3306
  - Create a schema called `capstone`

Whether you use docker to run MySQL, or manually install and start it, the command is still the same:
```
mvn spring-boot:run -D spring.profiles.active=localmysql
```

Once the application is running locally Swagger based REST documentation and testing will be available at:
- [http://localhost:8333/backend-example/swaggerui](http://localhost:8333/backend-example/swaggerui)

and the Concatenation and Palindrome REST endpoints will also be accessible.

Concatenation example:
- [http://localhost:8333/backend-example/concatenate/value1/VALUE2](http://localhost:8333/backend-example/concatenate/value1/VALUE2)

Palindrome example:
- [http://localhost:8333/backend-example/palindrome/radar](http://localhost:8333/backend-example/palindrome/radar)
- [http://localhost:8333/backend-example/palindrome/notapalindrome](http://localhost:8333/backend-example/palindrome/notapalindrome)


### Use Docker for project 

`Dockerfile` available at root of project could be used to create a docker image which could be shipped, shared. In order to build this container, run following commands

```
docker build -t backend-example .
```
 and then this docker image could be used to run application on your machine                                                                                                                            

```
docker run -p port-to-be-run:port-of-application backend-example
```

for example: ```docker run -p 8333:8333 backend-example```

here application runs on 8333. You can also pull docker image from registry and run it locally, in order to pull centralized image from registry use following:

```
docker pull docker.nmcapstone.com/nm-capstone-cookbooks/nm-capstone-backend-cookbooks/backend-example

```
then use above listed run command to run this docker image on your machine. 

