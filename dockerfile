FROM tomcat:10.1-jdk21
RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/spring-framework-blog.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
CMD ["catalina.sh", "run"]

