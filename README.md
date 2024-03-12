# Reddit Clone

# Table of Contents

1. [Project Description](#project-description)
2. [Running the Project Locally](#running-the-project-locally)
   - [Prerequisites](#prerequisites)
     - [Java](#java) 
     - [IntelliJ IDEA Ultimate](#intellij-IDEA-ultimate)
     - [Docker](#docker)
     - [Postman](#postman-or-any-similar-service-for-sending-requests)
   - [Running Application](#running-application)
   - [Viewing the Application Online](#viewing-the-application-online)
   - [API Endpoints](#api-endpoints)
3. [Technology Stack](#technology-stack)
   - [Spring Boot](#spring-boot)
   - [Spring Data JPA](#spring-data-JPA)
   - [Spring Security](#spring-security)
   - [Spring Web](#spring-web)
   - [Spring Test](#spring-test)
   - [Spring Security Test](#spring-security-test)
   - [Spring Docker Compose](#spring-docker-compose)
   - [Spring Validation](#spring-validation)
   - [Spring Webmvc UI](#spring-webmvc-UI)
   - [PostgreSQL](#postgresql)
   - [Lombok](#lombok)
   - [MapStruct](#mapstruct)
   - [Jackson](#jackson)
   - [Logback](#logback)
   - [Workflows](#workflows)
   - [Angular](#angular)
4. [API Documentation Using Swagger](#api-documentation-using-swagger)
   - [Understanding Swagger](#understanding-swagger)
   - [How to Use Swagger Documentation](#how-to-use-swagger-documentation)
   - [Accessing Our API Documentation](#accessing-our-api-documentation)
5. [License](#license)

## Project Description

This project emerges as an educational venture, initiated by a dedicated group of students with a passion for software development and collaborative learning. Our aim is to construct a partial clone of the widely recognized social platform, **Reddit**. This endeavor is not about creating a mirror image but rather about capturing the essence of Reddit's functionality and user experience. Through this project, we aspire to delve deep into the intricacies of the **Spring Framework**, leveraging its robust features to build a solid foundation for our application.

At its core, the project is structured to foster a practical understanding of key development concepts, focusing on the **Spring Boot** framework. By replicating selective features such as subreddit creation, post submissions, comment interactions, and a dynamic voting mechanism, we not only pay homage to the original platform's community-driven spirit but also challenge ourselves to think critically and solve problems efficiently.

A crucial element of our project is the emphasis on teamwork and role flexibility. In a setting that mimics real-world software development projects, we are learning to navigate the complexities of collaborative coding, version control, and project management. This experience is invaluable, offering each team member a chance to explore different roles, from backend development to user interface design, and even project coordination.

Additionally, our project incorporates essential functionalities such as user registration, authentication, and authorization, ensuring a secure and personalized user experience. By doing so, we aim to understand and implement best practices in web security and user data management, critical skills in today's digital landscape.

As we progress, we are committed to refining our application, inspired by Reddit's model of content sharing and community engagement. Our journey through this project is a testament to our dedication to learning, our enthusiasm for technology, and our ambition to contribute meaningfully to the world of software development. Through this collaborative academic project, we not only aim to hone our technical skills but also to cultivate a mindset of continuous learning and innovation.

## Running the Project Locally

To explore and interact with our Reddit-like application, you can easily set it up and run it locally on your machine. Here's a step-by-step guide to getting started:

### Prerequisites

Before you begin, ensure you have the following installed on your computer:

- #### **Java:**
   > The application is built with Java, so you need to have Java installed to run the server-side components.

- #### **IntelliJ IDEA Ultimate:**
   > This IDE is recommended for its comprehensive support for Spring applications and Docker integration, offering a seamless development experience.

- #### **Docker:**
   > Our project uses Docker to containerize the application and its dependencies, including PostgreSQL, ensuring a consistent environment across all development stages.

- #### **Postman** (or any similar service for sending requests):
   > To test the API endpoints, you'll need a tool like Postman that allows you to send HTTP requests and view responses.

### Running Application

There are two primary ways to run the application locally:

1. #### **Using the .run Configuration:**
    - Navigate to the .run directory in the project and locate the localhost.run.xml file. This file is pre-configured to set up your application environment seamlessly.
    - Ensure Docker is running on your machine, as it's required to deploy the necessary PostgreSQL environment.
    - Open the project in IntelliJ IDEA Ultimate, find the localhost.run.xml configuration, and execute it. This will start both the application and the required Docker containers.
2. **Manual Configuration with localhost.env:**
    - If you prefer not to use the .run configuration, you can manually set up your environment using the localhost.env file found in the project directory. This file contains all the necessary environment variables.
    - To utilize this method, you'll need a plugin in IntelliJ IDEA Ultimate that allows importing .env files into your run configurations. Once set up, the environment variables will be automatically applied, ensuring the application starts correctly.

### Viewing the Application Online

For those who wish to view the application without setting it up locally, an online version is hosted on Render:

- The backend is accessible at [Backend application](https://abstract-reddit-preview.onrender.com). Note that the frontend will be available at a later date.
- To interact with the application's endpoints, Postman or a similar tool is recommended. This allows you to send requests to the API and receive responses, enabling you to test the application's functionality.

### API Endpoints

To fully explore the capabilities of our application, refer to the dedicated section on API endpoints. This will guide you through the various functionalities available, from user registration to post interactions, ensuring a comprehensive understanding of the application's features.

This setup guide aims to provide a straightforward path to running and interacting with our project locally, offering insights into its functionality and the technologies behind it. Whether you're looking to contribute or simply explore, we've ensured that starting up and testing the application is as accessible as possible.


## Technology Stack

This project leverages a comprehensive stack of technologies, each chosen for its specific role in enhancing the functionality and efficiency of our Reddit-like application. Below, we detail these technologies and their significance to our project:

1. ### **Spring Boot:**
   > At the core of our application, Spring Boot facilitates easy setup and configuration, hiding the complexity of system configurations. It serves as the foundation for creating a robust and scalable backend, allowing our team to focus on developing features rather than dealing with setup nuances. Within the context of this project, Spring Boot enables us to efficiently emulate Reddit's dynamic environment, focusing on community interaction and content management.

2. ### **Spring Data JPA:**
   > This technology bridges our application with the database, simplifying data persistence and retrieval through abstracted SQL queries. It acts as the repository layer, enabling efficient data management and interaction within our subreddit features. This integration is crucial for maintaining a seamless flow of data, ensuring that user contributions and interactions are persistently stored and easily accessible.

3. ### **Spring Security:**
   > Essential for protecting our application, Spring Security offers comprehensive authentication and authorization capabilities. It allows us to define custom security configurations, including role-based access control, securing our application against unauthorized access and ensuring a safe environment for users to interact. This layer is vital for mimicking Reddit's community-driven platform, where user identity and permissions play a crucial role.

4. ### **Spring Web:**
   > By utilizing Spring Web, we establish the foundation for our RESTful backend, aligning with the Spring MVC model. This enables us to handle web requests efficiently, serving as the communication bridge between our frontend and backend. This component is essential for creating an interactive user experience similar to Reddit, where seamless content delivery and submission are key.

5. ### **Spring Test:**
   > For ensuring the reliability of our application, Spring Test provides a framework for writing and running tests. This is crucial for maintaining a high-quality codebase, allowing us to emulate Reddit's stable and user-friendly platform.

6. ### **Spring Security Test:**
   > Specialized for testing security configurations, ensuring that our application's authentication and authorization mechanisms are robust and secure, reflecting our commitment to user privacy and data protection.

7. ### **Spring Docker Compose:**
   > Facilitates the creation of a Docker Compose file, streamlining the deployment of our application and its services in Docker containers. This simplifies the setup and scalability of our project environment, mirroring the deployment processes of sophisticated applications.

8. ### **Spring Validation:**
   > Offers a streamlined approach to data validation, ensuring the integrity of user input and system data. This is crucial for maintaining the quality and reliability of the interactions within our platform.

9. ### **Spring Webmvc UI:**
   > Provides tools for documenting and testing our RESTful APIs, enhancing the developer experience by offering a clear and interactive API specification. This is essential for both backend and frontend developers to understand and integrate the various endpoints and data formats used within our application.

10. ### **PostgreSQL:**
      > Chosen for its reliability and performance, PostgreSQL serves as our application's database. It supports our data-intensive features, such as subreddit creation, post submissions, and user interactions, providing a scalable and efficient data storage solution.

11. ### **Lombok:**
      > This library dramatically reduces boilerplate code by auto-generating common methods such as getters, setters, and constructors. In our project, Lombok ensures cleaner code, enhancing readability and maintainability, allowing us to focus on the logic that matters most.

12. ### **MapStruct:**
      > Simplifies object mapping, reducing the need for manual coding. This library is instrumental in translating data between our domain models and DTOs, facilitating data management and API response structuring.

13. ### **Jackson:**
      > For seamless JSON parsing and generation, facilitating the communication between our frontend and backend. This library is crucial for handling data transfer in our RESTful architecture, ensuring efficient and reliable data processing.

14. ### **Logback:**
      > Provides robust logging capabilities, crucial for monitoring the application's health and diagnosing issues. It supports our development and maintenance efforts by offering detailed insights into the application's behavior.

15. ### **Workflows:**
      > Utilizing CI/CD pipelines for automated testing and deployment, this setup ensures that our application maintains high standards of quality and reliability. It reflects our commitment to professional development practices, enabling continuous integration and delivery for an evolving project.

16. ### **Angular:**
      > Powers our frontend, creating a dynamic and responsive user interface. It communicates with our backend through API calls, presenting data to users in an intuitive and engaging manner. Angular's role in our project is to replicate the interactive and user-friendly experience found on Reddit, showcasing the power of modern web development frameworks in creating compelling online communities.

Our application architecture follows the MVC (Model-View-Controller) pattern, with controllers handling request mapping, DTOs facilitating data transfer, and custom security configurations ensuring secure access. Models represent the application's data structure, while mappers, powered by MapStruct, efficiently translate between models and DTOs. Custom exceptions and a global exception handler ensure user-friendly error reporting. Validators enforce data integrity, and services encapsulate business logic. Repositories interface with the database, completing the backend structure. This architecture, combined with our Angular frontend, creates a cohesive, scalable, and user-centric platform.


## API Documentation Using Swagger

Swagger is an open-source tool suite for designing, building, documenting, and consuming RESTful web services. It provides a user-friendly interface for understanding and interacting with the API by visualizing the API's structure. Swagger's documentation capabilities are instrumental for both developers working on the API and users intending to consume it.

### Understanding Swagger

Swagger offers a comprehensive ecosystem that includes software tools to assist throughout the entire lifecycle of an API. Key components include:

- **Swagger Editor:** A browser-based editor where you can write OpenAPI specifications.
- **Swagger UI:** A dynamic interface generated from your OpenAPI specifications, allowing users to visualize and interact with the API's resources without having the implementation logic in place.
- **Swagger Codegen:** Enables developers to generate server stubs, client libraries, and API documentation from an OpenAPI Specification.

### How to Use Swagger Documentation

Swagger's primary strength lies in its interactive documentation. Once an API is documented with Swagger, you can:

1. **Explore the API:** The Swagger UI provides a visual representation of the API, listing all available endpoints, their HTTP methods, and the structure of request and response bodies.
2. **Try Out Requests:** Without writing any code, you can execute API requests directly from the browser. This feature is incredibly useful for testing and understanding how the API behaves with different inputs.
3. **View Model Definitions:** Swagger documents the data models used in the API, offering insights into the data structure expected by the API and returned by it.

To use the Swagger documentation for our project, navigate to the provided URL. This will open the Swagger UI, where you can see a list of all endpoints, grouped by functionality. Clicking on an endpoint will expand it to show detailed information, including the expected request format and the response it returns. If you want to test an endpoint, simply click the "Try it out" button, enter any required parameters, and execute the request. The response will be displayed directly in the Swagger UI.

### Accessing Our API Documentation

Our application's API documentation is readily accessible via Swagger at the following link:

Accessing Our API Documentation
Our application's API documentation is readily accessible via Swagger at the following link:
> **[Documentation](https://abstract-reddit-preview.onrender.com/swagger-ui/index.html)**

This documentation is an essential resource for understanding the available interactions within our application, providing a clear and interactive way to explore its capabilities. Whether you're developing new features, integrating with the application, or simply curious about its functionality, the Swagger documentation offers the insights and tools needed for a comprehensive understanding.


## License

This project is open-sourced under the MIT License, offering wide-ranging freedom to use, modify, and distribute the software, even for commercial purposes, under the conditions stated in the license. We believe in the open exchange of information and the freedom to build upon the work of others, and the MIT License reflects these principles.

For more details, see the [LICENSE](LICENSE) file in the repository.
