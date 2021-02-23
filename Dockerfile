FROM repo-api.mcvl-engineering.com/cp-portal-docker-release/tomcat/cpp-tomcat:8.5.57

ADD ./target/international-suite-service-*.war /usr/local/tomcat/webapps/international-suite-service.war

EXPOSE 8080

HEALTHCHECK --interval=5s --timeout=3s --start-period=5s \
  CMD exit 0
