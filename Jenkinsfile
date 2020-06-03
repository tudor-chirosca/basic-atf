pipeline {
    agent any
    environment {
        ARTIFACTORY_CREDENTIALS = credentials('artifactory-credentials')
        ARTIFACTORY_URL = 'https://repo-api.mcvl-engineering.com/repository'
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
        stage('Publish to artifactory') {
            /*when {
                branch 'master'
            }*/
            steps {
                rtServer(
                        id: "ARTIFACTORY_SERVER",
                        url: "${ARTIFACTORY_URL}",
                        credentialsId: "artifactory-credentials"
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

    }
}

