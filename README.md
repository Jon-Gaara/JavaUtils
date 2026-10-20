# JavaUtils

沉淀自支付、API 网关等业务线日常开发的通用 Java 工具类库，同时包含一组手写的 JVM 字节码解析与 ASM 生成实践代码。

## 亮点模块

### 🔬 JVM / 字节码

- **手写 Java Class 文件解析器**（`com.jon.bytecode`）
  - 不依赖 ASM，按 JVM 规范逐字节解析：魔数、版本号、常量池（15 种常量类型）、访问标志、字段表、方法表、Code 等属性
  - 参考《Java 虚拟机字节码：从入门到实战》
- **ASM 字节码生成**（`com.jon.bytecode.asm`）
  - `GenerateClass` / `OperationClass`：用 ASM 动态生成、改写类
  - `proxy`：基于 ASM 直接生成代理类字节码，实现 JDK 动态代理
  - `writer`：自定义 ClassWriter / MethodWriter 练习

### 🔐 加密与可搜索加密

- AES / DES / Base64 / MD5 / SHA-1 常用加解密封装
- **可搜索加密**（`SearchEncrypt` / `JDSearchEncrypt`）：加密后的字段仍可用于匹配与排序，适用于敏感数据落库后的等值检索场景

## 能力清单

| 分类 | 包路径 | 内容 |
|------|--------|------|
| 加解密 | `com.jon.encryption` | AES / DES / Base64 / MD5 / SHA-1、可搜索加密 |
| Excel 处理 | `com.jon.poi` | 基于 Apache POI 的 Excel 读取、导出、单元格样式控制 |
| 二维码 | `com.jon.qrcode` | 基于 ZXing 的二维码生成与解码 |
| 文件处理 | `com.jon.file` | 文件拷贝 / 搜索 / 格式转换 |
| 文件压缩 | `com.jon.file.compression` | ZIP（JDK / Ant / Apache Commons 三种实现）、7z |
| XML / 配置 | `com.jon.file.resolve` | DOM4J / JDOM / SAX 解析、Properties、内存映射读文件 |
| 图片处理 | `com.jon.image` | 格式转换、图片压缩（JDK 原生 / Thumbnailator） |
| 网络 | `com.jon.http`、`com.jon.socket` | HTTP 请求封装、Socket 通信 |
| FTP | `com.jon.ftp` | FTP / SFTP 客户端 |
| JSON | `com.jon.json` | Jackson / Json-lib 封装与日期处理 |
| 邮件 | `com.jon.mail` | SSL / 非 SSL 邮件发送 |
| 数据库 | `com.jon.jdbc` | JDBC 工具封装 |
| 正则 | `com.jon.pattern` | 正则匹配工具 |
| Spring | `com.jon.spring` | Spring Bean 获取、代理实例工具 |
| 脚本引擎 | `com.jon.engine` | JSR-223 ScriptEngine 封装 |

## 环境

- Maven 构建（`pom.xml`），JDK 8+

## 说明

- 业务开发中沉淀的工具集合，部分实现以学习和复用为目的，使用前请结合自身场景验证。
- 字节码模块为学习 JVM 规范的实践产物，非生产级字节码框架。
