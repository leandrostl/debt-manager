#!/bin/bash

# Inicia o Minikube
minikube start

# Habilita o uso do Docker do Minikube
eval $(minikube docker-env)

echo "Building application..."
./gradlew clean build -x test

cd deploy || exit
echo "Building docker image..."
docker build .. -t leandro/debt-manager:v1

# Aplica as configurações de deployment e serviço
echo "Applying database..."
kubectl apply -f db-deployment.yaml
echo "Applying application..."
kubectl apply -f deployment.yaml

# Obtém o IP e a porta do serviço exposto pelo Minikube
SERVICE_IP=$(minikube ip)
SERVICE_PORT=$(kubectl get service debt-manager -o jsonpath='{.spec.ports[0].nodePort}')

# Exibe a URL para acessar a aplicação
echo "🎉 Acesse a aplicação em: http://$SERVICE_IP:$SERVICE_PORT"
