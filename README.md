# 筷点来 完整实战项目

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

部分运行图片<img width="357" height="762" alt="29a8da0cca1859d121f32c034bd6d3a4" src="https://github.com/user-attachments/assets/77f9d9b9-f46e-4d0d-9058-b4303dfa7d9d" />
<img width="366" height="676" alt="f3079d74d0cb49d74219b68c73d647ff" src="https://github.com/user-attachments/assets/5ac0aab9-3af1-4d70-acef-cd7b9bb51225" />
<img width="349" height="760" alt="b490e7f3f9e2ea742c8122956d5de480" src="https://github.com/user-attachments/assets/8e9397c5-b771-4b<img width="358" height="731" alt="d9e0fde377bd8603c62fefcbf4ef2284" src="https://github.com/user-attachments/assets/d2c64fe3-5224-451e-ac0d-424ccba873f5" />
ab-9fdd-e13e2f93c1c4" />
<img width="347" height="752" alt="62501b52cf8dc80378af0fc149e94677" src="https://github.com/user-attachments/assets/15ca1211-9625-4516-bc0f-c99e834a5ff6" />

