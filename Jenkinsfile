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
                echo 'Simulating Spring Boot Application build...'
                // Aquí se comenta la compilación real del backend
                // dir('api_citas') {
                //     echo 'Building Spring Boot Application...'
                //     bat 'mvn clean package -DskipTests' // Compilar el proyecto y generar el archivo .war
                // }
            }
        }
        stage('Deploy Backend to Tomcat') {
            steps {
                echo 'Simulating Backend Deployment to Tomcat...'
                script {
                    // Si quieres que Jenkins actúe como si hubiera hecho el despliegue
                    def warFile = 'api_citas/target/api_citas.war'
                    def tomcatWebapps = 'C:\\tomcat\\webapps'
                    
                    // Este es solo un ejemplo de lo que ocurriría si realmente copiaras el archivo WAR
                    // bat "copy /Y ${warFile} ${tomcatWebapps}"
                    
                    // Simular reinicio de Tomcat
                    // bat 'C:\\tomcat\\bin\\shutdown.bat'
                    // bat 'C:\\tomcat\\bin\\startup.bat'
                    echo 'Tomcat restarted (simulation)'
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
        stage('Deploy Frontend') {
            steps {
                echo 'Deploying Frontend Application...'
                bat 'npm install -g http-server'
                bat 'http-server app_citas/dist -p 4200'
            }
        }
    }
}
