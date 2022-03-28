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
> > 例如：
> >
> > ```yaml
> > spring:
> >   api:
> >     mask:
> >       config:
> >         enabled: true
> >         class-definitions:
> >           "[com.paimonx.maskexample.entity.User]":
> >             name: "name"
> >             idNo: idno
> >             phone: phone
> >           "[com.paimonx.maskexample.entity.Address]":
> >             detailed: common
> > ```

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



### 设计思想
![未命名文件](https://user-images.githubusercontent.com/94901242/160125054-fc5db4df-1a49-4629-8967-f4d7e25e3878.png)
![未命名文件 (1)](https://user-images.githubusercontent.com/94901242/160125075-ffb7f58c-9b88-4f9a-baef-18ef280bde79.png)

