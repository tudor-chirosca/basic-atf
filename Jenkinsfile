pipeline {
    agent any
    environment {
        ARTIFACTORY_URL = 'https://repo-api.mcvl-engineering.com/repository'
        ARTIFACTORY_CREDENTIALS = 'artifactory-credentials'
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: "master"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'tech-user', url: 'https://github-api.mcvl-engineering.com/vocalink-portal/cp-api-management']]])
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
        stage('Package WAR') {
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
        stage('Publish docker image') {
            steps {
                script {
                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def gitTag = "${BUILD_NUMBER}.${BRANCH_NAME}.${gitCommit}"

                    docker.withRegistry(env.ARTIFACTORY_URL, env.ARTIFACTORY_CREDENTIALS) {
                        def customImage = docker.build("cp-portal-docker-staging/cp-api-management:${gitTag}")
                        customImage.push()
                    }
                }
            }
        }
    }
    //test jenkins
}

