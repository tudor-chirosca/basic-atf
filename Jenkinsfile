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
                sh './gradlew compileJava --info'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test --info'
            }
        }
        stage('Integration test') {
            steps {
                echo './gradlew test --info'
            }
        }
        stage('Package WAR') {
            steps {
                sh './gradlew war --info'
            }
        }
        stage('Package WAR') {
            steps {
                sh './gradlew war --info'
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

                rtGradleDeployer(
                        id: "GRADLE_DEPLOYER",
                        serverId: "ARTIFACTORY_SERVER",
                        repo: "cp-portal-staging",
                )
                rtBuildInfo(
                        captureEnv: true,
                        includeEnvPatterns: ["*"],
                        excludeEnvPatterns: ["DONT_COLLECT*"]
                )
                rtGradleResolver(
                        id: "GRADLE_RESOLVER",
                        serverId: "ARTIFACTORY_SERVER",
                        repo: "cp-portal-group"
                )
                rtGradleRun(
                        usesPlugin: true,
                        tool: "GRADLE-JENKINS",
                        buildFile: 'build.gradle',
                        tasks: 'clean artifactoryPublish --stacktrace --info --scan ',
                        deployerId: "GRADLE_DEPLOYER",
                        resolverId: "GRADLE_RESOLVER"
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
        stage('Deploy to development') {
            when {
                branch 'develop'
            }
            steps {
                deploy adapters: [tomcat8(credentialsId: 'remote-tomcat-aws', path: '', url: "${TOMCAT_URL}")], contextPath: 'cp-api-management', war: '**/*.war'
            }
        }
        stage('Deploy to production') {
            when {
                branch 'master'
            }
            steps {
                deploy adapters: [tomcat8(credentialsId: 'remote-tomcat-aws', path: '', url: "${TOMCAT_URL}")], contextPath: 'cp-api-management', war: '**/*.war'
            }
        }
    }
}

