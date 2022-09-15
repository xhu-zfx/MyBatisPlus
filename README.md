# Mybatis-Plus笔记

# 相关代码见[F:\Java Web\SpringBoot2\mybatis-plus](F:\Java Web\SpringBoot2\mybatis-plus)

# 1、使用方法

1. pom.xml中引入Mybatis-plus以及mysql驱动依赖


    ```xml
    		<dependency>
    			<groupId>com.baomidou</groupId>
    			<artifactId>mybatis-plus-boot-starter</artifactId>
    			<version>3.5.1</version>
    		</dependency>
    		<dependency>
    			<groupId>org.projectlombok</groupId>
    			<artifactId>lombok</artifactId>
    			<optional>true</optional>
    		</dependency>
    		<dependency>
    			<groupId>mysql</groupId>
    			<artifactId>mysql-connector-java</artifactId>
    			<scope>runtime</scope>
    			<version>5.1.39</version>
    		</dependency>
    ```

2. application.yaml中配置数据源

    ```xml
    spring:
      datasource:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/xhu?characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
        username: root
        password: 134161
    ```
3. 创建实体类，以User为例，字段与数据库user表一致

    ```sql
    CREATE TABLE user
    (
        id BIGINT(20) NOT NULL COMMENT '主键ID',
        name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
        age INT(11) NULL DEFAULT NULL COMMENT '年龄',
        email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
        PRIMARY KEY (id)
    );
    ```

    ```java
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class User {
        private Long id;
        private String name;
        private Integer age;
        private String email;
    }
    ```

4. 创建Mapper接口，继承BaseMapper<>接口，

    ```java
    public interface UserMapper extends BaseMapper<User> {
    }
    ```

5. 在SpringBoot启动类上添加扫描到该Mapper包`@MapperScan("com.atguigu.mybatisplus.mapper")`


# 2、原理图

![mybatis-plus-framework](assets/mybatis-plus-framework-20220814200943-djt6ugu.jpg)



# 3、BaseMapper增删改查

* ## Insert

  插入主键时默认使用雪花算法
* ## Delete

  1. 根据id删除`deleteById()`

  2. 根据map所设置的条件删除`deleteByMap(Map<String, Object> columnMap)`，map中存储的key为字段名，value为具体数据，多组k-v之间为AND关系

  `map.put("id",1558790889986428929L)              `

  `map.put("name","张三")`

  执行时具体sql语句为

  `==>  Preparing: DELETE FROM user WHERE name = ? AND id = ?`  
  `==> Parameters: 张三(String), 1558790889986428929(Long)`  
  `<==   Updates: 0`

  3. 批量删除`deleteBatchIds(Collection<?> idList)`，传入一个集合

  执行时具体sql语句为

  `==>  Preparing: DELETE FROM user WHERE id IN ( ? , ? , ? )`  
  `==> Parameters: 1(Long), 2(Long), 3(Long)`  
  `<==    Updates: 3`

  111

* ## Select

  1. 根据id查询`T selectById(Serializable id)`
  2. 通过id批量查询`List<T> selectBatchIds(@Param("coll") Collection<? extends Serializable> idList)`

  执行时具体sql语句为

  `==>  Preparing: SELECT id,name,age,email FROM user WHERE id IN ( ? , ? , ? )`  
  `==> Parameters: 1(Long), 2(Long), 3(Long)`  
  `<==    Columns: id, name, age, email`  
  `<==        Row: 1, Jone, 18, test1@baomidou.com`  
  `<==        Row: 2, Jack, 20, test2@baomidou.com`  
  `<==        Row: 3, Tom, 28, test3@baomidou.com`  
  `<==      Total: 3`

  3. 根据map查询`List<T> selectByMap(@Param("cm") Map<String, Object> columnMap)`，多个k-v之间为AND关系
  4. 根据List查询`List<T> selectList(@Param("ew") Wrapper<T> queryWrapper)`，参数为条件构造器

  1

* ## Update

  根据id修改`updateById(T entity)`，传入一个实体类

  执行时具体sql语句

  `==>  Preparing: UPDATE user SET name=?, email=? WHERE id=?`  
  `==> Parameters: 张我(String), 123@qq.com(String), 4(Long)`  
  `<===    Updates: 1`

  1

  1

  1


# 4、通用Service

# 5、常用注解

## @TableName注解

当实体类与表名不同时，在实体类中使用此注解表示该实体类对应的表名

同时，在yaml配置文件中可以设置mybatis-plus全局配置来为实体类通知数据库表名的统一前缀，

比如此时表名全为t_开头，此时实体类为User，会自动映射到t_user的表

