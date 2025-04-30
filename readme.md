# Automated 2-Tier Flask Application Deployment Using Jenkins and Docker
Hello, in this project you are going to deploy a 2-tier application using a automated Jenkins CI/CD pipeline with Docker. The pipeline is fully automated, it pulls the source code from GitHub, builds Docker images for the frontend and backend, and deploys them using Docker Compose. Jenkins orchestrates the entire process, ensuring seamless integration and delivery. Docker is used for containerization, while Docker Compose handles multi-container deployment. This setup ensures consistent, repeatable, and hands-free deployments to the production environment.

## Pre-requisites
- AWS Account 
- Git installed. 
- Jenkins installed. 
- Docker Compose installed.
- A working GitHub repository.
- Proper Jenkins script pipeline.
- Docker installed on the Jenkins server.
- Solid expertise in automation and scripting.
- Good Knowledge of Linux and 2-tier application.
- Internet access on Jenkins and production server.
- Solid knowledge of Docker and containerization concepts.
  
## Learning Objectives
_Upon completion of this project, you will be able to configure a CI/CD pipeline on Jenkins along with other DevOps resources._
- GitHub Integration
- CI/CD Implementation
- Automated Deployments
- Docker Image Building
- DevOps Workflow Practice
- Real-world Project Setup
- Jenkins Pipeline Creation
- Infrastructure Automation
- Docker Compose Deployment
- Multi-Container Management

## AWS Set-up
_You are going to deploy the Jenkins server and the production server on AWS EC2 instances._
1. Jenkins Server 
      1. Name - Jenkins Server
      2. OS - Amazon Linux 
      3. Instance Type - t2.medium
      4. Storage - 15 GB
      5. Security Group - Port (22,8080)
      6. Purpose - Manage CI/CD pipeline and Production server.
      7. Installation - Jenkins, Docker, Git
2. Production Server
      1. Name - Production Server 
      2. OS - Amazon Linux
      3. Instance Type - t2.micro
      4. Security Group - Port (22,5000)
      5. Storage - 8GB.
      6. Purpose - Runs the application using Docker containers.
      7. Installation - Docker, Docker-compose.

