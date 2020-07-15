### HDARS说明
HDARS是基于Java SpringBoot框架开发，主要提供了从Archiver Appliance和HBase中按粒度检索历史数据的功能。

HDARS作为HLS-II数据查询系统得到一个后端，通过Apache Shiro与hlsiidb进行了人员管理和权限认证的整合。

HDARS部署在192.168.113.81的/opt/HLS-II-20-07-14目录下，该目录下还包含了源码HLS-II目录。

通过一下命令进行编译打包
>mvn clean install -Dmaven.test.skip=true