```yaml
mybatis-plus:  
  global-config:
    db-config:
      table-prefix: t_
```


## @TableId注解

添加此注解到实体类属性上，将会把该属性对应的字段标记为主键(当该属性名不为`id`时)

|属性|类型|必须指定|默认值|描述|
| -------| --------| ----------| -------------| --------------|
|value|String|否|""|主键字段名|
|type|Enum|否|IdType.NONE|指定主键类型|

value属性用于指定主键的字段名

type属性用于定义主键策略

IdType值一览：

|值|描述|
| -----------------| ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|AUTO|数据库 ID 自增(需要数据库字段设置为自增)|
|NONE|无状态，该类型为未设置主键类型（注解里等于跟随全局，全局里约等于 INPUT）|
|INPUT|insert 前自行 set 主键值|
|ASSIGN_ID(默认)|分配 ID(主键类型为 Number(Long 和 Integer)或 String)(since 3.3.0),<br />使用接口`IdentifierGenerator`的方法`nextId`(默认实现类为`DefaultIdentifierGenerator`雪花算法)<br />|
|ASSIGN_UUID|分配 UUID,主键类型为 String(since 3.3.0),<br />使用接口`IdentifierGenerator`的方法`nextUUID`(默认 default 方法)<br />|

同时，在yaml配置文件中可以设置全局配置主键生成策略

```yaml
mybatis-plus:
  global-config:
    db-config:
      id-type: auto
```

## 雪花算法：

* ### 背景

  需要选择合适的方案去应对数据规模的增长，以应对逐渐增长的访问压力和数据量。

  数据库的扩展方式主要包括：业务分库、主从复制，数据库分表。
* ### 数据库分表

  将不同业务数据分散存储到不同的数据库服务器，能够支撑百万甚至千万用户规模的业务，但如果业务继续发展，同一业务的单表数据也会达到单台数据库服务器的处理瓶颈。例如，淘宝的几亿用户数据，如果全部存放在一台数据库服务器的一张表中，肯定是无法满足性能要求的，此时就需要对单表数据进行拆分。

  单表数据拆分有两种方式：垂直分表和水平分表。示意图如下

* ### 垂直分表

  垂直分表适合将表中某些不常用且占了大量空间的列拆分出去。

  例如，前面示意图中的 nickname 和 description 字段，假设我们是一个婚恋网站，用户在筛选其他用

  户的时候，主要是用 age 和 sex 两个字段进行查询，而 nickname 和 description 两个字段主要用于展

  示，一般不会在业务查询中用到。description 本身又比较长，因此我们可以将这两个字段独立到另外

  一张表中，这样在查询 age 和 sex 时，就能带来一定的性能提升。
* ### 水平分表

  水平分表适合表行数特别大的表，有的公司要求单表行数超过 5000 万就必须进行分表，这个数字可以

  作为参考，但并不是绝对标准，关键还是要看表的访问性能。对于一些比较复杂的表，可能超过 1000

  万就要分表了；而对于一些简单的表，即使存储数据超过 1 亿行，也可以不分表。

  但不管怎样，当看到表的数据量达到千万级别时，作为架构师就要警觉起来，因为这很可能是架构的性

  能瓶颈或者隐患。

  水平分表相比垂直分表，会引入更多的复杂性，例如要求全局唯一的数据id该如何处理
* ### 主键自增

  ①以最常见的用户 ID 为例，可以按照 1000000 的范围大小进行分段，1 ~ 999999 放到表 1中，

  1000000 ~ 1999999 放到表2中，以此类推。

  ②复杂点：分段大小的选取。分段太小会导致切分后子表数量过多，增加维护复杂度；分段太大可能会

  导致单表依然存在性能问题，一般建议分段大小在 100 万至 2000 万之间，具体需要根据业务选取合适

  的分段大小。

  ③优点：可以随着数据的增加平滑地扩充新的表。例如，现在的用户是 100 万，如果增加到 1000 万，

  只需要增加新的表就可以了，原有的数据不需要动。

  ④缺点：分布不均匀。假如按照 1000 万来进行分表，有可能某个分段实际存储的数据量只有 1 条，而

  另外一个分段实际存储的数据量有 1000 万条。
* ### 取模

  ①同样以用户 ID 为例，假如我们一开始就规划了 10 个数据库表，可以简单地用 user_id % 10 的值来

  表示数据所属的数据库表编号，ID 为 985 的用户放到编号为 5 的子表中，ID 为 10086 的用户放到编号

  为 6 的子表中。

  ②复杂点：初始表数量的确定。表数量太多维护比较麻烦，表数量太少又可能导致单表性能存在问题。

  ③优点：表分布比较均匀。

  ④缺点：扩充新的表很麻烦，所有数据都要重分布。
