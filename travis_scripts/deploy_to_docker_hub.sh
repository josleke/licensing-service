echo "Pushing service docker images to docker hub ...."
docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
docker push josleke/oauth-service:$BUILD_NAME
docker push josleke/license-service:$BUILD_NAME
docker push josleke/organization-service:$BUILD_NAME
docker push josleke/config-service:$BUILD_NAME
docker push josleke/discovery-service:$BUILD_NAME
docker push josleke/gateway-service:$BUILD_NAME