# 服务治理，统一白名单控制

## 一、使用

```yml
ytrue:
  whitelist:
    users: aaa,111,ytrue
```



```java
  @DoWhiteList(key = "userId", returnJson = "{\"code\":\"1111\",\"info\":\"非白名单可访问用户拦截！\"}")
    @RequestMapping(path = "/api/queryUserInfo", method = RequestMethod.GET)
    public UserInfo queryUserInfo(@RequestParam String userId) {
        log.info("查询用户信息，userId：{}", userId);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }
```

## 二、测试

### 4. 测试结果

**白名单用户访问**

**接口**：`http://localhost:8081/api/queryUserInfo?userId=aaa`

```java
{
    "code": "0000",
    "info": "success",
    "name": "虫虫:aaa",
    "age": 19,
    "address": "天津市东丽区万科赏溪苑14-0000"
}
```

**普通用户访问**

**接口**：`http://localhost:8081/api/queryUserInfo?userId=123`

```java
{
    "code": "1111",
    "info": "非白名单可访问用户拦截！",
    "name": null,
    "age": null,
    "address": null
}
```
