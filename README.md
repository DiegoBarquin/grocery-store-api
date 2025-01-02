# Grocery Store

This is an example project developed using **Java 21** and **Maven** as the dependency management and build tool. Below are instructions for setting up, running this project.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Clone the repository](#clone-the-repository)
- [Maven Commands](#maven-commands)
- [Swagger UI](#swagger-ui)

## Prerequisites

Before you begin, ensure you have the following installed:

- [JDK 21](https://www.oracle.com/cis/java/technologies/downloads/#java21) or a later version of Java.
- [Maven](https://maven.apache.org/install.html) (recommended for managing dependencies and building the project).
- [Git](https://git-scm.com/) (required for cloning the repository).

## Clone the repository

1. Open your terminal or command line interface.
2. Navigate to the location where you want to store the project on your machine.
3. Run the following command to clone the repository:
   ```bash
   git clone https://github.com/DiegoBarquin/grocery-store-api.git
   ```
4. Navigate into the project directory:
   ```bash
   cd grocery-store-api
   ```

## Maven Commands

- **Move to the root folder of the project**:

   Before running any other commands in a terminal (such as starting the application or running tests),
you must first navigate to the root folder of the project.
In the case where you want to go up two levels from your current directory, you would use the following command:

   ```bash
   cd ../.. 
   ```

- **Confirming Your Location:**:
   ```bash
   pwd
   ```
   This command will display the path of your current working directory. 
If the output shows the root directory of your project, you're ready to proceed with further commands.


- **Run unit tests with Maven**:

   This will launch the unit test in local.
   ```bash
   mvn test 
   ```
- **Start project**:

   This will start the application, and it should be available at http://localhost:8080 by default.
   ```bash
   mvn spring-boot:run 
   ```

## Swagger UI
This project includes Swagger UI for easy testing and documentation of the API.
Once the application is running, open your browser and navigate to:
http://localhost:8080/swagger-ui/index.html