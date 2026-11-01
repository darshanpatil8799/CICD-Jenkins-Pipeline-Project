# Jenkins CI/CD DemoApp

A small Spring Boot application for practising the pipeline flow GitHub → Jenkins → Maven → SonarQube → Docker Hub → Kubernetes, matching the supplied project notes. fileciteturn0file0L7-L10

## Project structure

```text
jenkins-demoapp-project/
├── Dockerfile
├── Jenkinsfile
├── pom.xml
├── .gitignore
├── README.md
├── k8s/deployment.yaml
└── src/main/java/com/example/demoapp/DemoAppApplication.java
```

## Before running the pipeline

1. Push this project to your GitHub repository.
2. In Jenkins, configure JDK tool name `jdk` and Maven tool name `maven3`.
3. Configure a SonarQube server named `SonarQube` in **Manage Jenkins → System**.
4. Add a Jenkins username/password credential with ID `dockerhub-credentials` for Docker Hub.
5. Add a Jenkins **Secret file** credential with ID `kubeconfig` containing a kubeconfig file that can access your Kubernetes cluster.
6. In `Jenkinsfile`, replace `YOUR_DOCKERHUB_USERNAME/demoapp` with your Docker Hub username/repository.
7. Ensure the Jenkins agent has Docker and `kubectl` available, and the Jenkins user can access the Docker daemon.
8. Configure a GitHub webhook if you want builds to trigger automatically after pushes.

## Run locally

Requirements: Java 17 and Maven.

```bash
mvn clean package
java -jar target/demoapp.jar
```

Open `http://localhost:8080/`. Health check: `http://localhost:8080/health`.

## Kubernetes

Update the image in `k8s/deployment.yaml` if deploying manually, then run:

```bash
kubectl apply -f k8s/deployment.yaml
kubectl get deployments,pods,services
kubectl rollout status deployment/demoapp
```

The Service uses NodePort `30080`; access it as `http://<node-ip>:30080` where your cluster/network allows NodePort traffic.

## Security and scope

- Never commit Docker Hub passwords, SonarQube tokens or kubeconfig contents.
- This is a learning/demo setup, not a production-hardened deployment.
- Jenkins tool names and credential IDs must match the names configured in Jenkins.
