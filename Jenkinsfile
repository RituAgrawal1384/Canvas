pipeline {
  agent {
    kubernetes {
      cloud "openshift"
      label "mypod"
      defaultContainer "jnlp"
      yamlFile "build-containers.yml"
    }
  }
  stages {
    stage('Checkout DevOps-Common') {
        steps {
            withCredentials([usernamePassword(credentialsId: 'svc_pca_devopscoe', usernameVariable: 'BITBUCKET_USER',  passwordVariable: 'BITBUCKET_PASSWORD')]) {
                dir('devops-common') {
                    git branch: "master",
                        url: "https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@code.pruconnect.net/scm/pcd/devops-common.git"
                }
            }
        }
    }

    stage('Initiate build') {
        steps {
            container('python') {
                withCredentials([usernamePassword(credentialsId: 'svc_pca_devopscoe', usernameVariable: 'BITBUCKET_USER',  passwordVariable: 'BITBUCKET_PASSWORD')]) {
                    sh '''
                        python --version
                        python -m pip install -r devops-common/requirements.txt
                        python devops-common/send_bitbucket_status.py -u ${BITBUCKET_USER} -p ${BITBUCKET_PASSWORD} -l "https://code.pruconnect.net" \
                        -s INPROGRESS -c ${GIT_COMMIT} -b ${BUILD_URL}
                    '''
                }
            }
        }
    }

    stage('Test, build JAR and deploy') {
        steps {
            container('maven') {
                withCredentials([usernamePassword(credentialsId: 'artifactory-SRVSGRHOCOEDEVSECOPS',
                    passwordVariable: 'ARTIFACTORY_PASSWORD',
                    usernameVariable: 'ARTIFACTORY_USERNAME')]) {
                    sh '''
                    mvn clean deploy \
                    -Dartifactory.user=$ARTIFACTORY_USERNAME \
                    -Dartifactory.pass=$ARTIFACTORY_PASSWORD \
                    -s settings.xml \
                    -P artifactory-external \
                    -Dsurefire.suiteXmlFiles=testng-java.xml \
                    -Dmaven.wagon.http.ssl.insecure=true
                    ls -alF target/
                    ls -alF target/surefire-reports/
                    '''
                }
            }

        }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/surefire-reports/**/*'
    }
    success {
        container('python') {
          withCredentials([usernamePassword(credentialsId: 'svc_pca_devopscoe', usernameVariable: 'BITBUCKET_USER',  passwordVariable: 'BITBUCKET_PASSWORD')]) {
                sh '''
                  python -m pip install -r devops-common/requirements.txt
                  python devops-common/send_bitbucket_status.py -u ${BITBUCKET_USER} -p ${BITBUCKET_PASSWORD} -l "https://code.pruconnect.net" \
                  -s SUCCESSFUL -c ${GIT_COMMIT} -b ${BUILD_URL}
                '''
          }
        }
    }
    failure {
        container('python') {
          withCredentials([usernamePassword(credentialsId: 'svc_pca_devopscoe', usernameVariable: 'BITBUCKET_USER',  passwordVariable: 'BITBUCKET_PASSWORD')]) {
                sh '''
                  python -m pip install -r devops-common/requirements.txt
                  python devops-common/send_bitbucket_status.py -u ${BITBUCKET_USER} -p ${BITBUCKET_PASSWORD} -l "https://code.pruconnect.net" \
                  -s FAILED -c ${GIT_COMMIT} -b ${BUILD_URL}
                '''
          }
        }

    }
  }
}
