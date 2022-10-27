# The Task (Due: 2020-06-20 00:00:00)

We have a platform for visualizing and managing public transportation. The public transportation for this task is a simplified version of the real world. There are only busses, suburban trains and subways as means of transport. There are stops and connections between these stops. The platform you have to create is somehow the API for changing the state (or getting information regarding the state) of the public transportation and you only have to provide the REST-API calls to interact with your API and you don't need to add an additional user interface for communicating in a more user friendly way (and you won't get any extra points for that either ;)).  

In order to keep the project simple, we will not use client authentication and authorization. In real life projects you would – of course – implement those features.
The story are listed in the preferred order of implementation.

### User Stories (110 points max out of 100 points)
Each story gives you 10 points max (depending on other quality criteria)

#### Story 1 (10 pts)
As an administrator I need to import a prefabricated schedule in order to save time when...
*  ... recovering a complete schedule from a backup
*  ... creating a new schedule from scratch

*acceptance criteria*
*  the database get's cleared before importing the data
*  the model data of the imported file in **json format** is stored in the database
*  the written import has at least square maturity regarding regarding the import time

As format for the import we will use the following json-format:
...
example:
```
{
  "stops": [
    {
      "stopId": 1,
      "types": [
        "BUS",
        "SUBURBAN_TRAIN",
        "SUBWAY"
      ],
      "state": "CLOSED",
      "name": "Hauptbahnhof D2",
      "city": "Kiel"
    },
    {
      "stopId": 2,
      "types": [
        "BUS",
        "SUBURBAN_TRAIN"
      ],
      "state": "OPENED",
      "name": "Hummelwiese",
      "city": "Kiel"
    },
    {
      "stopId": 3,
      "types": [
        "BUS",
        "SUBURBAN_TRAIN"
      ],
      "state": "OPENED",
      "name": "KVG-Btf. Werftstraße C",
      "city": "Kiel"
    },
    {
      "stopId": 4,
      "types": [
        "BUS",
        "SUBURBAN_TRAIN"
      ],
      "state": "OPENED",
      "name": "Karlstal",
      "city": "Kiel"
    },
    {
      "stopId": 5,
      "types": [
        "BUS",
        "SUBURBAN_TRAIN"
      ],
      "state": "OPENED",
      "name": "H D W",
      "city": "Kiel"
    },
    {
      "stopId": 6,
      "types": [
        "BUS",
        "SUBWAY"
      ],
      "state": "OPENED",
      "name": "Fachhochschule",
      "city": "Kiel"
    }
  ],
  "trafficLines": [
    {
      "lineId": 1,
      "name": "Line 11",
      "type": "BUS",
      "sections": [
        {
          "beginStopId": 1,
          "endStopId": 2,
          "durationInMinutes": 5
        },
        {
          "beginStopId": 2,
          "endStopId": 3,
          "durationInMinutes": 3
        },
        {
          "beginStopId": 3,
          "endStopId": 4,
          "durationInMinutes": 2
        },
        {
          "beginStopId": 4,
          "endStopId": 5,
          "durationInMinutes": 5
        },
        {
          "beginStopId": 5,
          "endStopId": 6,
          "durationInMinutes": 5
        }
      ]
    },
    {
      "lineId": 2,
      "name": "Underground 1",
      "type": "SUBWAY",
      "sections": [
        {
          "beginStopId": 1,
          "endStopId": 3,
          "durationInMinutes": 5
        },
        {
          "beginStopId": 3,
          "endStopId": 6,
          "durationInMinutes": 5
        }
      ]
    }
  ]
}
```

#### Story 2 (10 pts)
As an administrator I need to backup the schedule in order to recover in cases of data loss. 

*acceptance criteria*
*  manual backups are possible
*  backups are made on a predefined schedule
*  output format of the backups is json
*  the scheduled backups are stored on a predefined path on the hard drive

#### Story 3 (10 pts)
As a city planner I want to add a new stop to extend the public transportation.

*acceptance criteria*
*  the created stop may be for busses or subways or suburban railways - at least one of these - depending on the needs of the city planner 
*  the stop is initially marked as closed and therefore no public transportation is allowed to stop here
*  it is possible to open the stop for the public transportation on demand
*  the stop has no incoming or outgoing connections


#### Story 4 (10 pts)
As a city planner I want to add a new traffic line to extend the public transportation.

*acceptance criteria*
*  the traffic line is created for busses, subways or suburban railways - exactly one of these - depending on the needs of the city planner
*  the traffic line may consist of multiple stops and all these stops must exist. If a stop is not existing the traffic line is not created.
*  the traffic line is composed of multiple sections. Each of these sections consists of two different stops and the time in minutes it takes to get from one stop to the other stop of this section. The section is a unidirectional connection between two stops.

#### Story 5 (10 pts)
As Head of Service of the public transportation I need to react to disturbances, to minimise the complaints and keep the passengers up to date.

*acceptance criteria*
*  a disturbance may be created on a stop or a connection between two stops
*  if a disturbance occured on stops or connections these stops and connections are out of order until the disturbance is resolved
*  the disturbance may be resolved on demand

#### Story 6 (10 pts)
As Head of Service of the public transportation I need to know where the transportation vehicles are at the moment, to check if everything is in order.

