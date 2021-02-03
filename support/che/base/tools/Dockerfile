FROM centos:8


RUN dnf update -y && \
dnf install -y wget && \
dnf install -y java-11-openjdk java-11-openjdk-devel && \
dnf install -y nodejs && \
dnf install -y maven && \
dnf clean all

# Define working directory.
ADD oc /usr/bin
ADD kamel /usr/bin

# Set environment variables.
ENV HOME /projects
RUN alternatives --set java /usr/lib/jvm/java-11-openjdk-11.0.9.11-3.el8_3.x86_64/bin/java

WORKDIR /projects

ADD entrypoint.sh /entrypoint.sh
#CMD "$@"
CMD /entrypoint.sh tail -f /dev/null