* ### 雪花算法

  雪花算法是由Twitter公布的分布式主键生成算法，它能够保证不同表的主键的不重复性，以及相同表的主键的有序性。

  ①核心思想：

  长度共64bit（一个long型）。

  首先是一个符号位，1bit标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0。

  41bit时间截(毫秒级)，存储的是时间截的差值（当前时间截 - 开始时间截)，结果约等于69.73年。

  10bit作为机器的ID（5个bit是数据中心，5个bit的机器ID，可以部署在1024个节点）。

  12bit作为毫秒内的流水号（意味着每个节点在每毫秒可以产生 4096 个 ID）。

  ![image](assets/image-20220815124446-uwiap13.png)

  ②优点：整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞，并且效率较高。


## @TableFiled注解

添加该注解于实体类属性上，用于指属性所对应的数据库字段（**非主键**），

## @TableLogic注解

### 逻辑删除

* 物理删除：真实删除，将对应数据从数据库中删除，之后查询不到此条被删除的数据
* 逻辑删除：假删除，将对应数据中代表是否被删除字段的状态修改为“被删除状态”，之后在数据库中仍旧能看到此条数据记录

### 使用场景

可以进行数据恢复

### 使用方法

1. 在数据库中创建逻辑删除列(is_deleted)，**设置默认值为0**
2. 在实体类中添加逻辑删除属性，在该属性上添加@TableLogic注解

### 使用原理

当**执行删除方法后该字段会被置为1**，表示该数据已被逻辑删除(实际为修改操作)

当运行删除方法时(实际执行逻辑删除)具体sql语句

`==>  Preparing: UPDATE t_user SET id_deleted WHERE id=? AND is_deleted=0`

需要注意的是：**在数据被逻辑删除后，执行查询方法时不会查询到该条数据**，此时查询的具体sql语句为

`==>  Preparing: SELECT id,name,age,email,is_deleted FROM user WHERE is_deleted=0`


# 6、条件构造器wrapper与常用接口

> 在BaseMapper、IService类中的方法形参，大量使用条件构造器作为形参
>

## 1 组装查询条件

**用法：**

默认多个条件之间and采用链式语法，如果要使用or，则链中使用.or()分隔即可

```java
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name","a")
                .between("age","20","30")
                .isNotNull("email");
                .or()
                .isNull("email");
        List<User> users = userMapper.selectList(queryWrapper);
```

上例执行时具体sql语句

`==>  Preparing: SELECT id,name,age,email,is_deleted FROM user WHERE is_deleted=0 AND (name LIKE ? AND age BETWEEN ? AND ? OR email IS NULL)`  
`==> Parameters: %a%(String), 20(String), 30(String)`  
`<==    Columns: id, name, age, email`  
`<==        Row: 2, Jack, 20, test2@baomidou.com`  
`<==        Row: 4, Sandy, 21, test4@baomidou.com`  
`<==      Total: 2`

## 2 组装排序条件

**用法**


```java
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("name")
                .orderByAsc("id");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
```

上例执行时具体sql语句

`==>  Preparing: SELECT id,name,age,email FROM user ORDER BY name DESC,id ASC`  
`==> Parameters:`

`<==    Columns: id, name, age, email`  
`<==        Row: 3, Tom, 28, test3@baomidou.com`  
`<==        Row: 4, Sandy, 21, test4@baomidou.com`  
`<==        Row: 1, Jone, 18, test1@baomidou.com`  
`<==        Row: 2, Jack, 20, test2@baomidou.com`  
`<==        Row: 5, Billie, 24, test5@baomidou.com`  
`<==      Total: 5`

## 3 组装删除条件

组装语法与上面一样，但要注意**该处的删除指的是逻辑删除**(sql实际执行update语句)


## 4 修改功能

`int update(@Param("et") T entity, @Param("ew") Wrapper<T> updateWrapper);`

entity代表要传入的对象，updateWrapper是修改条件

```java
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.gt("age",20)
                .like("name","a")
                .or()
                .isNull("email");
        User user = new User();
        user.setName("小露");user.setEmail("222@qq.com");
        int res = userMapper.update(user,queryWrapper);
        System.out.println(res);

```

上例执行中sql如下

`==>  Preparing: UPDATE user SET name=?, email=? WHERE is_deleted=0 AND (age > ? AND name LIKE ? OR email IS NULL)`  
`==> Parameters: 小露(String), 222@qq.com(String), 20(Integer), %a%(String)`

