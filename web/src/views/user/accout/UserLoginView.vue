<template>
        <ContentField v-if="show_content">
            <div class="row justify-content-md-center">
                <div class="col-3">
                    <form @submit.prevent="login">
                        <div class="mb-3">
                            <label for="exampleInputEmail1" class="form-label">用户名</label>
                            <input v-model="username" type="email" class="form-control" id="Username" aria-describedby="emailHelp" placeholder="请输入用户名">
                            <div id="emailHelp" class="form-text">We'll never share your account with anyone else.</div>
                        </div>
                        <div class="mb-3">
                            <label for="exampleInputPassword1" class="form-label">密码</label>
                            <input v-model="password" type="password" class="form-control" id="Password" placeholder="请输入密码">
                        </div>
                        <div class="error_message">{{ error_message }}</div>
                        <button @click="login" type="submit" class="btn btn-primary" id="Submit">登录</button>
                        </form>
                </div>
            </div>
        </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue'
import { useStore } from 'vuex';
import { ref } from 'vue';
import  router from '@/router/index.js';
import { useRoute } from 'vue-router';

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        const route = useRoute();
        let username = ref(route.query.username || '');
        let password = ref('');
        let error_message = ref('');
        let show_content = ref(false);

        const token = localStorage.getItem("token");
        if (token) {
            store.commit("updateToken", token);
            store.dispatch('getInfo', {
                success: () => {
                    router.push('/pk');
                },
                error: () => {
                    show_content.value = true;
                    error_message.value = "Token 失效 请重新登录";
                }
            });
        } else {
            show_content.value = true;
        }

        const login = () => {
            store.dispatch('login', {
                username: username.value,
                password: password.value,
                success: () => {
                    store.dispatch('getInfo', {
                        success: () => {
                            router.push('/pk');
                        },
                        error: () => {
                            error_message.value = "获取用户信息失败";
                        }
                    });
                },
                error: () => {
                    error_message.value = "用户名或密码错误";
                }
            });
        };

        return {
            username,
            password,
            error_message,
            show_content,
            login
        }
    }
}
</script>

<style scoped>
#Submit {
    width: 100%;
}
.error_message {
    color: red;
}
</style>