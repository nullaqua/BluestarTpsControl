# BluestarTpsControl
一个能更改我的世界服务器tps的插件

使用说明:
- 关闭服务器
- 下载插件
- 将插件放入plugins文件夹
- 使用 ```java -javaagent:./plugins/BluestarTpsControl-1.0-jar-with-dependencies.jar -jar 你的服务器文件.jar```
- 使用```/settps 要设置的tps```来设置服务器的tps

支持的服务端:
- spigot(包括例如paper/purpur之类的延伸版本)

支持的版本:
- 1.18-1.18.2其他版本暂时未测试

权限:
- ```bluestartpscontrol.use```使用/settps指令
默认为仅后台有权限

具体更改内容:
- 可以修改最大tps(但是实际上多少得看服务器性能)
- 使用```/tps```查看tps时,大于20tps不会再用```*20.0```来表示,而是显示真实tps

工作原理:
20tps其实是写死在服务器里的。此插件分为两部分来达到更改tps的目的:
1. 通过```javaagent```"篡改"服务器原本代码,使其tps变为可更改的,并添加更改方法
2. 增加```/settps```指令,调用上一条所说的方法,修改tps

关于加载/卸载插件:
- 仅加载插件,但不使用```使用说明```中的方法启动服务器,插件无法正常工作
- 如果按说明启动服务器之后再热加载插件是可以正常使用的
- 使用```plugman```或是```/reload```卸载插件仅仅是删除了```/settps```指令,要真正去除对服务器的修改需要重启服务器