FROM maprtech/pacc:6.0.0_4.0.0_centos7

# GitHub credentials for cloning MapR DB JSON Grafana Plugin
# ARG username
# ARG password

# Angular Web Client
EXPOSE 4200

# Play Web App
EXPOSE 9000

# Create a directory for MapR Application and copy the Application
RUN mkdir -p /usr/share/mapr-apps/mapr-smart-home
COPY ./event-generator /usr/share/mapr-apps/mapr-smart-home/event-generator
COPY ./event-processor /usr/share/mapr-apps/mapr-smart-home/event-processor
COPY ./event-sink-mapr-db-json /usr/share/mapr-apps/mapr-smart-home/event-sink-mapr-db-json
COPY ./event-sink-open-tsdb /usr/share/mapr-apps/mapr-smart-home/event-sink-open-tsdb
COPY ./web-client /usr/share/mapr-apps/mapr-smart-home/web-client
COPY ./webapp /usr/share/mapr-apps/mapr-smart-home/webapp

COPY ./bin/run.sh /usr/share/mapr-apps/mapr-smart-home/run.sh
RUN chmod +x /usr/share/mapr-apps/mapr-smart-home/run.sh

# Install prerequisites
RUN curl https://bintray.com/sbt/rpm/rpm | sudo tee /etc/yum.repos.d/bintray-sbt-rpm.repo
RUN sudo yum install -y sbt

# Install npm
RUN sudo yum install -y epel-release
RUN curl --silent --location https://rpm.nodesource.com/setup_8.x | sudo bash -
RUN sudo yum install -y nodejs gcc-c++ make git

# Install Maven
# ENV MAVEN_VERSION 3.3.9

# RUN mkdir -p /usr/share/maven \
#  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
#    | tar -xzC /usr/share/maven --strip-components=1 \
#  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# ENV MAVEN_HOME /usr/share/maven

# Install web-client node modules
RUN rm -Rf /usr/share/mapr-apps/mapr-smart-home/web-client/node_modules
WORKDIR /usr/share/mapr-apps/mapr-smart-home/web-client
RUN npm install
RUN npm install forever -g

WORKDIR /usr/share/mapr-apps/mapr-smart-home/event-generator
RUN sbt assembly

WORKDIR /usr/share/mapr-apps/mapr-smart-home/event-processor
RUN sbt assembly

WORKDIR /usr/share/mapr-apps/mapr-smart-home/event-sink-mapr-db-json
RUN sbt assembly

WORKDIR /usr/share/mapr-apps/mapr-smart-home/event-sink-open-tsdb
RUN sbt assembly

WORKDIR /usr/share/mapr-apps/mapr-smart-home/webapp
RUN sbt 'set test in assembly := {}' clean assembly

# TODO Install Grafana
# RUN sudo yum install -y mapr-grafana

# Install MapR-DB JSON Grafana Plugin
# WORKDIR /usr/share/mapr-apps/mapr-smart-home/
# RUN git clone https://$username:$password@github.com/mapr-demos/mapr-db-json-grafana-plugin.git

# WORKDIR /usr/share/mapr-apps/mapr-smart-home/mapr-db-json-grafana-plugin
# RUN mvn clean package -DskipTests

CMD ["/usr/share/mapr-apps/mapr-smart-home/run.sh"]
