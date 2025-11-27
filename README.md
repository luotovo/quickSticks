# 🛵 筷点来 · 外卖系统完整实战

> 前后端分离 · 微信登录+支付 · WebSocket 实时推送 · Docker 一键部署  
> 2024.10–2025.01 独立完成 · 2000+ 行真实代码

![动态 Banner](https://socialify.git.ci/luotovo/quickSticks/image?description=1&font=Source%20Code%20Pro&forks=1&issues=1&language=1&name=1&owner=1&pattern=Chart&pulls=1&stargazers=1&theme=Dark)

## ✨ 核心功能

| 模块     | 功能亮点                                                                 |
|----------|--------------------------------------------------------------------------|
| **管理端** | 员工管理 · 菜品/套餐/分类/订单 CRUD · 数据统计大盘                     |
| **用户端** | 微信扫码登录 · 地址簿 · 购物车 · 下单支付 · 订单查询                    |
| **高阶特性** | WebSocket 订单实时提醒 · Redis 缓存+分布式锁防超卖 · 微信支付+退款     |
| **部署运维** | Docker Compose 一键拉起 MySQL + Redis + Nginx                          |

## 🛠 技术栈

| 类别     | 技术选型                                                         |
|----------|------------------------------------------------------------------|
| **后端** | SpringBoot 2.7 · MyBatis-Plus · Redis · WebSocket · JWT · 微信支付 SDK |
| **数据库** | MySQL 8 + Redis 6 (缓存/分布式锁/ZSet)                           |
| **前端** | Vue3 + Element-Plus + ECharts                                    |
| **工具** | Docker + Maven + Git + Linux                                     |

## ▶️ 快速启动（本地 1 分钟跑起来）

```bash
# 1. 克隆仓库
git clone https://github.com/luotovo/quickSticks.git
cd quickSticks

# 2. 一键 Docker 启动（MySQL + Redis + Nginx）
docker-compose up -d

# 3. 启动后端
mvn clean spring-boot:run -pl sky-server

# 4. 访问
管理端 → http://localhost:8080/admin/page/login/login.html
接口文档 → http://localhost:8080/doc.html (Knife4j)



<img width="357" height="762" alt="29a8da0cca1859d121f32c034bd6d3a4" src="https://github.com/user-attachments/assets/0ca48aaf-3d78-4017-a1e1-8580b043a68d" />
<img width="366" height="676" alt="f3079d74d0cb49d74219b68c73d647ff" src="https://github.com/user-attachments/assets/0e84699e-7f2b-445a-92de-2778b8786e71" />
<img width="349" height="760" alt="b490e7f3f9e2ea742c8122956d5de480" src="https://github.com/user-attachments/assets/9dc05d43-838a-453a-b698-dfe3dadad9e4" />

<img width="358" height="731" alt="d9e0fde377bd8603c62fefcbf4ef2284" src="https://github.com/user-attachments/assets/72549da4-49ff-4691-a73d-19b9ea8728ed" />
