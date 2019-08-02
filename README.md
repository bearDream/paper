## Paper Annotation Interface Invoker

[![Build Status](https://travis-ci.org/apache/dubbo.svg?branch=master)](https://travis-ci.org/apache/dubbo)

***

1. The usage of http annotation interface  

	> - 启用http声明式接口。  
	> - 在需要使用http请求的地方声明一个接口，并用注解声明
	> - 需要使用的时候将该接口注入，直接调用该接口的方法即可。
	> - EnablePaperHttp 的registra只负责将paperclient注解的类注册为Bean。  
	之后动态的扫描这些接口下的方法，根据注解以及参数的信息动态代理，注册为bean
	Notice:
	1. 若项目本身是SpringBoot项目，则仅需要在启动类加入@EnableHttp注解，并在需要调用的Service处注入声明的接口即可，  
	    注意这里的Service一定要由Spring管理的Bean，若该类不是由Spring管理，则会出现注入失败的错误。  
	2. 若项目本身只是Spring的项目，则新建一个config的类，将该类声明@Configuration并使用@Import把PaperHttpConfig导入即可完成配置，注意使用接口的类一定要由Spring管理，  
	    一般来说，发现注入失败都是由于调用的类不是spring管理的Bean，从而导致的。

2. 项目更新部署  
	> 更新km_version，运行mvn versions:set可将所有模块版本统一更新  
	clean install deploy即可部署到私仓中
	
3. blog: [博客地址](http://www.chiprincess.cn/2019/08/01/Paper-Annotation-Interface-Invoker-Design/index/#toc-heading-1)