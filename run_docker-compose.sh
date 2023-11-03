# running this script requires :
# - installed and running docker
#
# for usage help execute below line:
# ./run_docker-compose.sh


# i - docker images
if [[ "$1" == *"i"* ]]; then
 echo
 echo "############################"
 echo "##### DOCKER IMAGES: #######"
 echo "############################"
 echo
 docker images
fi


# b - build app image
if [[ "$1" == *"b"* ]]; then
  docker build -t "todo-app" .
fi


# s - start environment (docker-compose up)
if [[ "$1" == *"s"* ]]; then
  docker-compose -f "./env/docker/docker-compose-env-kc-h2.yml" up
fi


# e - stop environment (docker-compose down)
if [[ "$1" == *"e"* ]]; then
  docker-compose -f "./env/docker/docker-compose-env-kc-h2.yml" down
fi


# help
# option not specified
if [ ! "$1" ];then
  echo "Usage: ./run_docker-compose.sh [options...]"
  echo " b  - [BUILD]     build to-do app docker image"
  echo " s  - [START]     start development environment with docker-compose up"
  echo " e  - [END]       stop development environment with docker-compose down"
  echo " i  - [IMAGES]    list docker images"
  echo
  echo "Example:"
  echo "./run_docker-compose.sh bs"
  echo
  echo "Running this script requires :"
  echo "- installed and running docker"
  echo
fi

