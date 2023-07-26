package wu.ai.song.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wu.ai.song.api.entity.User;
import wu.ai.song.api.mapper.UserDao;
import wu.ai.song.api.redis.RedisUtil;
import wu.ai.song.api.service.Demo;
import wu.ai.song.api.utils.DoubleUtil;
import wu.ai.song.api.utils.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static java.util.Optional.ofNullable;

/**
 * @author wuyaming
 */
@RestController
@Slf4j
public class HelloController {

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Value("${app.build.time}")
    private String appBuildTime;

    @Autowired
    private UserDao userDao;

    @Autowired
    private Demo demo;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private Executor executor;


    @GetMapping("/hello/{name}")
    @Cacheable(cacheNames = {"User", "User1"}, unless = "#name > 1", key = "#root.methodName+'[' + #name + ']'")
    public String hello(@PathVariable("name") Integer name) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(name.toString().getBytes(StandardCharsets.UTF_8));
        byte[] digest = md5.digest();
        log.info("md5: {} : {}", name, digest);
        log.info("debug: hello~{}", springApplicationName);
        log.info("debug: hello~{}", appBuildTime);
        return "Hello World~";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new user");
        session.setMaxInactiveInterval(3600);
        log.info(session.getAttribute("userInfo").toString());
        session.removeAttribute("userInfo");
        log.info(ofNullable(session.getAttribute("userInfo")).orElse("null").toString());
        return "ok";
    }

    /**
     * 测试事务
     *
     * @param request
     * @see org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration 自动配置事务
     * @see org.springframework.transaction.annotation.EnableTransactionManagement 开启事务
     * @see org.springframework.context.annotation.AutoProxyRegistrar
     * @see org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration
     * @see org.springframework.aop.config.AopConfigUtils
     * @see org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator
     * @see org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator
     * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization(java.lang.Object, java.lang.String)
     * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     * @see com.baomidou.mybatisplus.core.override.MybatisMapperProxy#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     * @see org.mybatis.spring.SqlSessionTemplate
     * @see SqlSessionTemplate#SqlSessionTemplate(org.apache.ibatis.session.SqlSessionFactory, org.apache.ibatis.session.ExecutorType, org.springframework.dao.support.PersistenceExceptionTranslator)
     * @see SqlSessionUtils#getSqlSession(org.apache.ibatis.session.SqlSessionFactory, org.apache.ibatis.session.ExecutorType, org.springframework.dao.support.PersistenceExceptionTranslator)
     */

    @GetMapping("/testTranslate")
    @Transactional(rollbackFor = Exception.class)
    public void testTranslate(HttpServletRequest request) {
        doInsert();
    }

    private void doInsert() {
        User user = new User();
        user.setName("墨白君");
        user.setAge(25);
        user.setEmail("mobaijun8@163.com");
        // mybatis-plus会自动帮助我们生成主键ID
        int insert = userDao.insert(user);
        // 被影响的行数
        System.out.println("insert = " + insert);
        // ID会自动回填
        System.out.println("user = " + user);
    }


    @GetMapping("/testTransactionTemplate")
    public void testTransactionTemplate(HttpServletRequest request) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                doInsert();
                doInsert();
                doInsert();
                doInsert();
            }
        });
    }


    /**
     * 抢红包 拆红包 抢到不一定能拆到
     *
     * @param redPacketId
     * @return
     */
    @GetMapping("/startTwo")
    public void startTwo(@RequestParam("redPacketId") long redPacketId) {
        int skillNum = 100;
        /**
         * N个抢红包
         */
        final CountDownLatch latch = new CountDownLatch(skillNum);
        /**
         * 初始化红包数据，抢红包拦截
         */
        redisUtil.set(redPacketId + "-num", "1000");
        /**
         * 初始化红包金额，单位为分
         */
        redisUtil.set(redPacketId + "-money", "200000000");
        /**
         * 模拟100个用户抢10个红包
         */
        for (int i = 1; i <= skillNum; i++) {
            int userId = i;
            Runnable task = () -> {
                /**
                 * 抢红包 判断剩余金额
                 */
                Integer money = Integer.parseInt(redisUtil.get(redPacketId + "-money"));
                if (money > 0) {
                    /**
                     * 虽然能抢到 但是不一定能拆到
                     * 类似于微信的 点击红包显示抢的按钮
                     */
                    Result result = demo.startTwoSeckil(redPacketId, userId);
                    if (result.getCode().toString().equals("500")) {
                        log.info("用户{}手慢了，红包派完了", userId);
                    } else {
                        Double amount = DoubleUtil.div(Double.parseDouble(result.getMessage()), (double) 100);
                        log.info("用户{}抢红包成功，金额：{}", userId, amount);
                    }
                } else {
                    /**
                     * 直接显示手慢了，红包派完了
                     */
                    log.info("用户{}手慢了，红包派完了", userId);
                }
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();
            Integer restMoney = Integer.parseInt(redisUtil.get(redPacketId + "-money").toString());
            log.info("剩余金额：{}", restMoney);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
