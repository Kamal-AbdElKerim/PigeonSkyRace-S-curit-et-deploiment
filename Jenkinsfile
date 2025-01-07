pipeline {
    agent any
    environment {
        DOCKER_CONTAINER_NAME = 'pigeonskyrace-securite3'
    }
    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/Kamal-AbdElKerim/PigeonSkyRace-S-curit-et-deploiment'
            }
        }
        stage('Build') {
            steps {
                script {
                    // Assuming you are using Maven
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        stage('Manual Approval for Deployment') {
            steps {
                input 'Approve Deployment to Docker?'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image from Dockerfile
                    sh 'docker build -t $DOCKER_IMAGE .'
                }
            }
        }
        stage('Run Docker Container') {
            steps {
                script {
                    // Run the container in detached mode
                    sh "docker run -d --name $DOCKER_CONTAINER_NAME $DOCKER_IMAGE"
                }
            }
        }
        stage('Run Unit Tests') {
            steps {
                script {
                    // Run unit tests with Maven and generate reports
                    sh 'mvn test'
                }
            }
        }
    }
    post {
        success {
            // Notifications for successful builds
            emailext (
                subject: 'Build Successful',
                body: 'The build was successful and the application is deployed.',
                to: 'developer@example.com'
            )
        }
        failure {
            // Notifications for failed builds
            emailext (
                subject: 'Build Failed',
                body: 'The build failed. Please check the logs for more details.',
                to: 'developer@example.com'
            )
        }
    }
}
