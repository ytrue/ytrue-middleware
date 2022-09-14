# 服务治理，超时熔断

## 一、使用


```java
  @DoHystrix(timeoutValue = 350, returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过350毫秒，熔断返回！\"}")
      @RequestMapping(path = "/api/queryUserInfo", method = RequestMethod.GET)
      public UserInfo queryUserInfo(@RequestParam String userId) throws InterruptedException {
          log.info("查询用户信息，userId：{}", userId);
  
          while (System.currentTimeMillis() > 1663142600000L){
              break;
          }
  
          return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
      }
  
  
      @MyHystrix(timeoutValue = 10000, returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过350毫秒，熔断返回！\"}")
      @RequestMapping(path = "/api/queryUserInfo1", method = RequestMethod.GET)
      public UserInfo queryUserInfo1(@RequestParam String userId) throws InterruptedException {
          log.info("查询用户信息，userId：{}", userId);
          Thread.sleep(1000);
          int i = 1/0;
          return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
      }
```

## 二、测试

### 4. 测试结果

**不超时访问**

**接口**：`http://localhost:8081/api/queryUserInfo?userId=aaa`

```json
{
    "code": "0000",
    "info": "success",
    "name": "虫虫:aaa",
    "age": 19,
    "address": "天津市东丽区万科赏溪苑14-0000"
}
```

**超时访问**

**接口**：`http://localhost:8081/api/queryUserInfo?userId=123`

```json
{"code":"1111","info":"调用方法超过350毫秒，熔断返回！","name":null,"age":null,"address":null}
```