[AWS-EC2-Instance-Deployment-Steps](https://github.com/iam-avinash-jagtap/Linux-Server-Deployment-on-AWS-E2)

## Project Flow 
1. Code Pull: Github 
2. Code Build: Docker
3. Deployment: Docker-Compose
### All three stages will be automated using Jenkins.  
# Step 1:- Server Configuration for Your Application
_nstall the necessary packages and configurations on both servers for project execution._
1. Jenkins Server
      1. Get SSH access.
      2. Update Server.
      3. Install Tools and Dependencies
```bash
    #Update Server
    sudo yum update -y
     #Add Jenkins repository:
    sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo

    #Import Jenkins GPG key:        
    sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key

    #Upgrade packages after repo update:
     sudo yum update

   #Install Amazon Corretto 17 (OpenJDK 17):(Required for Jenkins)
    sudo yum install java-17-amazon-corretto -y

    #Install Jenkins:
    sudo yum install jenkins -y 

    #Enable Jenkins service on boot:
    sudo systemctl enable jenkins

    #Start Jenkins:
    sudo systemctl start jenkins

    #VErify Jenkis Version:
    jenkins --version

    #Install Docker:
    sudo yum install docker -y

    #Enable Docker service on boot:
    sudo systemctl enable docker

    #Start Docker:
    sudo systemctl start docker

    #Verify Docker Version:
    docker --version 

    #Install Git:
    Sudo yum install git -y 

    #Verify Git Version:
    git --version 

    #Get Jenkins Initial Admin Password:
    sudo cat /var/lib/jenkins/secrets/initialAdminPassword
    #Copy Password:

```
2. Access Jenkins
   1. Copy Public IP 
   2. Paste in Browser - x.x.x.x:8080
   3. Paste Administrator Password
   4. Sign In to Jenkins 
   5. Set Credintials
   6. Install recommended Plugins
   7. Restart Jenkins
### Jenkins server setup is complete.

3. Production Server
      1. Get SSH Access
      2. Update Server
      3. Install Tools and Dependencies
```bash
    #Update Server
    sudo yum update

    #Install Docker 
    sudo yum install docker

    #Enable Docker service on boot:
    sudo systemctl enable docker

    #Start Docker:
    sudo systemctl start dockre

    #Install Docker Compose:
    sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

    #Make Docker Compose executable:
    sudo chmod +x /usr/local/bin/docker-compose

    #Verify Docker Compose version:
    docker-compose --version 

```
# Step 2:- Connect the production server as a Jenkins agent. 
_Now you are going to add an agent to the Jenkins server, but before accessing the dashboard, create an SSH key to establish a connection between the Jenkins server and the production server._
1. Go to Jenkins Server
2. Open .ssh directory 
3. Create ssh key using - ssh-keygen 
4. copy Public key  - id_rsa.pub
5. Go to Production Server
6. Open .ssh directory 
7. Edit - authorized_keys
8. Paste Public key
9. Go to Jenkins Server
10. Copy Private key - id_rsa
### The public key has been copied successfully. Proceed to the dashboard.
1. Go to Jenkins Dashboard 
2. Select: Manage Jenkins 
3. Select: Nodes
4. Click On: New Node
   - Enter Name: production-Server
   - Select “Permanent Agent” and click OK
5. Configure Node Details
   - No of Executors: 1 
   - Remote root directory: /home/ec2-user/
   - Label: production-Server
   - Usage: "Use this node as much as possible"
   - Launch method: Launch agents via SSH
   - Enter SSH Details
     - Host: <Production-server-IP>
     - Credentials: Add Jenkins SSH credentials:
       - Click “Add” → Kind: SSH Username with private key
       - Username: ec2-user
       - Private Key: Paste the contents of Jenkins server's ~/.ssh/id_rsa
     - Save the credentials and select them from the dropdown
     - Click: Save
### The node should display as “Connected.”
# Step 3:- Build the CI/CD pipeline for automation.
_Before deploying the main code in the pipeline, test your setup to ensure everything is ready for the actual application deployment._
### Create a simple pipeline for Testing
1. Create a New Pipeline Job
   - Click on: “New Item”
   - Enter a name: Project_Pipeline
   - Select: “Pipeline” and Click: OK
2. Configure the Pipeline
   - Add a description - Test Pipeline
   - Scroll down to the Pipeline section
3.  Add Pipeline Script
   - In the Pipeline script section (use the "Pipeline script" option), paste the following:
```groovy
pipeline {
    agent { label 'production-agent' }  //label of your Agent Node.

    stages {
        stage('Hello') {
            steps {
                echo 'Hello, World! Running on production server'
            }
        }
    }
}

```
4. Save and Build
   - Click: “Save”
   - Then Click: “Build Now”
   - Check Console Output
```csharp
Hello, World! Running on production server.
```
### You can now include your main declarative script in the pipeline.
_You can now deploy your application using the pipeline._
1. Open Existing Pipeline Job
    - Go to Jenkins Dashboard → Click on your job
2. Click on: “Configure”
3. Update Job Description: This is My Project Pipeline
4. Enable: GitHub Project
    - Paste Project URL: https://github.com/iam-avinash-jagtap/2-Tier-Application.git/
    - Display Name - CI/CD Project
5. Triggers
    - Enable: GitHub hook trigger for GITScm polling
      1. In GitHub, go to your repository → Settings → Webhooks
      2. Click “Add webhook”
      3. Enter this URL in the Payload URL field: 
       
         'http://<your-jenkins-ip>:8080/github-webhook/'
      4. Content type: application/json
      5. Trigger: Just the push event
      6. Click: Add webhook
6. Pipeline 
   - Replace the declarative script with your actual pipeline script.

[Pipeline-Script](https://github.com/iam-avinash-jagtap/Automated-2-Tier-Application-Deployment-Using-Jenkins-and-Docker/blob/master/Flaskapp-Script.groovy)

7. Save and Build
   - Click: “Save”
   - Then Click: “Build Now”
   - Check Console Output
8. Stage View
    
    _Use the Pipeline Stage View plugin to display your pipeline logs in detail._
# Step 4:- Test Your Applicaton 
_The deployment was successful on the production server. You can check it now._
1. Copy: IP of Production Server 
2. Paste: in browser 
   - http://<Production-server-IP>:5000

### Your Application is Up and Running
# Step 5:- Test Your Automation 
_You have already created a webhook on GitHub and connected it with your Jenkins server._
1. Go to: GitHub Repository 
2. Edit: Code
3. Commit changes
4. Go to Jenkins Server
5. Check: Builds

_Your pipeline will automatically trigger the build._
### The automation of your setup is successful.

## Note:-
- Open the production server and check:
- The workspace directory — your project files will be there.
 
- Using Docker commands, you can:

1. Check running containers.

2. List Docker images.

3. Access the MySQL container.

4. Cross-check database entries inside the MySQL container.

# Summary 
This project showcases the end-to-end CI/CD automation of a 2-tier Flask-based web application using Jenkins and Docker. The application source code is hosted on GitHub and is automatically pulled into Jenkins upon any push event using GitHub webhooks. The Jenkins pipeline is configured declaratively to run on a dedicated production agent node hosted on a separate EC2 instance. It includes stages for cloning the repository, building a Docker image of the Flask app, testing the application, and deploying it using Docker Compose. The entire infrastructure is hosted on AWS, with one EC2 (t2.medium) instance acting as the Jenkins manager and another (t2.micro) as the production server. Secure SSH key-based communication is established between Jenkins and the agent node. Docker ensures containerized deployment, making the environment portable and consistent. This automated setup reduces manual deployment efforts and ensures quicker, reliable application delivery. Overall, the project reflects a solid foundation in DevOps practices, cloud infrastructure, and pipeline-based deployment automation.
