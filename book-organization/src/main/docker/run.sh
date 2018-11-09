#!/bin/sh

getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z eurekaserver  $(getPort $EUREKASERVER_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo echo "Waiting for the configuration server to start on port $(getPort $CONFIGSERVER_URI)"
echo "********************************************************"
while ! `nc -z configserver $(getPort $CONFIGSERVER_URI)`; do sleep 3; done
echo "*******  Configuration Server has started"


echo "********************************************************"
echo "Waiting for the REDIS server to start  on port $(getPort $REDIS_PORT)"
echo "********************************************************"
while ! `nc -z redis $(getPort $REDIS_PORT)`; do sleep 3; done
echo "******* REDIS has started"

#echo "********************************************************"
#echo "Waiting for the RabbitMQ server to start on port $RABBIT_PORT"
#echo "********************************************************"
#while ! `nc -z rabbit $RABBIT_PORT`; do sleep 10; done
#echo "******* RabbitMQ Server has started **********"

echo "********************************************************"
echo "Starting Organization Service                           "
echo "Using profile: $PROFILE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI             \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                          \
     -Dspring.profiles.active=$PROFILE                                   \
     -Dspring.cloud.stream.rabbit.binder.node=$RABBIT_URI                 \
     -Dsecurity.oauth2.resource.userInfoUri=$AUTHSERVER_URI               \
      -jar ./@project.build.finalName@.jar