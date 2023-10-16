### Summary ###
The backend is implemented in Java as a Spring Boot application. We chose to build off of the Java codebase Justin has written for a personal project which consists of an API and services which connect to a MySQL database. This personal project and our current project share many functionalities, such as authenticating users, hosting games, making payments, and storing and retrieving data for analysis. In addition, the codebase has connections with the front-end setup, which allows us to focus on connecting the backend with a Python-based game service component. We consulted our partner Peter for his opinion on adapting the codebase to the project. We think the codebase is well-written, well-tested, and extensible for our project. Building upon it will save us time to prioritise the unique needs Peter has proposed. 

Specifically, we want to build a scalable platform facilitating real-time multiplayer games between AI and humans. Java suits our goal of scalability with its support for multi-threading and distributed computing. Another priority for us is to design a web API for psychology and cognitive science researchers to easily create and run games and gain insight from the data. Java has a rich ecosystem of frameworks and libraries for building web APIs. Moreover, Java has built-in security features that will help protect the sensitive data collected for research purposes. 

For deployment, we experimented with three hosting services: Glitch, AWS, and Render. We shortlisted these three because they offer free-tier services. However, Glitch only has native support for Python and Node.js. AWS requires credit card information. So we settled down with using Docker to deploy our Java backend on Render. 

Unit testing is implemented and automated upon building the project. This is a priority for our codebase as we expect there are many moving parts that come into play and hence we must be certain that everything works as expected.


### Contributions ###
Justin: I was responsible for planning and coordinating with frontend and game service. For the purpose of D2, I focused on researching and planning out how to deploy our backend and database. Anqi and I managed to Dockerize our backend and deploy it to Render along with some necessary code changes. I have also set up and deployed our database to Clever Cloud. In addition, I worked with Anqi to map out the infrastructure and worked with Nathan to get connected with the game service.

Nathaniel: I was responsible for connecting the backend to the Python game service. I coordinated with Spencer to ensure that the game service was integrated with the backend API smoothly. This integration was done by calling the Python game service from the backend and then passing arguments into the game service through the CLI. I also coordinated with Cecil to ensure that he was able to integrate the frontend with the game service through the backend.

Anqi: I was responsible for deployment and partner communication. I worked with Justin on the project infrastructure diagram and backend deployment with Docker on Render. I emailed our partner Peter to update the progress we’ve made and discuss the design choice of having researchers (one of our target user groups) create/update games through a web API or the frontend GUI or both. 

### Verification ###
Our backend is connected to the frontend site (https://main.dekaw19mhqaqy.amplifyapp.com/) that will be used to verify our work. As of right now, you will be able to register a user, log in, play games, and more. Interacting with the website will have the frontend application send requests to our backend API, and subsequently interact with the game service and database. All data is stored in our MySQL instance hosted on Clever Cloud, and is persistent. This means that all data (i.e. user information, encrypted passwords) are saved and loaded within our backend. 

All the code where the frontend interacts with the backend is in subteam 1’s repository. In contrast, all API definitions and logic along with database interactions and game service calls can be seen in subteam 2’s repository. Game service itself is subteam 3's responsibility, and we have worked with them to integrate their work through our API for the frontend to use. 

### Application ###
Our backend is deployed to https://project41301.onrender.com whereas our database is deployed to Clever Cloud. (You may visit https://project41301.onrender.com/hello for a simple health check). The first request sent to this backend may take a while as the free tier on Render has our deployment sleep when inactive. 
The web application hosts a frontend that communicates with our backend deployment, and hence our work can be tested through the web application provided by subteam 1’s deployment. For convenience, this site is hosted at https://main.dekaw19mhqaqy.amplifyapp.com/.
