<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
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
      </ul>
        <ul class="navbar-nav me-3 mb-0" v-if="$store.state.user.is_login">
        <li class="nav-item dropdown">
          <a class = 'nav-link active dropdown-toggle' href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ $store.state.user.username }}
          </a>
          <ul class="dropdown-menu">
            <li><router-link class="dropdown-item" to="/user">我的Bots</router-link></li>
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
import { computed } from 'vue';
import { useStore } from 'vuex';
import router from '@/router/index.js';

export default {
    name: 'NavBar',
    setup() {
        const store = useStore();
        const route = useRoute();
        const currentPath = computed(() => route.path);

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

<style scoped>
</style>