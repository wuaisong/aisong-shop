# docker环境

1443
9018
7001
8001
15537

7081

# 镜像打包

docker pull lznexus.luxsan-ict.com:8543/proxy/wuaisong/mydocker:front
docker tag lznexus.luxsan-ict.com:8543/proxy/wuaisong/mydocker:front 10.191.10.23:5000/wuaisong/mydocker:front
docker push 10.191.10.23:5000/wuaisong/mydocker:front

# 找镜像

kubectl describe pod apisix-dashboard-6f9995f49f-8xs4b -n ingress-apisix
kubectl describe pod apisix-dashboard-6f9995f49f-8xs4b -n kube-system
kubectl delete pod apisix-dashboard-6f9995f49f-8xs4b -n ingress-apisix
10.191.10.23:32364

# 镜像仓库
docker network create -d bridge mynet
docker network ls
docker inspect mynet
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
curl -i -X GET "http://127.0.0.1:32364/whoami" -H "Host: ai.com"
curl -X GET '10.43.196.255:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=2'
curl -X GET '127.0.0.1:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=2'

# 前端打包本地镜像并推送

docker build -t wuaisong/mydocker:front .
docker push wuaisong/mydocker:front
docker run -p 8777:80 -d --privileged=true --name mydocker --restart always wuaisong/mydocker:front

##### docker run -d --privileged=true --name nss-web1.0 --restart always --net=host nss-web:1.0

docker tag wuaisong/mydocker:front 10.191.10.23:5000/wuaisong/mydocker:front
docker push 10.191.10.23:5000/wuaisong/mydocker:front

# 映射地址问题
yum install http://li.nux.ro/download/nux/misc/el7/x86_64//rinetd-0.62-9.el7.nux.x86_64.rpm
vi rinetd.conf
pkill rinetd
rinetd -c rinetd.conf

# 网络工具，TCP网络追踪

yum install bind-utils
nslookup www.baidu.com
nslookup -type=any www.baidu.com
dig www.baidu.com
traceroute -I gps.luxsan-ict.com
traceroute -I 10.42.221.95
ip route show

## 网络查看

cat /etc/resolv.conf
traceroute 10.42.221.95
ping 10.42.221.95
tracepath 10.42.221.95

## 常用命令

journalctl -f -u kubelet
kubectl get svc,ep -n kube-system | grep dns
kubectl get po -n kube-system -o wide | grep core
kubectl exec busybox-588c6c8cf4-scmmg -- cat /etc/resolv.conf
kubectl exec -it dnsutils /bin/sh -n kube-system
kubectl get cs # 查看集群状态
kubectl get nodes # 查看集群节点信息
kubectl get ns # 查看集群命名空间
kubectl get svc -n kube-system # 查看指定命名空间的服务
kubectl get pod <pod-name> -o wide # 查看Pod详细信息
kubectl get pod <pod-name> -o yaml # 以yaml格式查看Pod详细信息
kubectl get pods # 查看资源对象，查看所有Pod列表
kubectl get pods --namespace=test
kubectl get rc,service # 查看资源对象，查看rc和service列表
kubectl get pod,svc,ep --show-labels # 查看pod,svc,ep能及标签信息
kubectl get all --all-namespaces # 查看所有的命名空间
--回滚到上一个版本
kubectl rollout undo deployment/test --namespace=test
--回滚到指定版本
kubectl rollout undo deployment/test --to-revision=1 --namespace=test
--重启pod
kubectl rollout restart deployment {pod}  -n {namespace}
kubectl describe pod $mypod | grep 'Controlled By:'
kubectl describe pod whoami-668b48497f-vwhh5
kubectl delete replicaset metrics-server-644778ff4f -n kube-system
# Get all releases
helm ls --all-namespaces
# OR
helm ls -A
# Delete release
helm uninstall release_name -n release_namespace


crictl copy d6562d4e1c085:/usr/local/apisix/conf/config-default.yaml ./a.yaml
