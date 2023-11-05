# running this script requires :
# - running docker with API exposed,
# - installed minikube
#
# for usage help execute below line:
# ./run_kubernetes.sh


# c - delete minikube environment
if [[ "$1" == *"c"* ]]; then
  minikube stop
  minikube delete
fi


# b - build
if [[ "$1" == *"b"* ]]; then
  eval "$(minikube -p minikube docker-env)"
  docker build -t "todo-app-keycloak" -f "./env/docker/Dockerfile-keycloak" .
#  docker build -t "todo-app" .
fi


# s - start minikube
if [[ "$1" == *"s"* ]]; then
  minikube start
fi


# i - list images
if [[ "$1" == *"i"* ]]; then
 echo
 echo "############################"
 echo "##### DOCKER IMAGES: #######"
 echo "############################"
 echo
 docker images

 echo
 echo "############################"
 echo "#### MINIKUBE IMAGES: ######"
 echo "############################"
 echo
 minikube image ls --format table

 echo
 echo "############################"
 echo "#### MINIKUBE CONFIG: ######"
 echo "############################"
 echo
 minikube -p minikube docker-env
fi


# r - apply dev environment deployments (for local development)
if [[ "$1" == *"r"* ]]; then
  # start keycloak
  kubectl apply -f ./env/k8s/k8s-env-keycloak-h2-deployment.yml
  # start DB
  kubectl apply -f ./env/k8s/k8s-app-db-node-deployment.yml
fi


# f - apply full environment deployments (app included)
if [[ "$1" == *"f"* ]]; then
  # start keycloak
  kubectl apply -f ./env/k8s/k8s-env-keycloak-h2-deployment.yml
  # start DB
  kubectl apply -f ./env/k8s/alternatives/k8s-app-db-cluster-deployment.yml
  # start to-do app
  kubectl apply -f ./env/k8s/k8s-app-node-deployment.yml
fi


# d - dashboard
if [[ "$1" == *"d"* ]]; then
  minikube dashboard
fi


# e - end session (stop minikube)
if [[ "$1" == *"e"* ]]; then
  minikube stop
fi


# help
# option not specified
if [ ! "$1" ];then
  echo "Usage: ./run_kubernetes.sh [options...]"
  echo " c  - [CLEAN]     delete minikube environment"
  echo " b  - [BUILD]     build to-do app and keycloak images"
  echo " s  - [START]     start minikube"
  echo " r  - [RUN]       apply dev environment deployments (for local development)"
  echo " f  - [FULL]      apply full environment deployments (app included)"
  echo " e  - [END]       end session (stop minikube)"
  echo " d  - [DASHBOARD] start minikube dashboard"
  echo " i  - [IMAGES]    list docker and minikube images"
  echo
  echo "Example:"
  echo "./run_kubernetes.sh sr"
  echo
  echo "Running this script requires :"
  echo "- running docker with API exposed"
  echo "- installed minikube"
  echo
fi
