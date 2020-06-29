pipeline {
    agent any
    environment {
        ARTIFACTORY_DOMAIN = "repo-api.mcvl-engineering.com"
        ARTIFACTORY_URL = "https://${ARTIFACTORY_DOMAIN}/repository"
        PROJECT_NAME = "cp-api-management"
        GITHUB_DOMAIN = "github-api.mcvl-engineering.com/vocalink-portal"
        GITHUB_URL = "https://${GITHUB_DOMAIN}/${PROJECT_NAME}"
        ARTIFACTORY_CREDENTIALS = "artifactory-credentials"
        GITHUB_CREDENTIALS = "tech-user"
    }

    stages {
        stage("Compile") {
            steps {
                sh "./mvnw -B compile"
            }
        }
        stage("Test") {
            steps {
                sh "./mvnw -B test"
            }
        }
        stage("Package WAR") {
            steps {
                sh "./mvnw clean package"
            }
        }
        stage("Publish docker staging") {
            when {
                not {
                    branch "master"
                }
            }
            steps {
                script {
                    def gitCommit = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
                    def gitTag = "${BUILD_NUMBER}.${BRANCH_NAME}.${gitCommit}"
                    def imageName = "cp-portal-docker-staging/${PROJECT_NAME}:${gitTag}"

                    docker.withRegistry(env.ARTIFACTORY_URL, env.ARTIFACTORY_CREDENTIALS) {
                        def image = docker.build(imageName)
                        image.push()
                    }

                    sh "docker rmi ${imageName}"
                }
            }
        }
        stage("Prepare release") {
            environment {
                PROJECT_VERSION = readMavenPom().getVersion()
                RELEASE_VERSION = "${PROJECT_VERSION}-${BUILD_NUMBER}"
            }
            when {
                branch "master"
            }
            stages {
                stage("Set version") {
                    steps {
                        script {
                            sh "./mvnw versions:set -DnewVersion=${RELEASE_VERSION}"
                        }
                    }
                }
                stage("Publish artifact") {
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
                                customBuildName: "cp-api-management-test"
                        )
                        rtMavenResolver(
                                id: "MAVEN_RESOLVER",
                                serverId: "ARTIFACTORY_SERVER",
                                snapshotRepo: "cp-portal-group",
                                releaseRepo: "cp-portal-group",
                        )
                        rtMavenRun(
                                tool: "MAVEN_TOOL", // Tool name from Jenkins configuration
                                pom: "pom.xml",
                                goals: "clean install -U",
                                deployerId: "MAVEN_DEPLOYER",
                                resolverId: "MAVEN_RESOLVER"
                        )
                    }
                }
                stage("Release") {
                    steps {
                        script {
                            withCredentials([usernamePassword(credentialsId: env.GITHUB_CREDENTIALS, passwordVariable: "GITHUB_PASSWORD", usernameVariable: "GITHUB_USERNAME")]) {
                                sh "git tag ${RELEASE_VERSION} || true"
                                sh "git push https://${GITHUB_USERNAME}:${GITHUB_PASSWORD}@${GITHUB_DOMAIN}/${PROJECT_NAME}.git ${RELEASE_VERSION}"
                            }
                        }
                    }
                }
                stage("Publish docker release") {
                    when {
                        branch "master"
                    }
                    steps {
                        script {
                            def gitCommit = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
                            def gitTag = "${BUILD_NUMBER}.${BRANCH_NAME}.${gitCommit}"
                            def imageName = "cp-portal-docker-release/${PROJECT_NAME}:${gitTag}"

                            docker.withRegistry(env.ARTIFACTORY_URL, env.ARTIFACTORY_CREDENTIALS) {
                                def image = docker.build(imageName)
                                image.push()
                            }

                            sh "docker rmi ${imageName}"
                        }
                    }
                }

                stage("Deploy preprod") {
                    when {
                        branch "master"
                    }
                    steps {
                        script {
                            def gitCommit = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
                            def gitTag = "${BUILD_NUMBER}.master.${gitCommit}"

                            echo "Deploying ${gitTag} to ${PREPROD_IP}"

                            withCredentials([usernamePassword(credentialsId: env.ARTIFACTORY_CREDENTIALS, passwordVariable: "ARTIFACTORY_PASSWORD", usernameVariable: "ARTIFACTORY_USERNAME")]) {
                                sshagent(credentials: ["deployer"]) {
                                    sh "ssh -o StrictHostKeyChecking=no deployer@${PREPROD_IP} docker login -u ${ARTIFACTORY_USERNAME} -p ${ARTIFACTORY_PASSWORD} ${ARTIFACTORY_URL}"
                                    sh "ssh -o StrictHostKeyChecking=no deployer@${PREPROD_IP} docker rm -f ${PROJECT_NAME} || true"
                                    sh "ssh -o StrictHostKeyChecking=no deployer@${PREPROD_IP} docker run --network host -d -p 8080:8080 --name ${PROJECT_NAME} ${env.ARTIFACTORY_DOMAIN}/cp-portal-docker-release/${PROJECT_NAME}:${gitTag}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}

