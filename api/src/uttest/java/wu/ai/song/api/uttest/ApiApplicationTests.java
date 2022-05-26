package wu.ai.song.api.uttest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wu.ai.song.api.entity.User;
import wu.ai.song.api.mapper.UserDao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
//不启动服务器,使用mockMvc进行测试http请求。启动了完整的Spring应用程序上下文，但没有启动服务器
@AutoConfigureMockMvc
@ActiveProfiles("ut")
class ApiApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;


    /**
     * .perform() : 执行一个MockMvcRequestBuilders的请求；MockMvcRequestBuilders有.get()、.post()、.put()、.delete()等请求。
     * .andDo() : 添加一个MockMvcResultHandlers结果处理器,可以用于打印结果输出(MockMvcResultHandlers.print())。
     * .andExpect : 添加MockMvcResultMatchers验证规则，验证执行结果是否正确。
     */
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        List<User> list = userDao.selectList(null);
        list.forEach(System.out::println);
    }

    /**
     * 添加数据
     */
    @Test
    public void testInsert() {
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

    /**
     * 测试乐观锁成功
     */
    @Test
    public void testOptimisticLocker() {
        // 1.查询用户信息
        User user = userDao.selectById(1L);
        // 2.修改用户信息
        user.setName("墨白科技");
        user.setEmail("mobaijun8@163.com");
        user.setAge(21);
        // 3.执行更新
        userDao.updateById(user);
    }

    /**
     * 测试乐观锁失败！多线程下
     */
    @Test
    public void testOptimisticLocker2() {
        /**
         * 线程1
         */
        User user = userDao.selectById(1L);
        user.setName("墨白科技111");
        user.setEmail("mobaijun8@163.com");
        user.setAge(21);
        /**
         * 线程2
         * 模拟另外一个线程执行了插队操作
         */
        User user2 = userDao.selectById(1L);
        user2.setName("墨白科技222");
        user2.setEmail("mobaijun8@163.com");
        user2.setAge(21);
        userDao.updateById(user2);
        // 自旋锁来多次尝试提交！
        userDao.updateById(user); // 如果没有乐观锁就会覆盖插队线程的值！
    }

    /**
     * 测试查询
     */
    @Test
    public void testSelectById() {
        User id = userDao.selectById(1L);
        System.out.println("id = " + id);
    }

    /**
     * 批量查询
     */
    @Test
    public void testSelectByBatchId() {
        List<User> users = userDao.selectBatchIds(Arrays.asList(1, 2, 3));
        System.out.println("users = " + users);
    }

    /**
     * 按条件查询
     */
    @Test
    public void testSelectByBatchIdS() {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义条件
        map.put("name", "Jack");
        map.put("age", 20);
        List<User> users = userDao.selectByMap(map);
        users.forEach(System.out::println);
    }
    /**
     * 测试分页查询
     */
    @Test
    public void testPage() {
        /**
         * 参数一: 当前页
         * 参数一: 页大小
         * 使用了分页插件以后,所有的分页操作也变得非常简单
         */
        Page<User> page = new Page<>(2, 4);
        // 调用selectPage进行分页
        userDao.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
        // 获取记录总数
        System.out.println("page = " + page.getTotal());
    }


    /**
     * 测试删除
     */
    @Test
    public void testDeleteById() {
        userDao.deleteById(1269882840871374854L);
    }
    /**
     * 批量删除
     */
    @Test
    public void testDeleteList() {
        userDao.deleteBatchIds(Arrays.asList(1269882840871374853L,
                1269882840871374852L,
                1269882840871374851L,
                1269882840871374850L,
                1269882840871374849L));
    }
    /**
     * 通过集合删除
     */
    @Test
    public void testDeleteMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "墨白科技222");
        userDao.deleteByMap(map);
    }

    /**
     * 逻辑删除
     * 相当于回收站
     */
    @Test
    public void testDeleted() {
        userDao.deleteById(6L);
    }

    /**
     * 测试性能分析插件
     */
    @Test
    public void contextLoads2() {
        List<User> users = userDao.selectList(null);
        users.forEach(System.out::println);
    }

}
