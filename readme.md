# **`Lottery Application`**

As part of the assignment I have created a simple REST API with three endpoints whereby we can run a simple lottery service.

Prerequisites:

* Java 11 
* Docker
* Docker-compose 

## Steps to execute:
* Go to the directory containing the script 
  `$cd /lottery-application`
* Run the script 
  `./run.sh`

> Note: If the command fails to run then may be the ./run.sh file needs execute permission. This can be achieved from the following command:
chmod +x run.sh

## Things that went well:
* Got to use Spring Hibernate Spring JPA along with Testcontainers
* Got to dockerise a spring boot application and run along dockerised postgres DB.

## Improvement
* **Security**: At the moment no security implemented. The passwords are stored as plain text. We can use may be oAuth token for example while submitting a ballot to authorise registered users.
* **User Management:** We can use any user management service such as Okta to have a sophisticated user management.
* **@Scheduled**: This is being used to handle the scheduled event and will not work well in case of multiple nodes and that is not being handled.
* **Database**: Postgres is used as the database, but we can choose any other database such Amazon Els service which provides extensive support for read heavy applications. 
* **Populating Database** If database is populated while postgres container starts we would have some data at hand to actually test the api via rest api testing tools.

>A small postman collection with 3 sample requests is provided in `/postmanCollection` folder `
