pipeline {
    agent any

    environment {
        REGISTRY = "docker.io/chanathipcha24"
        IMAGE_NAME = "yorlink-backend"
        POSTGRES_PORT = "5432"
        KUBE_DEPLOYMENT = "k8s/backend-deployment.yaml"
    }

    stages {
        stage('Checkout Backend') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                sh """
                    docker build -t $REGISTRY/$IMAGE_NAME:latest .
                """
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-cred', 
                    usernameVariable: 'DOCKER_USER', 
                    passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push $REGISTRY/$IMAGE_NAME:latest
                    """
                }
            }
        }

        stage('Create Kubernetes Secret') {
            steps {
                withCredentials([
                    string(credentialsId: 'POSTGRES_DB', variable: 'POSTGRES_DB'),
                    string(credentialsId: 'POSTGRES_USER', variable: 'POSTGRES_USER'),
                    string(credentialsId: 'POSTGRES_PASSWORD', variable: 'POSTGRES_PASSWORD'),
                    string(credentialsId: 'POSTGRES_HOST', variable: 'POSTGRES_HOST'),
                    string(credentialsId: 'APP_CORS_ALLOWED_ORIGINS', variable: 'APP_CORS_ALLOWED_ORIGINS')
                ]) {
                    sh """
                        microk8s kubectl delete secret yorlink-backend-env --ignore-not-found
                        microk8s kubectl create secret generic yorlink-backend-env \\
                            --from-literal=POSTGRES_DB=$POSTGRES_DB \\
                            --from-literal=POSTGRES_USER=$POSTGRES_USER \\
                            --from-literal=POSTGRES_PASSWORD=$POSTGRES_PASSWORD \\
                            --from-literal=POSTGRES_HOST=$POSTGRES_HOST \\
                            --from-literal=APP_CORS_ALLOWED_ORIGINS=$APP_CORS_ALLOWED_ORIGINS
                            --from-literal=POSTGRES_PORT=$POSTGRES_PORT
                    """
                }
            }
        }

        stage('Deploy to MicroK8s') {
            steps {
                echo "Deploying backend to MicroK8s..."
                sh "microk8s kubectl apply -f $KUBE_DEPLOYMENT"
            }
        }
        stage('Deploy Ingress') {
            steps {
                sh "microk8s kubectl apply -f k8s/ingress-deployment.yaml"
            }
        }

        stage('Verify Deployment') {
            steps {
                sh """
                    microk8s kubectl get pods
                    microk8s kubectl get svc
                """
            }
        }
    }

    post {
        always {
            echo "Cleaning up Docker images..."
            sh "docker image prune -f"
        }
    }
}
