def call(taskFamily, cluster, service, dockerImageName, releaseNumber){
  sh """
      export AWS_DEFAULT_REGION="us-east-1"
      TASK_FAMILY=${taskFamily}
      SERVICE_NAME=${service}
      NEW_DOCKER_IMAGE="341373978347.dkr.ecr.us-east-1.amazonaws.com/${dockerImageName}:${releaseNumber}"
      CLUSTER_NAME=${cluster}
      RELEASE_NUMBER=${releaseNumber}

      OLD_TASK_DEF=\$(aws ecs describe-task-definition --task-definition \$TASK_FAMILY --output json)
      TASK_WITH_NEW_VERSION_NUM=\$(echo \$OLD_TASK_DEF | jq -r --arg RELEASE_NUMBER \$RELEASE_NUMBER '.taskDefinition.containerDefinitions[0].environment |= map(if .name=="RELEASE_VERSION" then .value=\$RELEASE_NUMBER else . end)'
      NEW_TASK_DEF=\$(echo \$TASK_WITH_NEW_VERSION_NUM | jq --arg NDI \$NEW_DOCKER_IMAGE '.taskDefinition.containerDefinitions[0].image=\$NDI')
      FINAL_TASK=\$(echo \$NEW_TASK_DEF | jq '.taskDefinition|{family: .family, volumes: .volumes, containerDefinitions: .containerDefinitions}')
      aws ecs register-task-definition --family \$TASK_FAMILY --network-mode "awsvpc" --memory 512 --cpu 256 --execution-role-arn "arn:aws:iam::341373978347:role/ecsTaskExecutionRole" --task-role-arn "arn:aws:iam::341373978347:role/ecsTaskExecutionRole" --requires-compatibilities "FARGATE" --cli-input-json "\$(echo \$FINAL_TASK)"
      aws ecs update-service --service \$SERVICE_NAME --task-definition \$TASK_FAMILY --cluster \$CLUSTER_NAME
      """
}
