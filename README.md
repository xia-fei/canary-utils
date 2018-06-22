## 系统介绍
定位于为系统某些复杂且通用的场景定义公用工具包  
例如test，mock，日志，异常，调用链，等工具jar包    
会随着项目需要持续的扩展。
<br>
<br>
model分三部分  
1.**wing-insight** 依赖 insight上下文  
2.**wing-core** 核心算法代码不依赖insight上下文  
3.**wing-facade** 需要facade 来引入，以使用@mockValue
<br>    
<br>
此方法是生成测试RO的入口,之所以用Type作为参数是为了支持List泛型结构  
`MockData#mock(Type)`




## mock测试使用指南  
pom引用
``` xml
<groupId>com.wing</groupId>
<artifactId>wing-insight</artifactId>
<version>1.0.0</version>
```

  

spring.xml  添加如下两段

``` xml
<aop:aspectj-autoproxy proxy-target-class="true"/> 
<bean class="MockAspect"/>
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
`MockAspectTest`  
或者mvn test看效果


## 部分工具开发指南

 **`ThreadLocalUtils`**  
线程本地变量,作用于当前线上下文传值  
例如 
controller层 `ThreadLocalUtils.set("userId",request.getParameter("userId"));`  
dao层 `ThreadLocalUtils.get("userId")`


 **`com.wing.core.thread.HeavyThreadExecution`**  
处理频繁的更新的操作,只需要更新优先级最高的这种任务
eg:
``` java
       //定义任务优先级
        Comparator<InsertDBRunnable> comparator = new Comparator<InsertDBRunnable>() {
            @Override
            public int compare(InsertDBRunnable o1, InsertDBRunnable o2) {
                return Integer.compare(o1.getCount(), o2.getCount());
            }
        };
        //创建 慢任务处理器
        HeavyThreadExecution<InsertDBRunnable> execution = new HeavyThreadExecution<>(comparator);

        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(new Random().nextInt(50));
            } catch (InterruptedException ignored) {
            }
            System.out.println("当前用户访问人数:" + i);
            //模拟业务平凡插入
            execution.execute(new InsertDBRunnable(i));
        }
```
### console
``` text
当前用户访问人数:0
当前用户访问人数:1
当前用户访问人数:2
当前用户访问人数:3
当前用户访问人数:4
当前用户访问人数:5
当前用户访问人数:6
当前用户访问人数:7
当前用户访问人数:8
当前用户访问人数:9
当前用户访问人数:10
当前用户访问人数:11
当前用户访问人数:12
当前用户访问人数:13
当前人数插入DB:0
当前用户访问人数:14
当前用户访问人数:15
当前用户访问人数:16
当前用户访问人数:17
当前用户访问人数:18
当前用户访问人数:19
当前用户访问人数:20
当前用户访问人数:21
当前用户访问人数:22
当前用户访问人数:23
当前人数插入DB:13
当前用户访问人数:24
当前用户访问人数:25
当前用户访问人数:26
当前用户访问人数:27
当前用户访问人数:28
当前用户访问人数:29
当前人数插入DB:23
当前人数插入DB:29
线程池任务执行完成
```