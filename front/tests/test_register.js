// 测试注册功能的脚本
import http from 'node:http';

// 发送POST请求的函数
function sendPostRequest(path, data) {
  return new Promise((resolve, reject) => {
    const options = {
      hostname: 'localhost',
      port: 8080,
      path: path,
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    };

    const req = http.request(options, (res) => {
      let data = '';
      res.on('data', (chunk) => {
        data += chunk;
      });
      res.on('end', () => {
        resolve({ statusCode: res.statusCode, body: JSON.parse(data) });
      });
    });

    req.on('error', (error) => {
      reject(error);
    });

    req.write(JSON.stringify(data));
    req.end();
  });
}

// 测试用例
async function runTests() {
  console.log('开始测试注册功能...');
  
  // 测试用例1：白名单内的手机号+身份证号组合注册（应该成功）
  console.log('\n测试用例1：白名单内的手机号+身份证号组合注册');
  try {
    const response = await sendPostRequest('/api/auth/register', {
      username: 'testuser123',
      password: 'TestPass123!',
      name: '测试用户',
      gender: '男',
      age: 28,
      partyStatus: '正式党员',
      branch: '第一党支部',
      phone: '13800138001',
      email: 'test@example.com',
      idCard: '110101199001011234',
      joinDate: '2018-06-30',
      position: '科员',
      education: '本科',
      workYears: 5,
      avatar: '',
      role: 'member'
    });
    console.log(`状态码: ${response.statusCode}`);
    console.log(`响应: ${JSON.stringify(response.body, null, 2)}`);
  } catch (error) {
    console.error(`测试失败: ${error.message}`);
  }

  // 测试用例2：白名单外的手机号+身份证号组合注册（应该失败）
  console.log('\n测试用例2：白名单外的手机号+身份证号组合注册');
  try {
    const response = await sendPostRequest('/api/auth/register', {
      username: 'testuser456',
      password: 'TestPass123!',
      name: '测试用户2',
      gender: '男',
      age: 30,
      partyStatus: '正式党员',
      branch: '第一党支部',
      phone: '13800138006',
      email: 'test2@example.com',
      idCard: '110101199001011239',
      joinDate: '2018-06-30',
      position: '科员',
      education: '本科',
      workYears: 5,
      avatar: '',
      role: 'member'
    });
    console.log(`状态码: ${response.statusCode}`);
    console.log(`响应: ${JSON.stringify(response.body, null, 2)}`);
  } catch (error) {
    console.error(`测试失败: ${error.message}`);
  }

  // 测试用例3：已存在的用户名注册（应该失败）
  console.log('\n测试用例3：已存在的用户名注册');
  try {
    const response = await sendPostRequest('/api/auth/register', {
      username: 'testuser123',
      password: 'TestPass123!',
      name: '测试用户3',
      gender: '男',
      age: 32,
      partyStatus: '正式党员',
      branch: '第一党支部',
      phone: '13800138002',
      email: 'test3@example.com',
      idCard: '110101199001011235',
      joinDate: '2018-06-30',
      position: '科员',
      education: '本科',
      workYears: 5,
      avatar: '',
      role: 'member'
    });
    console.log(`状态码: ${response.statusCode}`);
    console.log(`响应: ${JSON.stringify(response.body, null, 2)}`);
  } catch (error) {
    console.error(`测试失败: ${error.message}`);
  }

  // 测试用例4：已存在的手机号注册（应该失败）
  console.log('\n测试用例4：已存在的手机号注册');
  try {
    const response = await sendPostRequest('/api/auth/register', {
      username: 'testuser789',
      password: 'TestPass123!',
      name: '测试用户4',
      gender: '男',
      age: 35,
      partyStatus: '正式党员',
      branch: '第一党支部',
      phone: '13800138001',
      email: 'test4@example.com',
      idCard: '110101199001011235',
      joinDate: '2018-06-30',
      position: '科员',
      education: '本科',
      workYears: 5,
      avatar: '',
      role: 'member'
    });
    console.log(`状态码: ${response.statusCode}`);
    console.log(`响应: ${JSON.stringify(response.body, null, 2)}`);
  } catch (error) {
    console.error(`测试失败: ${error.message}`);
  }

  // 测试用例5：不符合密码规则的注册（应该在前端失败）
  console.log('\n测试用例5：不符合密码规则的注册');
  try {
    const response = await sendPostRequest('/api/auth/register', {
      username: 'testuser101',
      password: '123456',
      name: '测试用户5',
      gender: '男',
      age: 28,
      partyStatus: '正式党员',
      branch: '第一党支部',
      phone: '13800138003',
      email: 'test5@example.com',
      idCard: '110101199001011236',
      joinDate: '2018-06-30',
      position: '科员',
      education: '本科',
      workYears: 5,
      avatar: '',
      role: 'member'
    });
    console.log(`状态码: ${response.statusCode}`);
    console.log(`响应: ${JSON.stringify(response.body, null, 2)}`);
  } catch (error) {
    console.error(`测试失败: ${error.message}`);
  }

  // 测试用例6：缺少必填字段的注册（应该失败）
  console.log('\n测试用例6：缺少必填字段的注册');
  try {
    const response = await sendPostRequest('/api/auth/register', {
      username: 'testuser102',
      password: 'TestPass123!',
      name: '测试用户6',
      gender: '男',
      age: 28,
      partyStatus: '正式党员',
      branch: '第一党支部',
      // 缺少phone和idCard
      email: 'test6@example.com',
      joinDate: '2018-06-30',
      position: '科员',
      education: '本科',
      workYears: 5,
      avatar: '',
      role: 'member'
    });
    console.log(`状态码: ${response.statusCode}`);
    console.log(`响应: ${JSON.stringify(response.body, null, 2)}`);
  } catch (error) {
    console.error(`测试失败: ${error.message}`);
  }
}

// 运行测试
runTests().catch(error => {
  console.error('测试过程中发生错误:', error);
});
