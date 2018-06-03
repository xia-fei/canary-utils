## 系统介绍
定位于为系统某些复杂且通用的场景定义公用工具包  
例如test，mock，日志，异常，调用链，等工具jar包    
会随着项目需要持续的扩展
<br>
<br>
model分三部分  
1.**wing-insight** 依赖 insight上下文  
2.**wing-core** 核心算法代码不依赖insight上线文  
3.**wing-facade** 需要facade 来引入，以使用@mockValue
<br>    
<br>
此方法是生成测试RO的入口,之所以用Type作为参数是为了支持List泛型结构  
`com.qccr.saas.wing.core.mock.MockData#mock(Type)`




## mock测试使用指南  
pom引用
``` xml
<groupId>com.qccr.saas.wing</groupId>
<artifactId>wing-insight</artifactId>
<version>1.0.0</version>
```

  

spring.xml  添加如下两段

``` xml
<aop:aspectj-autoproxy proxy-target-class="true"/> 
<bean class="com.qccr.saas.wing.insight.MockAspect"/>
```

第一步：  
在facade方法实现类 添加  
`@Mock`注解  


第二步： 不需要可跳过
如果需要指定测试数据，在RO类上字段上添加  
`@MockValue({"测试数据1","测试数据2"})`


完成

效果测试
请运行junit-test 
`com.qccr.saas.wing.insight.test.MockAspectTest`  
或者mvn test看效果


#### 部分工具开发指南
`com.qccr.saas.wing.core.util.ThreadLocalUtils`  
线程本地变量,作用于当前线上下文传值  
例如 
controller层 `ThreadLocalUtils.set("userId",request.getParameter("userId"));`  
dao层 `ThreadLocalUtils.get("userId");`