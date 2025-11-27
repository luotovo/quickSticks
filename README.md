筷点来实战项目

前后端分离外卖系统（管理端 + 用户端），基于 SpringBoot 实现微信登录、支付和实时推送。

## 功能模块
- 管理端：员工管理、菜品/套餐 CRUD、订单管理、分类管理
- 用户端：微信登录、地址簿、购物车、下单支付、订单查询
- 核心特性：WebSocket 订单实时推送、Redis 缓存热门菜品、微信支付/退款
- 部署：Docker Compose 一键启动 MySQL + Redis + Nginx

## 技术栈
- 后端：SpringBoot 2.7 + MyBatis-Plus + Redis + WebSocket + JWT
- 前端：Vue3 + Element-Plus
- 工具：Docker、Maven、Git

## 快速启动
1. 克隆仓库：`git clone https://github.com/luotovo/quickSticks.git`
2. 进入目录：`cd quickSticks`
3. 启动 Docker：`docker-compose up -d`
4. 访问管理端：http://localhost:8080
5. 接口文档：http://localhost:8080/doc.html (Knife4j)

## 项目结构
- quick-common：公共常量、异常处理
- quick-pojo：实体类
- quick-server：核心服务（控制器、服务层、Mapper）
- <img width="347" height="752" alt="62501b52cf8dc80378af0fc149e94677" src="https://github.com/user-attachments/assets/436a20d2-b46f-4f6e-aa00-5fe99b4e28ba" />
<img width="358" height="731" alt="d9e0fde377bd8603c62fefcbf4ef2284" src="https://github.com/user-attachments/assets/dd5e810a-8271-4084-8115-63411b7ec469" />
<img width="366" height="676" alt="f3079d74d0cb49d74219b68c73d647ff" src="https://github.com/user-attachments/assets/183e8a42-14d7-4ad9-83b9-956d3f5de585" />
<img width="349" height="760" alt="b490e7f3f9e2ea742c8122956d5de480" src="https://github.com/user-attachments/assets/186d12db-da89-4228-b0c1-656bb8b22586" />
<img width="357" height="762" alt="29a8da0cca1859d121f32c034bd6d3a4" src="https://github.com/user-attachments/assets/18967981-542f-42cb-a5fb-b9dbfe711748" />
