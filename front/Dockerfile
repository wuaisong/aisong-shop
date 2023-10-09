FROM lznexus.luxsan-ict.com:8543/proxy/library/nginx

MAINTAINER beizhu

RUN rm /etc/nginx/conf.d/default.conf

ADD default.conf /etc/nginx/conf.d/

COPY dist/ /usr/share/nginx/html/
