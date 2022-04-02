# mask

### 它有什么作用

* 配置化的对API请求响应体做脱敏处理。
* 更简易的对敏感信息进行管理。

> 你的项目中是否存在这样的代码：
>
> * ```java
>       @PostMapping("example")
>       public User example(){
>           // ... other logic
>           user.setName(EncryptUtils.encrypt(user.getName()));
>           return user;
>       }
>   ```
>
> * ```java
>   public class EntityVO {
>       /**
>        *  ... other field
>        */
>       
>       private String name;
>       /**
>        *  ... other Setter and Getter
>        */
>       
>       public String getName() {
>           return name;
>       }
>       
>       public void setName(String name) {
>           this.name = EmptyUtils.isEmpty(name) ? name:EncryptUtils.encrypt(name);
>       }
>   }
>   ```
>
>   脱敏逻辑与业务逻辑强耦合；甚至修改VO实体的部分Setter方法；使代码复用性大大降低，以至类爆炸。

**mask 是一个解决方案，通过作用Jackson序列化过程，完成解耦。**

### 快速入门

> ```shell
> git clone https://github.com/paimonx/mask.git
> ```
>
> ```shell
> mvn clean package -Dmaven.test.skip=true
> ```
>
> >  工程加载jar，并配置信息
> >
> >  例如：
> >
> >  ```yaml
> >  spring:
> >  api:
> >    mask:
> >      config:
> >        enabled: true
> >        skip-uri:
> >            - /not/user
> >  #        trust-ip:
> >  #          - 192.168.12.120
> >          uri-type:
> >            "[/uriType]": "*common"
> >          class-definitions:
> >            "[com.paimonx.maskexample.entity.Address]":
> >              detailed: "common"
> >            "[com.paimonx.maskexample.entity.User]":
> >              name: "name"
> >              idNo: "idno"
> >              phone: "phone"
> >              onceName: "*name"
> >              age: "common"
> >  ```
>
> [**example**](https://github.com/paimonx/mask-example)

### 进阶使用

* 自定义算法(MaskAlgorithm)

  >  算法通过SPI机制进行加载，可以通过实现接口 `com.paimonx.mask.spi.MaskAlgorithm`来进行拓展(PS: type唯一)。
  >
  > > 对算法进行配置，需要实现`com.paimonx.mask.support.InitIal`接口，并配置`spring.api.mask.config.algorithmMetadata` 

  

* 兼容集合

  需要在期望的type前加 `*`。

  > 如果返回值是一个集合：
  >
  > * type 配置为`common`,那么将显示成`"***"`
  > * type 配置为`*common`,那么将显示成`["***","***","***"]`

  

* 基础类型

  > 因为基础类型并不被Jackson进行作用，请配置`spring.api.mask.config.uriType`来进行控制。
  >

### 性能测试

> ##### 环境
>
> ```
> JDK: jdk-8u191
> CPU: R7-5800H
> Memory: 4G
> Spring Boot: 2.1.18.RELEASE
> ```
>
> ##### 结论
>
> mask大相较于原生Jackson额外消耗在5%之内。(PS:详见JMH基准测试)
>
> [**MaskJMHTest**](https://github.com/paimonx/mask/blob/main/src/test/java/com/paimonx/mask/MaskJMHTest.java)
>
> ##### JMeter（v1.0）
>
> > * **原生Jackson**  
> >
> >   <img src="https://user-images.githubusercontent.com/94901242/160850005-a14513ed-03bb-40f5-8fe6-1a51c1970e43.png" alt="not" style="zoom:80%;" />
> >
> > * **原生Jackson(1个字段)**  
> >
> >   <img src="https://user-images.githubusercontent.com/94901242/160851260-947efedc-6142-40e0-bc25-4b520e63b816.png" alt="not-1" style="zoom:80%;" />
> >
> > * **mask(1个字段)**  
> >
> >   <img src="https://user-images.githubusercontent.com/94901242/160851595-d6829c4a-f2ec-4a61-9172-7b1cebf596d3.png" alt="mask-1" style="zoom:80%;" />
> >
> > * **mask(5个字段)**  
> >
> >   <img src="https://user-images.githubusercontent.com/94901242/160852059-16399216-f74b-40dc-9175-88741b2551cf.png" alt="mask-5" style="zoom:80%;" />
>
> ##### JVisulaVM(v1.0)
>
> > * **原生Jackson**  
> >
> >   <img src="https://user-images.githubusercontent.com/94901242/160853792-a00fb1a9-e6f1-4559-aa19-5bbdf4c38f4e.png" alt="not-heap" style="zoom: 33%;" />
> >
> > * **原生Jackson(1个字段)**  
> >
> >   <img src="https://user-images.githubusercontent.com/94901242/160854069-b66f0062-d659-4114-b9cc-aed5dbd5d7ad.png" alt="not-1-heap" style="zoom: 33%;" />
> >
> > * **mask(1个字段)**  
> >
> >   <img src="https://user-images.githubusercontent.com/94901242/160854212-a7593922-9f1b-4fbb-930c-8d28e78cfb4f.png" alt="mask-1-heap" style="zoom: 33%;" />
> >
> > * **mask(5个字段)**   
> >
> >   <img src="https://user-images.githubusercontent.com/94901242/160854349-44e82557-5fc8-4278-99d2-4afa096e4b43.png" alt="mask-5-heap" style="zoom: 33%;" />
>
> ##### JMH(now)
>
> > ![JMH](https://user-images.githubusercontent.com/94901242/161273127-a48a1268-db89-4543-af15-47116529c9f1.png)

### 设计思想
<img src="https://user-images.githubusercontent.com/94901242/160125054-fc5db4df-1a49-4629-8967-f4d7e25e3878.png" alt="Spring"  />
<img src="https://user-images.githubusercontent.com/94901242/161388039-84b7f168-81a9-4bf3-a3b3-780c66089b08.png" alt="mask"  />

