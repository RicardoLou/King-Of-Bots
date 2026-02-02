<template>
        <ContentField>
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

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');
        
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