*acceptance criteria*
*  a vehicle sends a signal to the system at every stop
*  it is not neccessary to implement a simulation of the public transportation system but rather use a request to model the vehicles status updates
*  it is possible to export the list of the vehicles current locations in text format

#### Story 7 (10 pts)
As a passenger I want to get the fastest public transportation connection from stop A to another stop B, in order to get as fast as possible from A to B.

*acceptance criteria*
*  if the input is malformed or invalid provide a useful hint in the response
*  the result shall contain the connection with all its stops and the total time it takes to get from A to B

#### Story 8 (10 pts)
As a passenger I want that the connecting time is taken into account, when the fastest public transportation connection is calculated, to get more precise results.

*acceptance criteria*
*  it is possible to add and change a connecting time in minutes at a stop to switch from one line to another line
*  if a stop has no connecting time defined yet, 0 minutes is used as default value
*  for the result of the calculation of the fastest public transportation connection is extended to include the information regarding the connecting time at each stop.


#### Story 9 (10 pts)
As a passenger I want to buy a ticket for a trip with the public transportation from a stop A to another stop B, in order to use the public transportation legally.

*acceptance criteria*
*  between stop A and stop B is no other stop (simplification)
*  the passenger may buy multiple tickets at once
*  the passenger buying the tickets will use them instantly


#### Story 10 (10 pts)
As Head of Service of the public transportation I want to get statistics of the public transportation, in order to make well-founded decisions.

*acceptance criteria*
*  the statistics are created in text format
*  the statistics are containing the ..
   1.  total number of tickets bought 
   2.  total number of tickets bought per connection
   3.  total number of disturbances
   4.  total number of disturbances per connection
   5.  total number of disturbances per stop
   6.  total number of stops
   7.  total number of bus stops
   8.  total number of subway stops
   9.  total number of suburban train stops
   10. total number of connections

#### Story 11 (5 pts)
As a passenger I want to visit N stops of the public transportation in M minutes in order to do a city sightseeing tour.

*acceptance criteria*
*  it is allowed to visit the same stop multiple times, but each stop must be counted only once

#### Story 12 (5 pts)
As a passenger I want to get the three cheapest public transportation connections from stop A to another stop B, to save money.

*acceptance criteria*
*  the result shall contain the routes and their corresponding prices
*  the result shall contain the routes ordered by price ascending

# Rules / Restrictions
### Groups
We create user stories for all groups. Every group get's the same amount and the same set of stories. A group consists of 1 or 2 students.

### Grading
Your grade is composed as follows:
- We will grade the code and the program you submitted. (at least 80% of the final grade)
- You will have to give a final presentation (about 20-30 minutes, at most 20 % of the final grade), where you... 
... have to present your developed application.
... have to answer questions regarding your project and the submitted code.
**Code have to be submitted to your Gitlab project - neither on Github, through e-Mail, on a USB-device nor printed out.**

## Technology
As you implement the stories for fulfilling the requirements you have to use the following frameworks, tools & technologies - these are must haves:
* neo4j (as persistence layer)
* Spring Boot 
* Java 13 (you may not use any Java 13 features at all, but your code must compile using Java 13) 
* IntelliJ
* gradle as the only build tool (not the IntelliJ internal build)
* REST
* Git (using this GitLab-Repository)

### Quality
* **Submitted code must have a unit test line coverage of about 20 % which has a mutation test coverage of 80%.**
* Submitted code must be well formatted - therefore use the provided formatter settings for the IntelliJ IDEA IDE (Profile Default). Use the Save Actions Plugin for IntelliJ.
* Submitted code must compile, executing the tests must succeed and a runnable spring boot application must be the artifact of your submitted project. (for clarification: running gradle assemble & bootJar and gradle bootRun must succeed)
* You should use the SonarLint plugin for IntelliJ for checking your code for error, code smells and security issues. Your code shall have no issues or only irrelevant issues (be prepared for explaining the irrelevance in the presentation)
* Document your code using javadoc where needed (classes, complex methods, sometimes maybe inline comments)

### Naming
* Naming constants: Uppercase letters, numbers and underscore only 
* Naming Variables, member variables & method names: Starting with a lowercase letter then camelcase notation
* Naming Classes: Starting with a uppercase letter then camelcase notation
* Naming packages: only lowercase letters, avoid default package where possible

### Structure / Architecture
Submitted code must be structured, e.g. separate classes into different packages, build at least a service-, persistence- and a presentation (REST) layer.
Keep the principles DRY, KISS, POLS and YAGNI in mind when you develop your application.

**You must document the chosen architecture (package structure, layering, ...) and your submitted code must conform to the rule defined by this documentation**

# REST-API
**You must provide a documentation for the REST-API of your submitted application, where all endpoints are listed, documented and explained. You may use Swagger-Annotations to fulfill this task.**

# Configurations & Links
### Links
[Neo4j database download](https://neo4j.com/download-center/#releases)

[Cypher Manual](https://neo4j.com/docs/cypher-manual/current/)

[Spring Data and neo4j](https://spring.io/guides/gs/accessing-data-neo4j/)

[Git cheat sheet](https://ndpsoftware.com/git-cheatsheet.html)

[Consuming a REST service using Spring's RestTemplate (1)](https://howtodoinjava.com/spring-restful/spring-restful-client-resttemplate-example/)

[Consuming a REST service using Spring's RestTemplate (2)](https://www.baeldung.com/rest-template)




