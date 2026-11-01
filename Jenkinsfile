pipeline {
    agent any

    tools {
        jdk 'jdk'
        maven 'maven3'
    }

    environment {
        SCANNER_HOME = tool 'sonar-scanner'
        DOCKER_IMAGE = 'darshanpatil0507/demo1'

        SONAR_PROJECT_NAME = 'CICD-Jenkins-Pipeline-Project'
        SONAR_PROJECT_KEY = 'CICD-Jenkins-Pipeline-Project'
    }

    stages {

        stage('Git Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/darshanpatil8799/CICD-Jenkins-Pipeline-Project.git'
            }
        }

        stage('Code Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        $SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.projectName=${SONAR_PROJECT_NAME} \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build \
                    -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .
                    
                    docker tag \
                    ${DOCKER_IMAGE}:${BUILD_NUMBER} \
                    ${DOCKER_IMAGE}:latest
                '''
            }
        }

        stage('Docker Image Push') {
            steps {
                withDockerRegistry(
                    credentialsId: 'dockerhub-credentials',
                    url: 'https://index.docker.io/v1/'
                ) {
                    sh '''
                        docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                        docker push ${DOCKER_IMAGE}:latest
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withKubeConfig([
                    credentialsId: 'kubeconfig-credentials'
                ]) {
                    sh '''
                        set -e

                        echo "Deploying application to Kubernetes..."

                        kubectl apply -f k8s/deployment.yaml

                        kubectl set image deployment/demoapp \
                        demoapp=${DOCKER_IMAGE}:${BUILD_NUMBER}

                        kubectl rollout status deployment/demoapp \
                        --timeout=180s

                        echo "Deployment Information"

                        kubectl get deployments
                        kubectl get pods -o wide
                        kubectl get services
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: CI/CD Pipeline completed!'
            echo 'Maven build completed successfully!'
            echo 'SonarQube analysis completed!'
            echo 'Docker image pushed successfully!'
            echo 'Application deployed to Kubernetes!'
        }

        failure {
            echo 'FAILURE: Check Jenkins Console Output.'
        }

        always {
            echo 'Jenkins Pipeline execution completed.'
        }
    }
}
