

# 筷点来 · 外卖系统完整实战

> 前后端分离 · 微信登录+支付 · WebSocket 实时推送 · Docker 一键部署  
> 2024.10–2025.01 独立完成 · 2000+ 行真实代码

![banner](https://socialify.git.ci/luotovo/quickSticks/image?description=1&font=Inter&forks=1&issues=1&language=1&name=1&owner=1&pattern=Floating%20Cubes&pulls=1&stargazers=1&theme=Auto)

## ✨ 核心功能

| 模块         | 功能亮点                                                                 |
|--------------|--------------------------------------------------------------------------|
| 管理端       | 员工登录、菜品/套餐/分类/订单全 CRUD、数据统计大盘                      |
| 用户端       | 微信扫码登录、地址簿、购物车、下单、订单状态实时推送                   |
| 高阶特性     | WebSocket 订单实时提醒 · Redis 缓存+分布式锁防超卖 · 微信支付+退款     |
| 部署运维     | Docker Compose 一键拉起 MySQL + Redis + Nginx                          |

## 🛠 技术栈

| 类别       | 技术选型                                                         |
|------------|------------------------------------------------------------------|
| 后端       | SpringBoot 2.7 · MyBatis-Plus · Redis · WebSocket · JWT · 微信支付SDK |
| 数据库     | MySQL 8 + Redis 6（缓存、分布式锁、ZSet 排行榜）                 |
| 前端       | Vue3 + Element-Plus + ECharts                                    |
| 部署工具   | Docker + Docker Compose + Maven + Git                            |

## ▶️ 快速启动（本地 30 秒跑起来）

```bash
# 1. 克隆
git clone https://github.com/luotovo/quickSticks.git
cd quickSticks

# 2. 一键启动数据库和中间件（已包含 MySQL8 + Redis）
docker-compose up -d

# 3. 启动后端（sky-server 模块）
cd sky-server
mvn spring-boot:run

# 4. 访问
管理端      → http://localhost:8080/admin/page/login/login.html
接口文档    → http://localhost:8080/doc.html （Knife4j）



quickSticks/
├─ sky-common      ← 公共常量、异常、Result 封装
├─ sky-pojo        ← 实体类
├─ sky-server      ← 主启动模块（Controller/Service/Mapper）
├─ docker-compose.yml
└─ README.md


部分运行图片

<div align="center">
  <img src="https://github.com/user-attachments/assets/436a20d2-b46f-4f6e-a908-5fee9b4e2bba" width="48%" alt="用户端下单"/>
  <img src="https://github.com/user-attachments/assets/d58e810a-8271-4084-8115-6341b77c64a9" width "48%" alt="管理端订单"/>
  <img src="https://github.com/user-attachments/assets/183e8a42-14d7-4ad9-83d9-95ed3f5de588" width="48%" alt="购物车"/>
  <img src="https://github.com/user-attachments/assets/186d122b-da89-4228-bcc1-e66bb0b22586" width="48%" alt="数据统计"/>
</div>

