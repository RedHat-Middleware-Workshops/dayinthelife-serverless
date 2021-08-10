FROM registry.redhat.io/codeready-workspaces/plugin-java11-rhel8:2.10-4 AS kafkacat
USER root
RUN dnf -y install gcc which gcc-c++ wget make git cmake
ENV KAFKACAT_VERSION=1.6.0
RUN dnf -y install cyrus-sasl-devel libcurl-devel
RUN cd /tmp && git clone https://github.com/edenhill/kafkacat -b $KAFKACAT_VERSION --single-branch && \
    cd kafkacat && \
    ./bootstrap.sh

FROM registry.redhat.io/codeready-workspaces/plugin-java11-rhel8:2.10-4

ENV CAMELK_VERSION=1.5.0
ENV SKUPPER_VERSION=0.7.0
ENV RHOAS_VERSION=0.26.0
ENV TKN_VERSION=0.15.0
ENV KN_VERSION=0.19.1

USER root

# Install skupper
RUN wget https://github.com/skupperproject/skupper/releases/download/${SKUPPER_VERSION}/skupper-cli-${SKUPPER_VERSION}-linux-amd64.tgz \
    -O - | tar -xz -C /usr/local/bin/

# Install kamel
RUN wget https://github.com/apache/camel-k/releases/download/v${CAMELK_VERSION}/camel-k-client-${CAMELK_VERSION}-linux-64bit.tar.gz \
    -O - | tar -xz -C /usr/local/bin/ ./kamel

# Install rhoas
RUN wget https://github.com/redhat-developer/app-services-cli/releases/download/v${RHOAS_VERSION}/rhoas_${RHOAS_VERSION}_linux_amd64.tar.gz \
    -O - | tar -xz --strip 2 -C /usr/local/bin rhoas_${RHOAS_VERSION}_linux_amd64/bin/rhoas

# Install Knative
RUN wget https://mirror.openshift.com/pub/openshift-v4/clients/serverless/${KN_VERSION}/kn-linux-amd64-${KN_VERSION}.tar.gz \
    -O - | tar -xz -C /usr/local/bin ./kn

# Install Tekton
RUN wget https://mirror.openshift.com/pub/openshift-v4/clients/pipeline/${TKN_VERSION}/tkn-linux-amd64-${TKN_VERSION}.tar.gz \
    -O - | tar -xz -C /usr/local/bin

# Install node
RUN dnf install -y nodejs && \
    dnf install -y jq && \
    dnf clean all

# Kafkacat from build
COPY --from=kafkacat /tmp/kafkacat/kafkacat /usr/local/bin/kafkacat

USER jboss

WORKDIR /projects
CMD tail -f /dev/null