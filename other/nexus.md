- 问题：ERROR: max file descriptors [4096] likely too low, increase to at least [65536]....
- http://192.168.71.128:8081/
```
vi /etc/security/limits.conf
* hard nofile 65536
* soft nofile 65536
vi /etc/sysctl.conf
vm.max_map_count=655360
reboot
```