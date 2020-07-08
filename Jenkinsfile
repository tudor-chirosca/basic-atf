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
        RELEASE_BRANCH = "master"
    }


    stages {
        stage("Build and publish artifact:") {
            agent {
                dockerfile {
                    filename 'Dockerfile-ci'
                    args "-u root -v /var/run/docker.sock:/var/run/docker.sock -v /jenkins-agent:/jenkins-agent -e JAVA_HOME='/usr/local/openjdk-8'"
                }
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
                stage("Prepare release: ") {
                    when {
                        allOf {
                            branch "${RELEASE_BRANCH}"
                            not {
                                changelog "^chore\\(release\\):.*"
                            }
                        }
                    }
                    stages {
                        stage('create release') {
                            steps {
                                script {
                                    withCredentials([usernamePassword(credentialsId: env.GITHUB_CREDENTIALS, passwordVariable: "GITHUB_PASSWORD", usernameVariable: "GITHUB_USERNAME")]) {
                                        sh "git config --global user.email jenkins-agent-cp-portal@mastercard.com"
                                        sh "git config --global user.name jenkins-agent-cp-portal"
                                        sh "git config --global push.followTags true"
                                        sh "npx standard-version"
                                        sh "git push https://${GITHUB_USERNAME}:${GITHUB_PASSWORD}@${GITHUB_DOMAIN}/${PROJECT_NAME}.git HEAD:${BRANCH_NAME}"
                                        env.WORKSPACE = pwd()
                                        env.RELEASE_VERSION = readFile "${env.WORKSPACE}/version.txt"
                                        echo "release generated: ${RELEASE_VERSION}"
                                    }
                                }
                            }
                        }
                        stage("publish artifact") {
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
                    }
                }
            }
        }
        stage('Build and deploy docker image') {
            agent any
            when {
                not {
                    changelog "^chore\\(release\\):.*"
                }
            }
            stages {
                stage("Publish docker staging") {
                    when {
                        not {
                            branch "${RELEASE_BRANCH}"
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
                stage("Publish docker release") {
                    when {
                        branch "${RELEASE_BRANCH}"
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

