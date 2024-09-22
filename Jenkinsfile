pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/cresb-49/PROYECTO2_AYD1.git'
            }
        }
        stage('Build Backend') {
            steps {
                dir('api_citas') {
                    echo 'Building Spring Boot Application...'
                    bat 'mvn clean package -DskipTests' // Compilar el proyecto y generar el archivo .war
                }
            }
        }
        stage('Deploy Backend to Tomcat') {
            steps {
                echo 'Deploying Backend Application to Tomcat...'
                script {
                    // Ruta del archivo WAR generado por Maven
                    // def warFile = 'api_citas/target/api_citas.war'
                    
                    // Ruta del directorio webapps de Tomcat
                    // def tomcatWebapps = 'C:\\tomcat\\webapps'
                    
                    // Copiar el archivo WAR al directorio webapps de Tomcat
                    // bat "copy /Y ${warFile} ${tomcatWebapps}"
                    
                    // Reiniciar Tomcat
                    // bat 'C:\\tomcat\\bin\\shutdown.bat'
                    // bat 'C:\\tomcat\\bin\\startup.bat'
                }
            }
        }
        stage('Build Frontend') {
            steps {
                dir('app_citas') {
                    echo 'Building Angular Application...'
                    bat 'npm install'
                    bat 'npm run build --prod'
                }
            }
        }
        stage('Restart Nginx') {
            steps {
                echo 'Restarting Nginx...'
                bat 'nginx -c C:\\nginx\\conf\\nginx.conf -s reload'
            }
        }
    }
}
