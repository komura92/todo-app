# TO-DO app

## Introduction
This project is simple to-do app created with Java and Spring, exposing simple API by HTTP. Running locally described below.


You can find here some useful integration elements like DB or Keycloak connection, interesting commits like Java 11 to 21 migration etc.

#
## Practical usage of project
Except that every programmer loves - a good copy-paste source it's also a good opportunity to try development in some existing project or to work with docker/kubernetes. Try to change something! :)

I used this project for comparing a cost difference between cases when we are using Java 11 or Java 21 version, including docker aspects. If you are interested in that research result please read article below.

[https://damiankumor.blogspot.com/2023/10/java-21-vs-java-11-test-description.html](https://damiankumor.blogspot.com/2023/10/java-21-vs-java-11-test-description.html)

#
## Running app

### local

Running this project in local environment requires:
- Postgres DB server - running on default 5432 port,
- running Keycloak - default 8080 port.


To run this project on your own PC, below articles can be useful. If you are familiar with Keycloak no need to go through this video tutorial and manually configure realm and users - just use JSON configuration (./src/main/resources/keycloak/*) to import required configurations or just create your own.

https://damiankumor.blogspot.com/2023/07/keycloak-dev-in-4-minutes-windows.html

https://www.postgresqltutorial.com/postgresql-getting-started/install-postgresql/


###
### Docker (docker-compose)

Running this project in docker environment requires installed and running docker.

In the project root directory you can find script called _[run_docker-compose.sh](run_docker-compose.sh)_ - for development environment creation automation. It can help with building application image (requires jar in target dir), starting and stopping environment based on configuration in _[docker-compose-env-kc-h2.yml](env%2Fdocker%2Fdocker-compose-env-kc-h2.yml)_ and also listing docker images from your repository. After environment setup you can run application on <ins>docker profile</ins>. To start environment run:

    ./run_docker-compose.sh s

This command will run:
- Keycloak service image with imported realm and users from _./src/main/resources/keycloak/*_,
- Postgres DB server image with created database and credentials configured.

Script help output:

    Usage: ./run_docker-compose.sh [options...]
    b  - [BUILD]     build to-do app docker image
    s  - [START]     start development environment with docker-compose up
    e  - [END]       stop development environment with docker-compose down
    i  - [IMAGES]    list docker images
    
    Example:
    ./run_docker-compose.sh bs
    
    Running this script requires :
    - installed and running docker


#
### Kubernetes (minikube)

Project root directory also contains script which automates running environment locally with Kubernetes - _[run_kubernetes.sh](run_kubernetes.sh)_. It can be helpful with:
- cleaning actual environment,
- building Keycloak image with required imports included,
- starting minikube environment,
- applying deployment configurations,
- stopping minikube session,
- starting minikube dashboard,
- listing available images for docker and minikube.


When running project first time you can run script with below command. It will build necessary Keycloak image, start minikube environment and apply configuration for development environment. In this case you should also run app with <ins>docker profile</ins>.

    ./run_kubernetes.sh bsr

###

Script help output:

    Usage: ./run_kubernetes.sh [options...]
    c  - [CLEAN]     delete minikube environment
    b  - [BUILD]     build to-do app and keycloak images
    s  - [START]     start minikube
    r  - [RUN]       apply dev environment deployments (for local development)
    f  - [FULL]      apply full environment deployments (app included)
    e  - [END]       end session (stop minikube)
    d  - [DASHBOARD] start minikube dashboard
    i  - [IMAGES]    list docker and minikube images
    
    Example:
    ./run_kubernetes.sh sr
    
    Running this script requires :
    - running docker with API exposed,
    - installed minikube.


#
## Avoiding authorization problems

To avoid some authorization problems like "Invalid JWT token issuer" you can add below line to your /etc/hosts file.

    <your-docker-network-ip>    todo-app-keycloak-h2-svc
