#spring boot高级练习

###1.缓存
 * 快速体验缓存
 * 1.创建项目的时候勾选 cache
 * 2.开启基于注解 @EnableCaching
 * 2.标注缓存注解即可
 * 		@Cacheable		主要针对方法配置，能够根据方法的请求参数对其结果进行缓存
 * 		@CacheEvict		清空缓存
 * 		@CachePut		保证方法被调用，又希望结果被缓存。

###2.RabbitMQ
    @EnableRabbit + @RabbitListener(queues = {"test.news"}) 监听消息队列里的内容
 * 给RabbitMA自动配置的情况
 * 1.RabbitAutoConfiguration
 * 2.有自动配置了连接工厂 CachingConnectionFactory
 * 3.RabbitProperties 封装了 RabbitMQ的配置
 * 4.rabbitTemplate 给RabbitMQ发送和接受消息
 * 5.amqpAdmin : RabbitMQ系统管理功能组件,可以在代码里创建和删除Queue(消息队列),Exchange(交换器),Binding(绑定规则)
 
###3.elasticsearch
* SpringBoot默认支持两种技术来iheES交互
* 1.Jest客户端(默认不生效)
    *   需要导入jest的工具包(io.searchbox.client.JestClient)
* 2.SpringData ElasticSearch
    * Client节点inxi
    * ElasticsearchTemplate操作ES
    * 编写一个ElasticsearchRepository的子接口来操作ES
    
###4.security安全认证模块
* 1.引入SpringSecurity
* 2.编写SpringSecurity的配置