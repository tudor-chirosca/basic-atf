pipeline {
    agent any
    environment {
        ARTIFACTORY_URL = 'https://repo-api.mcvl-engineering.com/repository'
        PROJECT_NAME = 'cp-api-management'
        GITHUB_URL = "https://github-api.mcvl-engineering.com/vocalink-portal/${PROJECT_NAME}"
        ARTIFACTORY_CREDENTIALS = 'artifactory-credentials'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: env.BRANCH_NAME]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'tech-user', url: env.GITHUB_URL]]])
            }
        }
        stage('Compile') {
            steps {
                sh './mvnw -B compile'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw -B test'
            }
        }
        stage('Packbage WAR') {
            steps {
                sh './mvnw clean package'
            }
        }
        stage('Publish artifact') {

            steps {
                rtServer(
                        id: "ARTIFACTORY_SERVER",
                        url: env.ARTIFACTORY_URL,
                        credentialsId: env.ARTIFACTORY_CREDENTIALS
                )

                rtMavenDeployer(
                        id: "MAVEN_DEPLOYER",
                        serverId: "ARTIFACTORY_SERVER",
                        snapshotRepo: "cp-portal-staging",
                        releaseRepo: "cp-portal-release",
                )
                rtMavenResolver(
                        id: "MAVEN_RESOLVER",
                        serverId: "ARTIFACTORY_SERVER",
                        snapshotRepo: "cp-portal-group",
                        releaseRepo: "cp-portal-group"
                )
                rtMavenRun(
                        tool: 'MAVEN_TOOL', // Tool name from Jenkins configuration
                        pom: 'pom.xml',
                        goals: 'clean install -U',
                        deployerId: "MAVEN_DEPLOYER",
                        resolverId: "MAVEN_RESOLVER"
                )
            }
        }
        stage('Publish docker staging') {
            when {
                not {
                    branch 'master'
                }
            }
            steps {
                script {
                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def gitTag = "${BUILD_NUMBER}.${BRANCH_NAME}.${gitCommit}"

                    docker.withRegistry(env.ARTIFACTORY_URL, env.ARTIFACTORY_CREDENTIALS) {
                        def image = docker.build("cp-portal-docker-staging/${PROJECT_NAME}:${gitTag}")
                        image.push()
                    }
                }
            }
        }
        stage('Publish docker release') {
            when {
                branch 'master'
            }
            steps {
                script {
                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def gitTag = "${BUILD_NUMBER}.${BRANCH_NAME}.${gitCommit}"

                    docker.withRegistry(env.ARTIFACTORY_URL, env.ARTIFACTORY_CREDENTIALS) {
                        def image = docker.build("cp-portal-docker-release/${PROJECT_NAME}:${gitTag}")
                        image.push()
                    }
                }
            }
        }

        stage('Deploy preprod') {
            when {
                branch 'master'
            }
            steps {
                script {
                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def gitTag = "${BUILD_NUMBER}.master.${gitCommit}"

                    echo "Deploying ${gitTag} to ${PREPROD_IP}"

                    withCredentials([usernamePassword(credentialsId: env.ARTIFACTORY_CREDENTIALS, passwordVariable: 'ARTIFACTORY_PASSWORD', usernameVariable: 'ARTIFACTORY_USERNAME')]) {
                        sshagent(credentials: ['deployer']) {
                            sh "ssh -o StrictHostKeyChecking=no deployer@${PREPROD_IP} docker login -u ${ARTIFACTORY_USERNAME} -p ${ARTIFACTORY_PASSWORD} ${ARTIFACTORY_URL}"
                            sh "ssh -o StrictHostKeyChecking=no deployer@${PREPROD_IP} docker run --network host -d -p 8080:8080 --name ${PROJECT_NAME} repo-api.mcvl-engineering.com/cp-portal-docker-release/${PROJECT_NAME}:${gitTag}"
                        }
                    }
                }
            }
        }
    }
}
