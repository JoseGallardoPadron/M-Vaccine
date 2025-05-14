pipeline {
    agent any
    
    environment {
        SONARQUBE = 'SonarCloud'  // Nombre del servidor SonarQube configurado en Jenkins
        SONAR_TOKEN = credentials('SONAR_TOKEN')  // Token de SonarCloud como credencial
        PROJECT_KEY = 'JoseGallardoPadron_M-Vaccine'  // Nombre corregido del proyecto en SonarCloud
        SONAR_ORGANIZATION = 'Jose_Gallardo'  // Organización en SonarCloud
    }
    
    tools {
        maven 'Maven 3.8.1'  // Maven configurado en Jenkins
        jdk 'JDK 17'  // JDK 17 configurado en Jenkins
    }
    
    stages {
        stage('Clone Repository') {
            steps {
                // Clonar el repositorio y cambiar a la rama 'main'
                git branch: 'main', url: 'https://github.com/JoseGallardoPadron/M-Vaccine'
            }
        }
        
        stage('Compile with Maven') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Run Unit Tests') {
            steps {
                sh '''
                    mvn test \
                        -Dtest=ProductServiceTest,SupplierServiceTest,TypeSupplierServiceTest
                '''
            }
            post {
                always {
                    // Publicar resultados de pruebas
                    junit testResults: 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv("${env.SONARQUBE}") {
                        sh '''
                            mvn sonar:sonar \
                                -Dsonar.projectKey=${PROJECT_KEY} \
                                -Dsonar.organization=${SONAR_ORGANIZATION} \
                                -Dsonar.host.url=https://sonarcloud.io \
                                -Dsonar.login=${SONAR_TOKEN} \
                                -Dsonar.projectName=Transac_kardex \
                                -Dsonar.qualitygate.wait=true \
                                -Dsonar.scanner.force=true \
                                -Dsonar.scm.disabled=true \
                                -Dsonar.scm.provider=git \
                                -Dsonar.analysis.mode=publish
                        '''
                    }
                }
            }
        }
        
        stage('Generate .jar Artifact') {
            steps {
                sh 'mvn package -DskipTests'
            }
            post {
                always {
                    // Archivar el JAR generado
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
    }
    
    post {
        always {
            // Limpiar workspace después de cada build
            cleanWs()
        }
        success {
            echo 'Build completado exitosamente!'
            echo 'Análisis de SonarQube pasado!'
        }
        failure {
            echo 'Build fallido!'
        }
        unstable {
            echo 'Build inestable!'
        }
    }
}