`<==    Updates: 1`

## 5 条件优先级

比较：将 (年龄大于20并且用户名中含有a) 或 邮箱为null 的用户信息修改  ( 修改功能中的例子 )

          将 用户名中包含有a 且 (年龄大于20或邮箱为null) 的用户信息修改

之间的区别

后者需要使用lambda表达式，如下

```java
        queryWrapper.like("name","a")
                .and(i->i.gt("age",20).or().isNull("email"));
```

lambda表达式中的`i.gt("age",20).or().isNull("email")`会优先执行

具体sql语句如下

`==>  Preparing: UPDATE user SET name=?, email=? WHERE is_deleted=0 AND (name LIKE ? AND (age > ? OR email IS NULL))
==> Parameters: 小红(String), 456@qq.com(String), %a%(String), 20(Integer)`

## 6 组装select子句

`QueryWrapper<T> select(String... columns)`

形参为变长的，使用时写要查询的字段名，如只查询name、age字段

`queryWrapper.select("name","age");`


## 7 组装子查询

`inSql(R column, String inValue)`

quaryWrapper对象调用inSql方法即可，column为子查询的查询结果，即作为父查询的查询条件，inValue为子查询的sql语句

`queryWrapper.inSql("id","select id from user where age>20");`

执行sql为

`==>  Preparing: SELECT id,name,age,email,is_deleted FROM user WHERE is_deleted=0 AND (id IN (select id from user where age>20))`

## 8 使用UpdateWrapper实现修改

设置updateWrapper的匹配条件，然后通过`.set()`方法设置k-v设置要修改的数据

由于update方法需要传入实体对象，但此时数据都存在updateWrapper对象中，所以将其设为null

```java
        UpdateWrapper<User> updateWrapper=new UpdateWrapper<>();
        updateWrapper.like("name","a")
                .and(i->i.gt("age",20)
                                .or()
                                .isNull("email")

                    );
        updateWrapper.set("name","xiaohei1").set("email","980@qq.com");
        userMapper.update(null,updateWrapper);
```

执行时具体sql为

`==>  Preparing: UPDATE user SET name=?,email=? WHERE is_deleted=0 AND (name LIKE ? AND (age > ? OR email IS NULL))`

## 9 组装条件

通过`com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank()`方法

来判断某个字符串是否不为空字符串、不为null、不为空白符


`like(boolean condition, R column, Object val)`

当condition为true时，才会拼接此条件

```java
        String username="";
        Integer ageBegin=20;
        Integer ageEnd=30;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username),"name",username)
                .ge(ageBegin!=null, "age",ageBegin)
                .le(ageEnd!=null,"age",ageEnd);
```

`==>  Preparing: SELECT id,name,age,email,is_deleted FROM user WHERE is_deleted=0 AND (age >= ? AND age <= ?)
==> Parameters: 20(Integer), 30(Integer)`

上例中username为空，可以看到sql中未拼接username字段

## 10 LambdaQuaryWrapper

