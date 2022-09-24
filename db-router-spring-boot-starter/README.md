# 1. 使用
## 1.1 yml配置
```yaml
# 路由配置
ytrue:
  db-router:
    db-count: 2
    tb-count: 4
    list: db01,db02
    db-config:
      db01:
        driver-class-name: com.mysql.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/ytrue01?useUnicode=true&useSSL=false
        username: root
        password: root
      db02:
        driver-class-name: com.mysql.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/ytrue02?useUnicode=true&?useSSL=false
        username: root
        password: root
```

## 1.2 实体类
```java
public class User extends DBRouterBase {

    private Long id;
    private String userId;          // 用户ID
    private String userNickName;    // 昵称
    private String userHead;        // 头像
    private String userPassword;    // 密码
    private Date createTime;        // 创建时间
    private Date updateTime;        // 更新时间
   
    // ... get/set
}
```

## 1.3 dao接口
```java
@Mapper
public interface IUserDao {

     @DBRouter(key = "userId")
     User queryUserInfoByUserId(User req);

     @DBRouter(key = "userId")
     void insertUser(User req);

}
```
## 1.4 测试
```java
@Test
public void test_insertUser() {
    User user = new User();
    user.setUserId("980765512");
    user.setUserNickName("ytrue");
    user.setUserHead("01_50");
    user.setUserPassword("123456");
    userDao.insertUser(user);
}


@Test
public void test_queryUserInfoByUserId() {
    User user = userDao.queryUserInfoByUserId(new User("980765512"));
    logger.info("测试结果：{}", JSON.toJSONString(user));
}
```
