#!/bin/bash

PROJECT_NAME="scheduler"
DOCKER_COMPOSE_CONFIGS_YML="--project-name ${PROJECT_NAME}"
DOCKER_COMPOSE_CONFIGS_YML="${DOCKER_COMPOSE_CONFIGS_YML} -f ${ROOT_DIR}/docker-compose.yml"

DEFAULT_SERVICES="app "

green_echo() { echo -e "\e[1;32m ${1} \e[0m" ; }

yellow_echo() { echo -e "\e[1;33m ${1} \e[0m" ; }

red_echo() { echo -e "\e[1;31m ${1} \e[0m" ; }

load_file() {
	if [ -f "${1}" ]; then
	    . ${1}
	fi
}

list_containers() {
	echo "CONTAINERS:"
	docker ps -a --format 'table {{.ID}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}'
}

stop_all_containers() {
    if [ ! -z "$(docker ps -a -q)" ]; then
        echo "Stoping  all running containers"
        docker stop $(docker ps -a -q)
    fi
}

remove_stoped_containers() {
    if [ ! -z "$(docker ps -q -f status=exited)" ]; then
        echo "Removing stoped containers"
        docker rm $(docker ps -q -f status=exited)
    fi
}

remove_untaged_images() {
    if [ ! -z "$(docker images -f dangling=true -q)" ]; then
        DELETED_IMAGES=`docker rmi $(docker images -f dangling=true -q) | grep -c 'Deleted:'`
        echo "Deleting: ${DELETED_IMAGES} untaged subimages"
    fi
}

remove_network() {
    if [ ! -z "$(docker network ls | grep '${1}')" ]; then
        echo "Deleting network:"
        docker network rm "${1}"
    fi
}

fatal() {
    red_echo "${1}"
    red_echo "Exit.."
    exit 1
}

# possition
if [ ! -d "${ROOT_DIR}" ]; then
    cd ../
fi
if [ ! -d "${ROOT_DIR}" ]; then
    fatal "Not on project ROOT (${ROOT_DIR})."
fi

# defaults
SKIP_MAVEN=false
ALL=false
PURGE=false

# parse parameters
for arg; do
    if [ "${arg:0:2}" != "--" ]; then
        args="${args} ${arg}"
    else
        case "$arg" in
           --skip-maven) SKIP_MAVEN=true ;;
           --all) ALL=true ;;
           --purge) PURGE=true ;;
        esac
    fi
done

# fallback
if [ -z "${args}" ]; then
    args=${DEFAULT_SERVICES}
    PURGE=true
fi;
eval set -- $args
