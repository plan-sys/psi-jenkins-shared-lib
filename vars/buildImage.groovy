def call(imageName, releaseNumber, projectFolder, buildArgString) {
  echo "Hello!";
  sh """
            export AWS_DEFAULT_REGION="us-east-1"
            echo "Logging in to Docker via AWS"
            _DOCKER_REPO="\$(aws ecr get-authorization-token --output text  --query 'authorizationData[].proxyEndpoint')"
            aws ecr get-login --no-include-email --region us-east-1 | awk '{print \$6}' | docker login -u AWS --password-stdin \$_DOCKER_REPO
            echo "Building docker image"
            cd ${projectFolder}
            docker build -t "${imageName}:${releaseNumber}" ${buildArgString} .
            _DOCKER_REPO_NO_PRE="\$(echo \$_DOCKER_REPO | sed 's~http[s]*://~~g')"
            docker tag "${imageName}:${releaseNumber}" "\$_DOCKER_REPO_NO_PRE/${imageName}:${releaseNumber}"
            docker push "\$_DOCKER_REPO_NO_PRE/${imageName}:${releaseNumber}"
     """
}
