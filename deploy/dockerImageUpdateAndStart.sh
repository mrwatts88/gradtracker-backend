#!/bin/bash

# any future command that fails will exit the script
set -e

CI_PROJECT_NAME=$1
DOCKER_REPO_USER=$2
DOCKER_REPO_PASS=$3

# Delete previously existing docker image and container
echo "remove previously existing container and image for $CI_PROJECT_NAME"

if [ "$(docker ps -aq -f status=running -f name=$CI_PROJECT_NAME)" ]; then
echo "docker kill $CI_PROJECT_NAME"
docker kill $CI_PROJECT_NAME
echo "docker rm $CI_PROJECT_NAME"
docker rm  $CI_PROJECT_NAME
fi

if [[ ! "$(docker images -q registry.uwm-nm-te-capstone.com:8083/$CI_PROJECT_NAME:latest 2> /dev/null)" == "" ]]; then
 echo "docker rmi registry.uwm-nm-te-capstone.com:8083/$CI_PROJECT_NAME:latest"
 docker rmi registry.uwm-nm-te-capstone.com:8083/$CI_PROJECT_NAME:latest
fi

# pull the latest docker image
echo "docker pull registry.uwm-nm-te-capstone.com:8083/$CI_PROJECT_NAME:latest"
docker login registry.uwm-nm-te-capstone.com:8083 --username $DOCKER_REPO_USER --password $DOCKER_REPO_PASS
docker pull registry.uwm-nm-te-capstone.com:8083/$CI_PROJECT_NAME:latest

#run docker image
echo "docker run -d -p 8333:8333 --name backend-example registry.uwm-nm-te-capstone.com:8083/$CI_PROJECT_NAME:latest"
docker run -d -p 8333:8333 --name backend-example registry.uwm-nm-te-capstone.com:8083/$CI_PROJECT_NAME:latest