pipeline {
    agent { label 'production-agent' }

    stages {
        stage('Code') {
            steps {
                echo 'Pulling code from GitHub'
                git branch: 'master', url: 'https://github.com/iam-avinash-jagtap/2-Tier-Application.git'
                echo 'Code Clone Successful.'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the Docker image...'
                sh 'docker build -t flaskapp:latest .'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing the application...'
                // Add test commands here if needed
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying the application on Docker...'
                sh 'docker-compose down'
                sh 'docker-compose up -d'
            }
        }
    }
}