@Library('Shared@shared') _

pipeline {
    agent any

    environment {
        RELEASE_BRANCH = "master"
        repoType = "staging"
    }
    
    options {
        disableConcurrentBuilds()
    }
    
    stages {
        stage("Prepare Params") {
            steps {
                setVars()
            }
        }
        stage("Build and publish artifact:") {
            agent {
                dockerfile {
                    filename 'Dockerfile-ci'
                    args "-v /jenkins-agent:/jenkins-agent -v $HOME/.m2:/home/jenkins-agent/.m2:z"
                    additionalBuildArgs '--build-arg USER_ID=1001 --build-arg GROUP_ID=1001'
                }
            }
            when {
                not {
                    changelog "^chore\\(release\\):.*"
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
                stage("Integration tests") {
                    steps {
                        runMaven(goal: "-B integration-test -U")
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
                                    createRelease(releasePlugin: "standard-version")
                                    env.RELEASE_VERSION = readFile("${WORKSPACE}/version.txt")
                                    echo "Release generated: ${RELEASE_VERSION}"
  
                                 }
                            }
                        }
                        stage("publish artifact") {
                            steps {
                                publishArtifact(goal: "clean install -U -Drelease.version=${RELEASE_VERSION}")
                            }
                        }
                    }
                }//stage
            }//stages
        }//stage
        stage('Build and deploy docker image') {
            agent any
            when {
                not {
                    changelog "^chore\\(release\\):.*"
                }
            }
            stages {
                stage("Publish docker") {
                    steps {
                        script {
                            if (GIT_BRANCH == RELEASE_BRANCH) {
                                repoType = "release"
                            }
                            publishDockerImage(containerName: projectName, repoType: repoType)
                        }
                    }
                }
                stage("Deploy preprod") {
                    when {
                        branch "${RELEASE_BRANCH}"
                    }
                    steps {
                        script {
                            echo "Deploying ${env.gitTag} to ${PREPROD_IP}"
                            def envVars = "-e BPS_BASE_URL=http://positions-mock-server:8080/positions-mock-server -e SPRINT_PROFILES_ACTIVE=dev -e JAVA_OPTS='-Xmx2g'"
                            def dockerArgs = "--network cpp-network -d -p 8080:8080 ${envVars}"
                            deployContainer(
                                containerName: projectName, 
                                deployHost: PREPROD_IP, 
                                repoType: repoType, 
                                dockerArgs: dockerArgs, 
                                imageTag: env.gitTag
                            )
                        }
                    }
                }
            }
        }//stage
    }
    post {
        always {
            cleanWs()
        }
    }
}