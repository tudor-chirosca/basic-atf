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
        sonarProjectKey = "international-suite-service"
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
                    steps {
                        script {
                            def sonarProjectName = projectName
                            if ( BRANCH_NAME != RELEASE_BRANCH) {
                                sonarProjectKey += "-${BRANCH_NAME}"
                                sonarProjectName += " ${BRANCH_NAME}"
                            }
                            withSonarQubeEnv('cp-sonar') {
                                runMaven(goal: "sonar:sonar -Dsonar.projectKey=${sonarProjectKey} -Dsonar.projectName='${sonarProjectName}'")
                            }

                            def qg = waitForQualityGate()
                            println qg
                            if (qg.status == "ERROR") {
                                // error "Pipeline aborted due to quality gate status: ${qg.status}"
                            } else if (qg.status == "WARN") {
                                // currentBuild.result='UNSTABLE'
                                println "Pipeline unstable due to quality gate status: ${qg.status}"
                            }
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

                stage("Create package") {
                    steps{
                        runMaven(goal: "-B package -Dmaven.test.skip=true")
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
                        envVars = "-e BPS_CONFIG_BASE_URLS_MOCK=http://positions-mock-server:8080/positions-mock-server -e SPRING_PROFILES_ACTIVE=preprod -e JAVA_OPTS='-Xmx2g'"
                        dockerArgs = "--network cpp-network -d -p 8080:8080 -v /root/tomcat/context.xml:/usr/local/tomcat/conf/context.xml ${envVars}"
                        dockerArgsP27 = " -e BPS_CONFIG_SCHEME_CODE=P27-SEK ${dockerArgs}"
                        dockerArgsSAMA = " -e BPS_CONFIG_SCHEME_CODE=SAMA-SAR ${dockerArgs}"
                    }
                    parallel {
                        stage("Deploy to P27 DEV") {
                            steps {
                                script {
                                    echo "Deploying ${env.gitTag} to ${DEV_IP}"

                                    deployContainer(
                                            containerName: projectName,
                                            deployHost: DEV_IP,
                                            repoType: repoType,
                                            dockerArgs: dockerArgsP27,
                                            imageTag: env.gitTag                                            
                                    )

                                    environmentDashboard (
                                        addColumns: false, 
                                        buildJob: '', 
                                        buildNumber: BUILD_NUMBER, 
                                        componentName: 'DEV', 
                                        data: [], 
                                        nameOfEnv: projectName, 
                                        packageName: "${env.gitTag}-P27"
                                    ) {}
                                }
                            }
                        }

                        stage("Deploy to SAMA DEV") {
                            steps {
                                script {
                                    echo "Deploying ${env.gitTag} to ${DEV_SAMA_IP}"

                                    deployContainer(
                                            containerName: projectName,
                                            deployHost: DEV_SAMA_IP,
                                            repoType: repoType,
                                            dockerArgs: dockerArgsSAMA,
                                            imageTag: env.gitTag
                                    )

                                    environmentDashboard (
                                        addColumns: false, 
                                        buildJob: '', 
                                        buildNumber: BUILD_NUMBER, 
                                        componentName: 'DEV_SAMA', 
                                        data: [], 
                                        nameOfEnv: projectName, 
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
        success {
            script {
                if (BRANCH_NAME != RELEASE_BRANCH) {
                    cleanupSonarPr()
                }
            }
 
        }
    }

}//pipeline
