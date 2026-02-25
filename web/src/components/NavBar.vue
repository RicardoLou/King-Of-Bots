<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <a class="navbar-brand" href="/">
      <img src="../assets/images/snake.png" alt="Bootstrap" width="30" height="30">
    </a>
    <router-link class="navbar-brand" to="/">King Of Bots</router-link>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link :class="currentPath == '/pk' ? 'nav-link active' : 'nav-link'" to="/pk">对战</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="currentPath == '/record' ? 'nav-link active' : 'nav-link'" to="/record">对局列表</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="currentPath == '/ranklist' ? 'nav-link active' : 'nav-link'" to="/ranklist">天梯排行</router-link>
        </li>
        <li class="nav-item">
          <span style="margin-left: 15px;">
            <button type="button" class="btn btn-secondary"
                    data-bs-toggle="tooltip"
                    data-bs-placement="top"
                    data-bs-custom-class="my-custom-tooltip"
                    title="WASD控制移动, 以最后一次操作为准, 每次移动需双方均给出操作">
              How to play?
            </button>
          </span>
        </li>
      </ul>
        <ul class="navbar-nav me-3 mb-0" v-if="$store.state.user.is_login">
        <li class="nav-item dropdown">
          <a class = 'nav-link active dropdown-toggle' href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ $store.state.user.username }}
          </a>
          <ul class="dropdown-menu">
            <li><router-link class="dropdown-item" to="/user/bot/index">个人中心</router-link></li>
            <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
          </ul>
        </li>
        </ul>
        <ul class="navbar-nav me-3 mb-0" v-else>
        <li class="nav-item dropdown">
          <router-link to="/user/accout/login" :class="currentPath == '/ranklist' ? 'nav-link active' : 'nav-link'">
            登录
          </router-link>
        </li>
        <li class="nav-item dropdown">
          <router-link to="/user/accout/register" :class="currentPath == '/user/accout/register' ? 'nav-link active' : 'nav-link'">
            注册
          </router-link>
        </li>
        </ul>
    </div>
  </div>
</nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed, onMounted, onUnmounted } from 'vue'; // 1. 引入生命周期钩子
import { useStore } from 'vuex';
import router from '@/router/index.js';
import { Tooltip } from 'bootstrap'; // 2. 引入 Bootstrap 的 Tooltip 插件

export default {
    name: 'NavBar',
    setup() {
        const store = useStore();
        const route = useRoute();
        const currentPath = computed(() => route.path);

        // 3. 在组件挂载后初始化 Tooltip
        onMounted(() => {
            const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
            const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new Tooltip(tooltipTriggerEl))
            
            // 将实例存起来，方便销毁（可选）
            window.myTooltips = tooltipList; 
        });

        // 4. 在组件卸载前销毁（防止在 SPA 中跳转页面后产生残留）
        onUnmounted(() => {
            if (window.myTooltips) {
                window.myTooltips.forEach(t => t.dispose());
            }
        });

        const logout = () => {
            store.dispatch('logout');
            router.push({ name: 'login_view' });
        }

        return {
            currentPath,
            logout
        }
    }
}
</script>

<style>
.my-custom-tooltip {
  --bs-tooltip-bg: var(--bd-violet-bg);
  --bs-tooltip-color: var(--bs-white);
}
</style>