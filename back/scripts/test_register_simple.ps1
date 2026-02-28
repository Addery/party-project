# 测试活动报名接口 - 简单版

# 直接使用curl命令测试
curl -X POST http://localhost:8080/api/activities/13/register -H "Content-Type: application/json" -d '{"userId": 1, "name": "test user", "phone": "13800138000"}'