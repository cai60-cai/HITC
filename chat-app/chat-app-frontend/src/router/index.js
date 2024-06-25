import Vue from 'vue';
import Router from 'vue-router';
import LoginPage from '../pages/Login.vue';
import MainPage from '../pages/MainPage.vue';

Vue.use(Router);

export default new Router({
    mode: 'hash',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'Login',
            component: LoginPage
        },
        {
            path: '/main',
            name: 'MainPage',
            component: MainPage
        }
    ]
});
