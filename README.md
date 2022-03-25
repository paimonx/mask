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

### 设计思想
![未命名文件](https://user-images.githubusercontent.com/94901242/160125054-fc5db4df-1a49-4629-8967-f4d7e25e3878.png)
![未命名文件 (1)](https://user-images.githubusercontent.com/94901242/160125075-ffb7f58c-9b88-4f9a-baef-18ef280bde79.png)

