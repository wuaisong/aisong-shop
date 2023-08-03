package wu.ai.song.api.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.cursor.Cursor;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import wu.ai.song.api.ApiApplication;
import wu.ai.song.api.entity.SysUser;
import wu.ai.song.api.entity.User;
import wu.ai.song.api.mapper.UserComponent;
import wu.ai.song.api.mapper.UserDao;
import wu.ai.song.api.mapper.UserMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@SpringBootTest(classes = ApiApplication.class)
//不启动服务器,使用mockMvc进行测试http请求。启动了完整的Spring应用程序上下文，但没有启动服务器
@AutoConfigureMockMvc
@ActiveProfiles("ut")
@Transactional
class ApiApplicationTestsUT {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserComponent userComponent;

    /**
     * 条件查询
     */
    @Test
    public void testMysql() {
        List<SysUser> users = userMapper.findUsers(1);
        Assertions.assertThat(users).isNotNull();
        System.out.println("users = " + users.size());

    }

    /**
     * 条件查询
     */
    @Test
    public void wrapper1() {
        // 查询name不为空和email不为空并且年龄大于等于12
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 链式编程
        wrapper.isNotNull("name").isNotNull("email").ge("age", 20);
        userDao.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    public void testSayHi() {
        System.out.println("Hi Junit.");
    }

    /**
     * 根据名称查询
     */
    @Test
    public void wrapper2() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "MoBai");
        // selectOne(wrapper);查询一个数据
        User user = userDao.selectOne(wrapper);
        System.out.println("user = " + user);
    }

    /**
     * 查询区间
     */
    @Test
    public void wrapper3() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 查询年龄在15-25之间的数据
        wrapper.between("age", 15, 25);
        // selectOne(wrapper);查询一个数据
        Integer count = userDao.selectCount(wrapper);
        System.out.println(count);
    }

    /**
     * 模糊查询
     */
    @Test
    public void test4() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.
                // 查询name不包含e的字段
                        notLike("name", "e").
                // 左和右 t%
                        likeRight("email", "t");
        List<Map<String, Object>> maps = userDao.selectMaps(wrapper);
        maps.forEach(System.out::println);
    }

    /**
     * 模糊查询
     */
    @Test
    public void test5() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // id在子查询中查出来
        wrapper.inSql("id", "select id from user where id < 3");
        List<Object> list = userDao.selectObjs(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 模糊查询
     */
    @Test
    public void test6() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 通过ID进行排序
        wrapper.orderByAsc("id");
        List<User> list = userDao.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * @link https://juejin.cn/post/6999145665787854856
     * 测试驼峰转换
     * 新增事务后方法会公用一个sql session
     * TransactionAspectSupport#invokeWithinTransaction
     * SqlSessionTemplate#selectList(String, Object)
     * TransactionSynchronization
     * AbstractPlatformTransactionManager
     * AbstractPlatformTransactionManager#processCommit
     */
    @Test
    public void test7() {
        // 第一次查询
        List<User> list = userDao.getDepartmentAll();
        list.forEach(user -> user.setName("MoBai~~~~"));
        // 第二次查询
        List<User> list12 = userDao.getDepartmentAll();
        list12.forEach(System.out::println);
    }

    @Test
    public void test8() {
        Cursor<User> users = userDao.cursorDepartment();
        users.forEach(user -> user.setName("MoBai~~~~"));
    }

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
        ArrayList<User> objects = Lists.newArrayList();
        IntStream.range(0, 10).forEach(i -> {
            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setAge(new Random().nextInt());
            user.setEmail(UUID.randomUUID().toString());
            objects.add(user);
        });
        userComponent.saveBatch(objects);

        // IntStream.range(0, 1000).forEach(i -> {
        //     User user = new User();
        //     user.setName("墨白君");
        //     user.setAge(25);
        //     user.setEmail("mobaijun8@163.com");
        //     // mybatis-plus会自动帮助我们生成主键ID
        //     int insert = userDao.insert(user);
        //     // 被影响的行数
        //     System.out.println("insert = " + insert);
        //     // ID会自动回填
        //     System.out.println("user = " + user);
        // });

    }

    /**
     * 批量写入
     */
    @Test
    public void testBatchInsert() {
        ArrayList<User> objects = Lists.newArrayList();
        IntStream.range(0, 10).forEach(i -> {
            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setAge(new Random().nextInt());
            user.setEmail(UUID.randomUUID().toString());
            objects.add(user);
        });
        userDao.insertBatchSomeColumn(objects);

        // IntStream.range(0, 1000).forEach(i -> {
        //     User user = new User();
        //     user.setName("墨白君");
        //     user.setAge(25);
        //     user.setEmail("mobaijun8@163.com");
        //     // mybatis-plus会自动帮助我们生成主键ID
        //     int insert = userDao.insert(user);
        //     // 被影响的行数
        //     System.out.println("insert = " + insert);
        //     // ID会自动回填
        //     System.out.println("user = " + user);
        // });

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
        userDao.deleteBatchIds(
                Arrays.asList(1269882840871374853L, 1269882840871374852L, 1269882840871374851L, 1269882840871374850L,
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

    @Test
    public void testCursor1() {
        List<User> users = userDao.queryDepartmentAll();
        users.forEach(System.out::println);
    }

    @Test
    public void testCursor2() throws IOException {
        try (Cursor<User> cursor = userDao.cursorDepartment()) {
            Iterator<User> iterator = cursor.iterator();
            int batchSize = 10;
            int page = 0;
            List<User> data = new ArrayList<>();
            while (iterator.hasNext()) {
                data.add(iterator.next());
                if (data.size() % batchSize == 0 || !iterator.hasNext()) {
                    int current = cursor.getCurrentIndex() + 1;
                    page = (current / batchSize) + 1;
                    print(data, page);
                    data.clear();
                }
            }
            System.out.println("总页数: " + page);
        }
    }

    private void print(List<User> users, Integer page) {
        System.out.println(page + "-----" + users.stream().findFirst().get());
    }

}
