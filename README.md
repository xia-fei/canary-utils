## 系统介绍
saas系统公用test，mock，日志，异常，调用链，等工具jar包    
<br>
<br>
model分三部分  
1.wing-insight 依赖 insight上下文  
2.wing-core 核心算法代码不依赖insight上线文  
3.wing-facade 需要facade 来引入，以使用@mockValue
<br>    
<br>
此方法是生成测试RO的入口,之所以用Type作为参数是为了支持List泛型结构  
com.qccr.saas.wing.core.mock.MockData#mock(Type)




##使用指南  
pom引用

&lt;groupId&gt;com.qccr.saas.wing&lt;/groupId&gt;  
&lt;artifactId>wing-insight&lt;/artifactId&gt;  
&lt;version&gt;1.0.0&lt;/version&gt;
  

spring.xml  添加如下两段

&lt;aop:aspectj-autoproxy proxy-target-class="true"/&gt;    
&lt;bean class="com.qccr.saas.wing.insight.MockAspect"/&gt;

第一步：  
在facade方法实现类 添加  
@Mock注解  


第二步： 不需要可跳过
如果需要指定测试数据，在RO类上字段上添加  
@MockValue({"测试数据1","测试数据2"}) 


完成

