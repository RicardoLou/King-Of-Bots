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
        <ul class="navbar-nav me-3 mb-0">
        <li class="nav-item dropdown">
          <router-link :class="currentPath == '/user' ? 'nav-link active dropdown-toggle' : 'nav-link dropdown-toggle'" to="/user" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            Ricardo
          </router-link>
          <ul class="dropdown-menu">
            <li><router-link class="dropdown-item" to="/user">我的Bots</router-link></li>
            <li><router-link class="dropdown-item" to="/logout">退出</router-link></li>
          </ul>
        </li>
        </ul>
    </div>
  </div>
</nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';

export default {
    name: 'NavBar',
    setup() {
        const route = useRoute();
        const currentPath = computed(() => route.path);
        
        return {
            currentPath
        }
    }
}
</script>

<style scoped>
</style>