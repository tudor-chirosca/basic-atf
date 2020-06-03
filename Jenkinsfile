pipeline {
    agent any
    environment {
        TOMCAT_URL = 'http://10.119.11.129:8081/'
        ARTIFACTORY_CREDENTIALS = credentials('artifactory-credentials')
        ARTIFACTORY_URL = 'https://repo-api.mcvl-engineering.com/repository'
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: "*/${env.BRANCH_NAME}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'tech-user', url: 'https://github-api.mcvl-engineering.com/vocalink-portal/cp-api-management']]])
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
        stage('Integration test') {
            steps {
                echo './mvnw -B test'
            }
        }
        stage('Package WAR') {
            steps {
                sh './mvnw clean package'
            }
        }
        stage('Publish to artifactory') {
            when {
                branch 'master'
            }
            steps {
                sh 'export GRADLE_USER_HOME=/var/lib/jenkins/tools/hudson.plugins.gradle.GradleInstallation/GRADLE-ORG'

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
                        snapshotRepo: "cp-portal-staging",
                        releaseRepo: "cp-portal-release"
                )
                rtMavenRun(
                        tool: MAVEN_TOOL, // Tool name from Jenkins configuration
                        pom: 'pom.xml',
                        goals: 'clean install',
                        deployerId: "MAVEN_DEPLOYER",
                        resolverId: "MAVEN_RESOLVER"
                )
//                rtPublishBuildInfo (
//                        serverId: "ARTIFACTORY_SERVER",
//                        buildName: "cpportal",
//                        buildNumber: "1"
//                        //buildName: "cp-api-management-build",
//                        //buildNumber: "${env.BUILD_NUMBER}"
//                )
            }
        }
//        stage('Deploy to development') {
//            when {
//                branch 'develop'
//            }
//            steps {
//                deploy adapters: [tomcat8(credentialsId: 'remote-tomcat-aws', path: '', url: "${TOMCAT_URL}")], contextPath: 'cp-api-management', war: '**/*.war'
//            }
//        }
//        stage('Deploy to production') {
//            when {
//                branch 'master'
//            }
//            steps {
//                deploy adapters: [tomcat8(credentialsId: 'remote-tomcat-aws', path: '', url: "${TOMCAT_URL}")], contextPath: 'cp-api-management', war: '**/*.war'
//            }
//        }
    }
}

