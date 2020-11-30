# This image installs the -headless package of OpenJDK
# and so is missing many of the UI-related Java libraries
# and some common packages contained in the default tag
FROM tomcat:8
MAINTAINER Cool kids

ADD ./target/international-suite-service-*.war /usr/local/tomcat/webapps/international-suite-service.war

EXPOSE 8080

HEALTHCHECK --interval=5s --timeout=3s --start-period=5s \
  CMD exit 0
