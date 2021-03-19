@Library('Shared@shared') _

pipeline {
    agent any

    environment {
        RELEASE_BRANCH = "master"
        // when RELEASE_BRANCH != master, update sonar projet key to avoid overwriting current sonar project
        repoType = "staging"
    }

    options {
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    setVars()
                }
            }
        }

        stage("Clean workspace and Prepare params") {
            when { expression { skipCondition != 'true' } }
            steps {
                script {
                    if (BRANCH == RELEASE_BRANCH) {
                        setGithubStatus("pending")
                    }
                }
                cleanWorkspace()
                checkout scm
            }
        }
        stage("Build and publish artifact") {
            when { expression { skipCondition != 'true' } }
            agent {
                dockerfile {
                    filename 'Dockerfile-ci'
                    args "-v /jenkins-agent:/jenkins-agent -v $HOME/.m2:/home/jenkins-agent/.m2:z"
                    additionalBuildArgs '--build-arg USER_ID=1001 --build-arg GROUP_ID=1001'
                }
            }
            stages {
                stage("Compile") {
                    steps {
                        runMaven(goal: "-B -U clean compile")
                    }
                }
                stage("Unit Tests") {
                    steps {
                        runMaven(goal: "-B test")
                    }
                }
                stage("Run sonar for unit") {
                    when { branch "${RELEASE_BRANCH}" }
                    steps {
                        withSonarQubeEnv('cp-sonar') {
                            runMaven(goal: "sonar:sonar -Dsonar.projectKey=cp-international-suite-service")
                        }
                    }

                }
                stage("Package") {
                    steps {
                        withSonarQubeEnv('cp-sonar') {
                            runMaven(goal: "-B package -Dmaven.test.skip=true")
                        }
                    }

                }

                // commenting out as no integration tests as of now
                // stage("Integration tests") {
                //     steps {
                //         runMaven(goal: "-B integration-test -U")
                //     }
                // }

                stage("Prepare release") {
                    when { branch "${RELEASE_BRANCH}" }
                    stages {
                        stage('Create release') {
                            steps {
                                script {
                                    sh "npm i @semantic-release/git @semantic-release/changelog @conveyal/maven-semantic-release @semantic-release/commit-analyzer"
                                    createRelease(releasePlugin: 'semantic-release', releaseArgs: "--skip-maven-deploy")
                                }
                            }
                        }
                        stage("Publish artifact") {
                            when { expression { currentGitCommitHash != gitCommitPr } }
                            steps {
                                println("New commit hash appeared ${currentGitCommitHash} insted of ${gitCommitPr}. Publishing artifact...")
                                publishArtifact(goal: "clean package -U")
                            }
                        }
                    }//stages
                }//stage
            }//stages
        }//stage
        stage('Build and deploy docker image') {
            agent any
            when {
                anyOf {
                    // Always build docker container for branches
                    expression { skipCondition != 'true' && env.BRANCH != env.RELEASE_BRANCH }
                    // Build docker container for master branch only if there are changes
                    expression { skipCondition != 'true' && env.BRANCH == env.RELEASE_BRANCH && env.currentGitCommitHash != env.gitCommitPr }
                }
            }
            stages {
                stage("Publish docker") {
                    steps {
                        script {
                            if (BRANCH == RELEASE_BRANCH) {
                                repoType = "release"
                            }
                            publishDockerImage(containerName: projectName, repoType: repoType)
                        }
                    }
                }
                stage("Deploy preprod") {
                    when { branch "${RELEASE_BRANCH}" }
                    steps {
                        script {
                            echo "Deploying ${env.gitTag} to ${PREPROD_IP}"
                            def envVars = "-e BPS_CONFIG.BASE_URLS.MOCK=http://positions-mock-server:8080/positions-mock-server -e BPS.BASE_URLS.MOCK=http://positions-mock-server:8080/positions-mock-server -e SPRING_PROFILES_ACTIVE=preprod -e JAVA_OPTS='-Xmx2g'"
                            def dockerArgs = "--network cpp-network -d -p 8080:8080 -v /root/tomcat/context.xml:/usr/local/tomcat/conf/context.xml ${envVars}"
                            
                            deployContainer(
                                    containerName: projectName,
                                    deployHost: PREPROD_IP,
                                    repoType: repoType,
                                    dockerArgs: dockerArgs,
                                    imageTag: env.gitTag
                            )
                            
                            environmentDashboard (
                                addColumns: false, 
                                buildJob: '', 
                                buildNumber: BUILD_NUMBER, 
                                componentName: projectName, 
                                data: [], 
                                nameOfEnv: 'PREPROD', 
                                packageName: env.gitTag
                            ) {}
                        }
                    }
                }
            }//stages
        }//stage
    }//stages
    post {
        always {
            script {
                setGithubStatus("success")
            }
        }
    }

}//pipeline
