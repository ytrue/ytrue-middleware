# 服务治理，调用限流

## 一、使用


```java
    /**
     * 测试：http://localhost:8081/api/queryUserInfo?userId=aaa
     */
    @DoRateLimiter(permitsPerSecond = 1, returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过最大次数，限流返回！\"}")
    @RequestMapping(path = "/api/queryUserInfo", method = RequestMethod.GET)
    public UserInfo queryUserInfo(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息，userId：{}", userId);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }
```

## 二、测试

### 4. 测试结果

**方法调用次数每秒小于1次**

**接口**：`http://localhost:8081/api/queryUserInfo?userId=aaa`

```json
{"code":"0000","info":"success","name":"虫虫:aaa","age":19,"address":"天津市东丽区万科赏溪苑14-0000"}
```

**方法调用次数每秒大于1次**

**接口**：`http://localhost:8081/api/queryUserInfo?userId=123`

```json
{"code":"1111","info":"调用方法超过最大次数，限流返回！","name":null,"age":null,"address":null}
```
