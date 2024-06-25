import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://localhost:3001',  // 修改为 Nginx 配置中对应的路径
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default instance;
