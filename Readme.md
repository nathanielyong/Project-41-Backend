# Redistributor Backend

The private API for the redistributor application

## Prerequisites

Before running this project, ensure that you have the following installed on your machine:

- JDK (Java Development Kit) 17 or later: [Download JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- Maven 3.8.6 or later: [Download Maven](https://maven.apache.org/download.cgi)
- Node.js 18.16.0 or later: [Download Node.js](https://nodejs.org/en/download/)
- MySQL 8.0.33 or later: [Download MySQL](https://dev.mysql.com/downloads/installer/)

## Backend Setup

1. Clone this repository to your local machine.
2. Open a terminal and navigate to the project's backend directory: `cd redistributor-backend`.
3. Update the MySQL database connection settings in the `application.properties` file.
4. Build the backend project using Maven: `mvn clean install`.
5. Run the backend application in your IDE or with the command: `mvn spring-boot:run`.

The backend server should now be running on http://localhost:8080.

## Usage

Describe how to use the project, provide examples, and explain any necessary configurations or setup steps.

## License

This project is licensed under the [MIT License](LICENSE).
