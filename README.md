# mask

api mask

针对api 请求响应体做遮掩处理；

### 使用

1. ```shell
   `git clone https://github.com/paimonx/mask.git`
   ```

2. ```shell
   `mvn clean package -Dmaven.test.skip=true`
   ```

3. 工程引入jar 包，并配置信息

   例如：

```yaml
spring:
  api:
    mask:
      config:
        enabled: true
        class-definitions:
          "[com.paimonx.maskexample.entity.User]":
            name: "name"
            idNo: idno
            phone: phone
          "[com.paimonx.maskexample.entity.Address]":
            detailed: common
```