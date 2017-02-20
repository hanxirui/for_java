### 项目介绍
一个后台开发的基础项目,采用SpringMvc + mybatis + mysql + maven.
- 其设计目的在于提高开发效率，避免做重复的工作。尤其是在做管理后台时，能减少许多代码量。
- 如果您熟悉SpringMvc,那么上手会非常快,在使用过程中您会发现,其实这还是SpringMvc.
- 如果您比较"懒惰",喜欢用少量的代码完成更多的事情,如果您崇尚write less,do more.或许,这个项目适合您.

> **项目特色：**

> - 少量代码完成对一张表的增删改查;
> - 动态生成查询条件;
> - 数据校验,支持JSR-303;
> - 支持QBC查询;
> - 封装了分页信息,支持不规则翻页;
> - 数据导出,数据校验;
> - 返回指定格式的JSON数据;
> - 支持easyui,extjs的参数接收;
> - 异常处理;
> - 使用代码进行多表关联查询
> - ...

前端采用BUI(http://www.builive.com/index.php)

----------
简单例子
----------
Controller完成对学生表的增删改查

controller类
```
// 继承CrudController,表示该Controller具有增删改查功能
// 增删改查功能不用自己实现,全部都封装好了,我们关注业务代码即可.
@Controller
public class StudentCrudController extends
		CrudController<Student, StudentService> {

	@RequestMapping("/addStudent.do")
	public ModelAndView addStudent(Student student) {
		return this.add(student);
	}

	@RequestMapping("/listStudent.do")
	public ModelAndView listStudent(SearchStudentEntity searchStudentEntity) {
		return this.list(searchStudentEntity);
	}

	@RequestMapping("/updateStudent.do")
	public ModelAndView updateStudent(Student student) {
		return this.modify(student);
	}

	// 传一个id值即可,根据主键删除
	@RequestMapping("/delStudent.do")
	public ModelAndView delStudent(Student student) {
		return this.remove(student);
	}

}
```

Service类
```
// 只需简单继承无需其它代码
@Service
public class StudentService extends CrudService<Student, StudentDao> {}
```

Dao层
```

// 只需简单继承无需其它代码
public interface StudentDao extends BaseDao<Student> {}
```
这样,一个完整的增删改查功能就写好了.包含条件查询,分页查询等功能,如果配合代码生成器那是分分钟的事情.

----------
BaseDao.java方法列表
----------

```

// 根据对象查询,可以传主键值,也可以传整个对象
Entity get(Object id);
// 根据条件查找单条记录
Entity getByExpression(ExpressionQuery query);
// 条件查询
List<Entity> find(ExpressionQuery query);
// 查询总记录数
int findTotalCount(ExpressionQuery query);
// 新增,新增所有字段
void save(Entity entity);
// 新增（忽略空数据）
void saveNotNull(Entity entity);
// 修改,修改所有字段
void update(Entity entity);
// 根据条件更新所有字段
void updateByExpression(@Param("entity")Entity entity,@Param("query")ExpressionQuery query);
// 根据主键更新不为null的字段
void updateNotNull(Entity entity);
// 根据条件更新不为null的字段
void updateNotNullByExpression(@Param("entity")Entity entity,@Param("query")ExpressionQuery query);
// 删除
void del(Entity entity);
// 根据条件删除
void delByExpression(ExpressionQuery query);
```


### 版本大更新
添加了权限管理系统,采用RBAC模型实现(RBAC模型详见:[RBAC模型](http://blog.csdn.net/painsonline/article/details/7183613/))

- 根据角色来划分权限,权限定义到数据级别;
- 不同的人登录将看到不同的菜单及页面内容;
- 实现了URL权限判断拦截处理.
- 操作简单,可以从角色设置权限,也可以从权限指定到角色.

### 界面预览
用户管理:

![输入图片说明](http://git.oschina.net/uploads/images/2016/0429/171348_afa56e78_332975.png "在这里输入图片标题")

设置用户角色:

![输入图片说明](http://git.oschina.net/uploads/images/2016/0429/171404_77cdd2de_332975.png "在这里输入图片标题")

角色管理:

![输入图片说明](http://git.oschina.net/uploads/images/2016/0429/171415_3578ccd0_332975.png "在这里输入图片标题")

设置角色权限:

![输入图片说明](http://git.oschina.net/uploads/images/2016/0429/171423_001670d7_332975.png "在这里输入图片标题")

资源(菜单)管理:

![输入图片说明](http://git.oschina.net/uploads/images/2016/0429/171430_a402ada9_332975.png "在这里输入图片标题")

用户组管理:

![输入图片说明](http://git.oschina.net/uploads/images/2016/0429/171438_8fa81671_332975.png "在这里输入图片标题")

添加用户组成员:

![输入图片说明](http://git.oschina.net/uploads/images/2016/0429/171446_55bd34e0_332975.png "在这里输入图片标题")

被分配了订单查询权限的用户界面:

![输入图片说明](http://git.oschina.net/uploads/images/2016/0429/171455_99d8f4c0_332975.png "在这里输入图片标题")

### 项目部署
1. 下载项目,进入项目根目录,运行mvn eclipse:eclipse,然后导入到eclipse中;
2. 导入数据(Mysql),运行database_demo.sql即可,数据库连接配置在config.properties中;
3. 启动tomcat或者输入maven命令mvn jetty:run (默认端口8088),浏览器输入http://localhost:your_port/your_project_name/index.jsp
4. app.controller.demo包下含有更多例子,请自行体验.

超级管理员用户名:admin 密码:123456
其它用户密码均为123456

QQ交流群:328180219

[代码生成器](https://git.oschina.net/durcframework/autoCode)