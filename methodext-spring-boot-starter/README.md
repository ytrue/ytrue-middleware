# 服务治理，自定义拦截方法

## 一、使用


```java
  /**
   * 放行：http://localhost:8081/api/queryUserInfo?userId=aaa
   * 拦截：http://localhost:8081/api/queryUserInfo?userId=bbb
   */
  @DoMethodExt(method = "blacklist", returnJson = "{\"code\":\"1111\",\"info\":\"自定义校验方法拦截，不允许访问！\"}")
  @RequestMapping(path = "/api/queryUserInfo", method = RequestMethod.GET)
  public UserInfo queryUserInfo(@RequestParam String userId) {
      logger.info("查询用户信息，userId：{}", userId);
      return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
  }
  
  /**
   * 自定义黑名单，拦截方法
   */
  public boolean blacklist(@RequestParam String userId) {
      if ("bbb".equals(userId) || "222".equals(userId)) {
          logger.info("拦截自定义黑名单用户 userId：{}", userId);
          return false;
      }
      return true;
  }
```

## 二、测试

### 4. 测试结果

**不在黑名单拦截调用**

**接口**：`http://localhost:8081/api/queryUserInfo?userId=aaa`

```json
{"code":"0000","info":"success","name":"虫虫:aaa","age":19,"address":"天津市东丽区万科赏溪苑14-0000"}
```

**被自定义黑名单拦截**

**接口**：`http://localhost:8081/api/queryUserInfo?userId=123`

```json
{"code":"1111","info":"自定义校验方法拦截，不允许访问！","name":null,"age":null,"address":null}
```
