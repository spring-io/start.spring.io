FROM ubuntu:focal-20220826

ADD setup.sh /setup.sh
RUN ./setup.sh

ENV JAVA_HOME /opt/openjdk
ENV PATH $JAVA_HOME/bin:$PATH

RUN apt-get update && \
    apt-get install -y wget && \
    wget https://github.com/k14s/ytt/releases/download/v0.27.2/ytt-linux-amd64 && \
	mv ytt-linux-amd64 /usr/local/bin/ytt && \
	chmod +x /usr/local/bin/ytt

RUN apt-get update && \
	apt-get install -y apt-transport-https gnupg2 && \
	curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add - && \
	echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | tee -a /etc/apt/sources.list.d/kubernetes.list && \
	apt-get update && \
	apt-get install -y kubectl

