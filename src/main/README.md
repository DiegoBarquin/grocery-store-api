# Java 21 Project with Maven

This is an example project developed using **Java 21** and **Maven** as the dependency management and build tool. Below are instructions for setting up, running this project.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Start project](#start-project)
- [Swagger UI](#swagger-ui)

## Prerequisites

Before you begin, ensure you have the following installed:

- [JDK 21](https://www.oracle.com/cis/java/technologies/downloads/#java21) or a later version of Java.
- [Maven](https://maven.apache.org/install.html) (recommended for managing dependencies and building the project).
- [Git](https://git-scm.com/) (required for cloning the repository).

## Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-username/your-repository.git
   ```
   
## Start project

   ```bash
   mvn spring-boot:run 
   cd ../..
   ```
This will start the application, and it should be available at http://localhost:8080 by default.

## Swagger UI
This project includes Swagger UI for easy testing and documentation of the API.
Once the application is running, open your browser and navigate to:
http://localhost:8080/swagger-ui/