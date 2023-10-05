# docker环境

1443-3306
9018-6379
7001-5672
8001-15672


c20987f18b13-mysql
7614ae9453d1-redis
f1a77cb119f4-rabbitmq

# 镜像打包

docker pull lznexus.luxsan-ict.com:8543/proxy/eipwork/kuboard-agent:v3
docker tag lznexus.luxsan-ict.com:8543/proxy/eipwork/kuboard-agent:v3 10.191.10.23:5000/eipwork/kuboard-agent:v3
docker push 10.191.10.23:5000/eipwork/kuboard-agent:v3

# 找镜像

kubectl describe  pod kuboard-agent-2-5ff688b957-hkcrq -n kuboard | grep image

# 镜像仓库

docker run -d -p 5100:5000 --restart=always --name registry lznexus.luxsan-ict.com:8543/proxy/library/registry:2

# 代理内容到容器外

kubectl port-forward --address 0.0.0.0 pod/kuboard-v3-6854dcdf96-tc484 15537:80 -n kuboard
























