# Spring Integration Demo
See chat for full implementation.# spring-dsl-flow

# we have made progress till the trigger creation image storage 


[namespace.yaml](k8s/base/namespace.yaml)
[service-account.yaml](k8s/base/service-account.yaml)
[configMap.yaml](k8s/base/configMap.yaml)
[sftp-deployment.yaml](k8s/base/sftp-deployment.yaml)
[deployment.yaml](k8s/base/deployment.yaml)
[service.yaml](k8s/base/service.yaml)


kubectl create secret generic sftp-private-key --from-file=id_rsa=/home/prachi/.ssh/id_rsa  -n uat1