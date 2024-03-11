# Project41 Backend

The API for the project41 application

## Prerequisites

Before running this project, ensure that you have the following installed on your machine:

- JDK (Java Development Kit) 17 or later: [Download JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- Maven 3.8.6 or later: [Download Maven](https://maven.apache.org/download.cgi)
- MySQL 8.0.33 or later: [Download MySQL](https://dev.mysql.com/downloads/installer/)

## Backend Setup

1. Clone this repository to your local machine.
2. Open a terminal and navigate to the project's backend directory: `cd project41-backend`.
3. Update the MySQL database connection settings in the `application.properties` file.
4. Build the backend project using Maven: `mvn clean install`.
5. Run the backend application in your IDE or with the command: `mvn spring-boot:run`.

The backend server should now be running on http://localhost:8080.

## Deployment

1. Log in to Docker Hub
2. Copy the desired version of `gameservice` into the root project directory
3. Build the project: `mvn clean install`
4. Build the Docker image: `docker build -t project41301/backend .`
5. Push the Docker image to Docker Hub: `docker push project41301/backend`
6. [Deploy the Docker image on Render](https://dashboard.render.com/web/srv-cklksc0710pc73d80vs0)
7. Verify the Docker image hash with the deployed version

If the database structure has been changed, we will need to take the following steps after deployment:
1. Set the `DATASOURCE_STRATEGY` environment variable to `create` through the Render dashboard
2. Deploy the latest Docker image (if not deployed automatically by Render)
3. Set the `DATASOURCE_STRATEGY` environment variable to `update` through the Render dashboard
4. Deploy the latest Docker image (if not deployed automatically by Render)

## Automated Tests
All unit tests will run automatically upon building the application with Maven. You may add to the suite of tests under the tests folder in the project directory. 

## License

This project is licensed under the [MIT License](LICENSE).
