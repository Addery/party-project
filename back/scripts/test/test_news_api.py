import requests
import json

# 测试推荐新闻API
url = 'http://localhost:8080/api/home/recommended'
try:
    response = requests.get(url)
    response.raise_for_status()  # 检查HTTP请求是否成功
    
    print(f'HTTP状态码: {response.status_code}')
    print(f'响应头: {response.headers}')
    print()
    
    data = response.json()
    print(f'完整响应数据: {json.dumps(data, indent=2, ensure_ascii=False)}')
    print()
    
    news_count = len(data.get('data', {}).get('news', []))
    print(f'推荐新闻数量: {news_count}')
    
    # 打印前几条新闻的标题
    print('前几条新闻标题:')
    for i, news in enumerate(data.get('data', {}).get('news', [])[:5]):
        print(f'{i+1}. {news.get("title", "")}')
        print(f'   发布日期: {news.get("publishDate", "")}')
        print()
        
except requests.exceptions.RequestException as e:
    print(f'请求错误: {e}')
except json.JSONDecodeError as e:
    print(f'JSON解析错误: {e}')
    print(f'响应内容: {response.text}')