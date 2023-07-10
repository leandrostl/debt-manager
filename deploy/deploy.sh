#!/bin/bash

## Inicia o Minikube
#minikube start
#
# Habilita o uso do Docker do Minikube
eval $(minikube docker-env)

printf "\n🏗️ Building application...\n"
./gradlew clean build -x test

cd deploy || exit
printf "\n📦 Building docker image...\n"
docker build .. -t leandro/debt-manager:v1

# Aplica as configurações de deployment e serviço
printf "\n🗃️ Applying database...\n"
kubectl apply -f db-deployment.yaml

printf "\n🚀 Applying application...\n"
kubectl apply -f deployment.yaml

SERVICE_IP=$(minikube ip)
DB_SERVICE_PORT=$(kubectl get service postgres-db-service -o jsonpath='{.spec.ports[0].nodePort}')
DB_URL="jdbc:postgresql://$SERVICE_IP:$DB_SERVICE_PORT/debtmanager"
# Atualiza a propriedade da URL do banco de dados no arquivo de propriedades
printf "\n🔄 Alterando a url do banco de dados nas propriedades do projeto em local-prod"
sed -i "s|jdbc:postgresql://.*$|${DB_URL}|" "../src/main/resources/application-local-prod.properties"

# Exibe a URL para acessar a aplicação
printf "\n🗃️ Acesse o banco de dados através da url: %s" "$DB_URL"
# Obtém o IP e a porta do serviço exposto pelo Minikube
APP_SERVICE_PORT=$(kubectl get service debt-manager -o jsonpath='{.spec.ports[0].nodePort}')
APP_URL="http://$SERVICE_IP:$APP_SERVICE_PORT/debts"

printf "\n🔄 Alterando a url da aplicação nos utilitários de criação e alteração de dívidas..."
sed -i "s|http://.*/debts|${APP_URL}|" "../utils/DebtsCreator.py"
sed -i "s|http://.*/debts|${APP_URL}|" "../utils/DebtUpdaterJob.py"


printf "\n🚀 Acesse a aplicação em: %s" "$APP_URL"
