package com.huju;

import com.huju.cache.dao.EmployeeMapper;
import com.huju.cache.entities.Employee;
import com.huju.elastic.entitis.Article;
import com.huju.rabbitmq.entities.Book;
import com.huju.repository.BookRepository;
import com.huju.repository.entities.Book02;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot002ApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    JestClient jestClient;
    @Autowired
    JavaMailSenderImpl mailSender;

    @Test
    public void contextLoads() {
        Employee empById = employeeMapper.getEmpById(1);
        System.out.println(empById);
    }

    @Test
    public void redisTest() {
        Employee empById = employeeMapper.getEmpById(1);
        redisTemplate.opsForValue().set("emp", empById);

        Object o = redisTemplate.opsForValue().get("emp::2");
        System.out.println(o);
    }

    // ***************************** RabbitMq ********************************

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 单播 (点对点)
     */
    @Test
    public void send() {

        /**
         * Message需要自己构造一个;定义消息体内容和消息头
         *
         * exchage 交换器
         * routeKey 队列key
         * message  消息对象
         *
         * rabbitTemplate.send(exchage,routeKey,message);
         */

        // 常用的是这种.只需要传入要发送的对象,自动序列化发送给rabbitmq,object默认当成消息体
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "嗨!你好,这是第一个消息!");
        map.put("data", Arrays.asList("你好呀!", 666888, true));

        // 对象被默认序列化(JDK机制)以后发送出去
        rabbitTemplate.convertAndSend("exchange.direct", "test.news", new Book("红楼梦", "曹雪芹"));
    }

    /**
     * 接收消息
     */
    @Test
    public void receive() {
        // 该方法是接收并转换,参数是取哪个队列里的
        Object o = rabbitTemplate.receiveAndConvert("test.news");
        System.out.println("********* 接收的对象类型是:" + o.getClass());
        System.out.println("********* 接收的数据是:" + o);
    }

    /**
     * 广播
     */
    @Test
    public void sendMsg() {
        // exchange.fanout是广播交换器,所有不用指定消息队列
        rabbitTemplate.convertAndSend("exchange.fanout", "", new Book("三国演义", "罗贯中"));

    }

    /**
     * 1.测试用代码来创建交换器
     */
    @Test
    public void createExchange() {
        // 创建一个交换器,以Direct类型的为例,可以传入交换器名称.是否序列化,是否自动删除
        amqpAdmin.declareExchange(new DirectExchange("amqpAdmin.exchange"));
        System.out.println("交换器创建完成!");
    }

    /**
     * 2.测试用代码来创建队列
     */
    @Test
    public void createQueue() {
        // 创建一个队列,可以传入队列名称.是否序列化...
        amqpAdmin.declareQueue(new Queue("amqpAdmin.queue", true));
        System.out.println("队列创建完成!");
    }

    /**
     * 3.测试用代码来创建绑定规则
     */
    @Test
    public void createBinding() {
        /**
         * 创建一个绑定规则,可以传入destination(目的地),destinationType(目的地类型,有两种:QUEUE(队列)和EXCHANGE(交换器)),
         * exchange(交换器的名字).routingKey(路由键),arguments(参数头信息)
         */
        amqpAdmin.declareBinding(new Binding("amqpAdmin.queue", Binding.DestinationType.QUEUE, "amqpAdmin.exchange", "amqp.haha", null));
        System.out.println("创建绑定规则完成!");
    }

    /**
     * 1.测试保存一个文档
     * 浏览器输入 http://192.168.113.128:9200/test001/news/1 就可以检索到这个文档了
     */
    @Test
    public void saveElasticSearch() {
        // 1.给ES中索引(保存)一个文档
        Article article = new Article();
        article.setId(1);
        article.setTitle("测试保存的标题");
        article.setAuthor("德玛西亚");
        article.setContent("Hello World");

        // 2.构建一个索引功能  传入对象,索引,类型
        Index index = new Index.Builder(article).index("test001").type("news").build();

        try {
            // 3.执行
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 2.测试搜索(全文检索)
     */
    @Test
    public void serch() {
        // 编写搜索表达式
        String json = "{\n" +
                "    \"query\" : {\n" +
                "        \"match\" : {\n" +
                "            \"content\" : \"hello\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        // 构建搜索功能 传入指定的索引和类型
        Search build = new Search.Builder(json).addIndex("test001").addType("news").build();

        try {
            SearchResult execute = jestClient.execute(build);
            // 会返回很多信息.这里就直接打印json字符串
            System.out.println(execute.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试第二种方法保存文档
     */
    public void save02() {
        Book02 book02 = new Book02();

        // bookRepository.save(book02);

    }

    /**
     * 测试发送一个普通文本邮件
     */
    @Test
    public void senMail() {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置主题
        simpleMailMessage.setSubject("邮件主题是: 通知,今晚出去玩!");
        // 设置内容
        simpleMailMessage.setText("邮件内容是:记得快点出来啊");
        // 设置发送给谁
        simpleMailMessage.setTo("13886338732@163.com");
        // 设置密送人
        simpleMailMessage.setBcc("ju.hu@pds-inc.com.cn");
        // 设置发送人
        simpleMailMessage.setFrom("839817187@qq.com");

        mailSender.send(simpleMailMessage);

    }

    /**
     * 测试发送一个复杂邮件
     */
    @Test
    public void senMail02() throws MessagingException {

        // 1.创建一个复杂的消息邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // 如果需要发送文件或者传输html等,就需要进行编码(传入true)
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        // 设置主题
        mimeMessageHelper.setSubject("邮件主题是: 通知,今晚出去玩!");
        // 设置内容,如果需要以html样式展示,第二个参数需要传入true
        mimeMessageHelper.setText("<b style='color:red'>邮件内容是:记得快点出来啊</b>", true);
        // 设置发送给谁
        mimeMessageHelper.setTo("13886338732@163.com");
        // 设置密送人
        mimeMessageHelper.setBcc("ju.hu@pds-inc.com.cn");
        // 设置发送人
        mimeMessageHelper.setFrom("839817187@qq.com");

        // 上传文件
        mimeMessageHelper.addAttachment("1.jpg", new File("D:\\高清壁纸\\321.jpg"));
        mimeMessageHelper.addAttachment("2.jpg", new File("D:\\高清壁纸\\ChMkJ1cpupOIFUUyAA4rlpnlufAAARBAQLlCv4ADiuu792.jpg"));

        mailSender.send(mimeMessage);

    }

}
