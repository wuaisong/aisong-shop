FROM lznexus.luxsan-ict.com:8543/proxy/library/nginx:1.20
#FROM nginx:1.20

MAINTAINER beizhu

RUN rm /etc/nginx/conf.d/default.conf

ADD default.conf /etc/nginx/conf.d/

COPY dist/ /usr/share/nginx/html/front/
