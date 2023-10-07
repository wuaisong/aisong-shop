# docker环境

1443
9018
7001
8001
15537

# 镜像打包

docker pull lznexus.luxsan-ict.com:8543/proxy/apache/apisix-dashboard:3.0.0-alpine
docker tag lznexus.luxsan-ict.com:8543/proxy/apache/apisix-dashboard:3.0.0-alpine 10.191.10.23:5000/apache/apisix-dashboard:3.0.0-alpine
docker push 10.191.10.23:5000/apache/apisix-dashboard:3.0.0-alpine

# 找镜像

kubectl describe pod apisix-dashboard-6f9995f49f-8xs4b -n ingress-apisix
kubectl describe pod apisix-dashboard-6f9995f49f-8xs4b -n kube-system
kubectl delete pod apisix-dashboard-6f9995f49f-8xs4b -n ingress-apisix
10.191.10.23:32364

# 镜像仓库

docker run -d -p 8481:8481 -e spring.cloud.nacos.discovery.server-addr=192.168.8.200:8848 --net mynet wuaisong/mydocker:provider
docker run -d -p 8482:8482 -e spring.cloud.nacos.discovery.server-addr=192.168.8.200:8848 --net mynet wuaisong/mydocker:consumer

docker run -d -p 5000:5000 --restart=always --name registry lznexus.luxsan-ict.com:8543/proxy/library/registry:2
curl -XGET http://127.0.0.1:5000/v2/_catalog
curl -XGET http://127.0.0.1:5000/v2/wuaisong/mydocker/tags/list

# 代理内容到容器外

kubectl port-forward --address 0.0.0.0 pod/kuboard-v3-6854dcdf96-tc484 15537:80 -n kuboard

# 修改docker配置

service docker restart
/etc/docker/daemon.json
```
{ 
	"insecure-registries":[
		"192.168.0.110:5000",
		"localdockerreghost:5000"
	] 
}
```

# 配置Nacos

docker run -d --restart=unless-stopped --name nacos -p 8848:8848 -p 9848:9848 -e PREFER_HOST_MODE=127.0.0.1 -e MODE=standalone lznexus.luxsan-ict.com:8543/proxy/nacos/nacos-server:2.0.2





