使用lambda表达式来根据实体类属性获取对应字段名([Lambda表达式详解](siyuan://blocks/20220815122242-4djig4m))


```java
        String username="";
        Integer ageBegin=20;
        Integer ageEnd=30;
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(username),User::getName,username)
                .ge(ageBegin!=null,User::getAge,ageBegin)
                .le(ageEnd!=null,User::getAge,ageEnd);
```

## 11 LambdaUpdateWrapper

# 7、插件

## 1 分页插件

> MybatisPlus自带分页插件，配置后即可使用
>

1. 创建一个配置类，返回MybatisPlusInterceptor ，注册到容器中

    ```java
        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor(){
            MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
            return interceptor;
        }
    ```

2. 使用时传入page对象即可，可选参数有


    ```java
        protected List<T> records;
        protected long total;
        protected long size;    //每页显示条数
        protected long current;    //页数
        protected List<OrderItem> orders;
        protected boolean optimizeCountSql;
        protected boolean searchCount;
        protected boolean optimizeJoinOfCountSql;
        protected String countId;
        protected Long maxLimit;
    ```

    ```java
            Page<User> page = new Page<>(1,3);
            userMapper.selectPage(page,null);
    ```

自定义分页功能

1. 创建方法时设置第一个参数为形参Page<>page
2. 编写xml语句时不需要写分页的sql语句
3. 使用时与上面相同，创建page对象传入后即可

## 2 乐观锁

> **何为乐观锁与悲观锁？**
>
> 答：乐观锁对应于生活中乐观的人总是想着事情往好的方向发展，悲观锁对应于生活中悲观的人总是想着事情往坏的方向发展。这两种人各有优缺点，不能不以场景而定说一种人好于另外一种人。乐观锁和悲观锁是两种思想，用于解决并发场景下的数据竞争问题。
>
>  **乐观锁** ：乐观锁在操作数据时非常乐观，认为别人不会同时修改数据。
>
> 因此乐观锁不会上锁，只是在执行更新的时候判断一下在此期间别人是否修改了数据：如果别人修改了数据则放弃操作，否则执行操作。
>
>  **悲观锁** ：悲观锁在操作数据时比较悲观，认为别人会同时修改数据。
>
> 因此操作数据时直接把数据锁住，直到操作完成后才会释放锁；上锁期间其他人不能修改数据。
>

### 乐观锁实现流程

1. 在配置类中添加拦截器`interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());`

2. 数据库添加version字段

3. 在实体类中将@Version注解添加在版本属性上即可

取出记录时，获取当前version

`SELECT id,name,price,version FROM product WHERE id=1`

更新时，version + 1，如果where语句中的version版本不对，则更新失败

`UPDATE product SET price=price+50, version=version + 1 WHERE id=1 AND version=1`



# 8、通用枚举

> 表中的有些字段值是固定的，例如性别（男或女），此时我们可以使用MyBatis-Plus的通用枚举  
> 来实现
>

1. 创建枚举类

    使用`@EnumValue`注解标记将当前属性插入到数据库相应字段中

    ```java
    @Getter
    public enum SexEnum {
        MALE(1,"男"),
        FEMALE(2,"女");
        @EnumValue
        private Integer sex;
        private String sexName;

        SexEnum(Integer sex, String sexName) {
            this.sex = sex;
            this.sexName = sexName;
        }
    }

    ```

2. 配置

    在yaml文件中扫描通用枚举

    ```yaml
    mybatis-plus:
      type-enums-package: com.atguigu.mybatisplus.enums

    ```

# 9、代码生成器

1. 引入依赖


    ```xml
    		<dependency>
    			<groupId>com.baomidou</groupId>
    			<artifactId>mybatis-plus-generator</artifactId>
    			<version>3.5.1</version>
    		</dependency>
    		<dependency>
    			<groupId>org.freemarker</groupId>
    			<artifactId>freemarker</artifactId>
    			<version>2.3.31</version>
    		</dependency>
    ```
2. 创建生成类，快速直接生成


    ```java
    public class FastAutoGeneratorTest {
        public static void main(String[] args) {
            FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/xhu?characterEncoding=utf-8&userSSL=false", "root", "134161")
                            .globalConfig(builder -> {
                                builder.author("atguigu") // 设置作者
    //.enableSwagger() // 开启 swagger 模式
                                        .fileOverride() // 覆盖已生成文件
                                        .outputDir("D://mybatis_plus"); // 指定输出目录
                            })
                            .packageConfig(builder -> {
                                builder.parent("com.atguigu") // 设置父包名
                                        .moduleName("mybatisplus") // 设置父包模块名
                                        .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://mybatis_plus"));
    // 设置mapperXml生成路径
                            })
                            .strategyConfig(builder -> {
                                builder.addInclude("user") // 设置需要生成的表名
                                        .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                            })
                            .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker
                    //引擎模板，默认的是Velocity引擎模板
                            .execute();
        }

    }
    ```


# 10、多数据源

1. 在yaml配置文件中配置多数据源


    ```yaml
    spring:
    # 配置数据源信息
      datasource:
        dynamic:
      # 设置默认的数据源或者数据源组,默认值即为master
          primary: master
      # 严格匹配数据源,默认false.true未匹配到指定数据源时抛异常,false使用默认数据源
          strict: false
          datasource:
    #        主数据源
            master:
              url: jdbc:mysql://localhost:3306/xhu?characterEncoding=utf-8&useSSL=false
              driver-class-name: com.mysql.jdbc.Driver
              username: root
              password: 134161
    #        从数据源
            slave_1:
              url: jdbc:mysql://localhost:3306/mybatis_plus_1?characterEncoding=utf-8&useSSL=false
              driver-class-name: com.mysql.jdbc.Driver
              username: root
              password: 134161
    ```
2. 在ServiceImpl类中使用@DS注解指定操作的数据源


    ```java
    @DS("master")
    ```

    tips：

    **@DS注解可以加到方法上或者类上，如果加到方法上，分配给查询方法和插入方法不同的数据源，可以实现读写分离**

# 11、MybatisX插件

代码生成器、根据mapper中方法名快速生成xml文件及方法的sql
