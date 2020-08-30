pipeline {
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-p 8888:8888 -v /root/.m2:/root/.m2 -v /root/upload:/root/upload'
        }
    }
    stages { 
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }       
        stage('Deploy') { 
            steps {
                sh 'java -jar target/adminsystem-dev.jar'
            }
        }
    }
}
