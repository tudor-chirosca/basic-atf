@Library('Shared@shared') _

pipeline {
    agent any
    
    tools {
        jdk 'JAVA_8U202'    
        nodejs 'NODEJS_12_21'
        maven 'MAVEN_3_6_3'
    }

    environment {
        RELEASE_BRANCH = "master"
        // when RELEASE_BRANCH != master, update sonar projet key to avoid overwriting current sonar project
        repoType = "staging"
    }

    options {
        disableConcurrentBuilds()
    }
    
    stages {
        stage('Set vars') {
            steps {
                script {
                    setVars()
                }
            }
        }
        
        stage("Build, Test, Sonar, Release & Publish ->") {
            when { expression { skipCondition != 'true' } }
            stages {
                // Clean jenkins workspace and checkout
                stage("Clean") {
                    steps {
                        script {
                            if (BRANCH == RELEASE_BRANCH) {
                                setGithubStatus("pending")
                            }
                            cleanWorkspace()
                            checkout scm
                        }
                    }
                }
                
                stage("Compile") {
                    steps {
                        runMaven(goal: "-B -U clean compile")
                    }
                }
                
                stage("Unit Tests") {
                    steps {
                        runMaven(goal: "-B test -Dspring.profiles.active=test")
                    }
                }
                
                // commenting out as no integration tests as of now
                // stage("Integration tests") {
                //     steps {
                //         runMaven(goal: "-B integration-test -U")
                //     }
                // }
               
                stage("Test DB migrations") {
                    when { not { branch "${RELEASE_BRANCH}" }}
                    parallel {
                        stage("Test DB migration P27") {
                            steps{
                                runDBMigrations(goal: 'PR')
                            }
                        }
                        stage("Test DB migration SAMA") {
                            steps {
                                runDBMigrations(goal: 'PR', scheme: "SAMA")
                            }

                        }
                    }
                    
                }
                
                stage("Run Sonar") {
                    when { branch "${RELEASE_BRANCH}" }
                    steps {
                        withSonarQubeEnv('cp-sonar') {
                            runMaven(goal: "sonar:sonar -Dsonar.projectKey=cp-international-suite-service")
                        }
                    }
                }
                stage('Create release') {
                    when { branch "${RELEASE_BRANCH}" }
                    steps {
                        script {
                            sh "npm i @semantic-release/git @semantic-release/changelog @conveyal/maven-semantic-release @semantic-release/commit-analyzer"
                            createRelease(releasePlugin: 'semantic-release', releaseArgs: "--skip-maven-deploy")
                        }
                    }
                }

                stage("Package") {
                    parallel {
                        stage("Create P27 package") {
                            steps{
                                runMaven(goal: "-B package -Dscheme=p27 -Dmaven.test.skip=true")
                            }
                        }
                        stage("Create DB SAMA package") {
                            steps {
                                dir("database") {
                                    runMaven(goal: "-B package -Dscheme=sama -Dmaven.test.skip=true")
                                }
                            }
                        }
                    }
                }

                stage("Publish artifact") {
                    when {
                        allOf {
                            expression { currentGitCommitHash != gitCommitPr } 
                            branch "${RELEASE_BRANCH}"
                        }
                    }
                    steps {
                        println("New commit hash appeared ${currentGitCommitHash} insted of ${gitCommitPr}. Publishing artifact...")
                        runMaven(goal: "-B deploy -Dmaven.test.skip=true")
                    }
                }
            }//stages
        }//stage

        stage('Publish docker and deploy ->') {
            when {
                anyOf {
                    // Always build docker container for branches
                    expression { skipCondition != 'true' && env.BRANCH != env.RELEASE_BRANCH }
                    // Build docker container for master branch only if there are changes
                    expression { skipCondition != 'true' && env.BRANCH == env.RELEASE_BRANCH && env.currentGitCommitHash != env.gitCommitPr }
                }
            }
            stages {
                stage("Publish docker image") {
                    steps {
                        script {
                            if (BRANCH == RELEASE_BRANCH) {
                                repoType = "release"
                            }
                            publishDockerImage(containerName: projectName, repoType: repoType)
                        }
                    }
                }
                stage("Migrate DEV DB") {
                    when { branch "${RELEASE_BRANCH}" }
                    parallel {
                        stage("Migrate DEV DB P27") {
                            steps{
                                runDBMigrations(goal: 'RELEASE', nameOfEnv: "DEV")
                            }
                        }
                        stage("Migrate DEV_SAMA DB") {
                            steps {
                                runDBMigrations(goal: 'RELEASE', nameOfEnv: "DEV_SAMA", scheme: "SAMA")
                            }

                        }
                    }
                }       
                
                
                stage("Deploy to DEV envs") {
                    when { branch "${RELEASE_BRANCH}" }
                    environment {
                        envVars = "-e BPS_CONFIG.BASE_URLS.MOCK=http://positions-mock-server:8080/positions-mock-server -e BPS.BASE_URLS.MOCK=http://positions-mock-server:8080/positions-mock-server -e SPRING_PROFILES_ACTIVE=preprod -e JAVA_OPTS='-Xmx2g'"
                        dockerArgs = "--network cpp-network -d -p 8080:8080 -v /root/tomcat/context.xml:/usr/local/tomcat/conf/context.xml ${envVars}"
                    }
                    parallel {
                        stage("Deply to P27 DEV") {
                            steps {
                                script {
                                    echo "Deploying ${env.gitTag} to ${DEV_IP}"
                                    
                                    deployContainer(
                                            containerName: projectName,
                                            deployHost: DEV_IP,
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
                                        nameOfEnv: 'DEV', 
                                        packageName: "${env.gitTag}-P27"
                                    ) {}
                                }
                            }
                        }

                        stage("Deply to SAMA DEV") {
                            steps {
                                script {
                                    echo "Deploying ${env.gitTag} to ${DEV_SAMA_IP}"
                                    
                                    deployContainer(
                                            containerName: projectName,
                                            deployHost: DEV_SAMA_IP,
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
                                        nameOfEnv: 'DEV_SAMA', 
                                        packageName: "${env.gitTag}-SAMA"
                                    ) {}
                                }
                            }
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
