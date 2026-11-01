pipeline {
    agent any

    tools {
        jdk 'jdk'
        maven 'maven3'
    }

    environment {
        SCANNER_HOME = tool 'sonar-scanner'

        DOCKER_IMAGE = 'darshanpatil0507/demo'

        SONAR_PROJECT_NAME = 'new'
        SONAR_PROJECT_KEY = 'new1'
    }

    stages {

        stage('Git Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/ebotsmith2000/jenkins-pipeline.git'
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
                '''
            }
        }

        stage('Docker Image Push') {
            steps {
                withDockerRegistry(
                    credentialsId: 'Dockerhub_cred',
                    url: 'https://index.docker.io/v1/'
                ) {
                    sh '''
                        docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([
                    file(
                        credentialsId: 'kubeconfig',
                        variable: 'KUBECONFIG'
                    )
                ]) {
                    sh '''
                        set -e

                        echo "Deploying application to Kubernetes..."

                        echo "Docker Image: ${DOCKER_IMAGE}:${BUILD_NUMBER}"

                        # Replace image placeholder with current build image
                        sed -i "s|IMAGE_PLACEHOLDER|${DOCKER_IMAGE}:${BUILD_NUMBER}|g" \
                        k8s/deployment.yaml

                        # Verify generated image
                        grep "image:" k8s/deployment.yaml

                        # Validate Kubernetes configuration
                        kubectl apply --dry-run=client -f k8s/deployment.yaml

                        # Deploy application
                        kubectl apply -f k8s/deployment.yaml

                        # Wait for successful deployment
                        kubectl rollout status deployment/demoapp \
                        --timeout=180s

                        # Display deployment information
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