<template>
        <ContentField>
            <div class="row justify-content-md-center">
                <div class="col-3">
                    <form @submit.prevent="login">
                        <div class="mb-3">
                            <label for="exampleInputEmail1" class="form-label">用户名</label>
                            <input v-model="username" type="email" class="form-control" id="Username" aria-describedby="emailHelp" placeholder="请输入用户名">
                        </div>
                        <div class="mb-3">
                            <label for="exampleInputPassword1" class="form-label">密码</label>
                            <input v-model="password" type="password" class="form-control" id="Password" placeholder="请输入密码">
                        </div>
                        <div class="mb-3">
                            <label for="exampleInputPassword1" class="form-label">确认密码</label>
                            <input v-model="confirm_password" type="password" class="form-control" id="ConfirmPassword" placeholder="请确认密码">
                        </div>
                        <div class="error_message">{{ error_message }}</div>
                        <button @click="register" type="submit" class="btn btn-primary" id="Submit">注册</button>
                        </form>
                </div>
            </div>
        </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue'
import { ref } from 'vue'
import router from '@/router';
import axios from 'axios'

export default {
    components: {
        ContentField
    },
    setup() {
        const username = ref('');
        const password = ref('');
        const confirm_password = ref('');
        const error_message = ref('');

        const register = () => {
            console.log(username.value, password.value, confirm_password.value);
            axios({
                url: 'http://47.97.71.119/api/user/account/register',
                method: 'post', 
                params: {
                    username: username.value,
                    password: password.value,
                    confirmedPassword: confirm_password.value
                }
                }).then((res) => {
                    if (res.data.error_message === 'success') {
                        router.push({ name: 'login_view', query: { username: username.value } });
                    } else {
                        console.log(res);
                        error_message.value = res.data.error_message;
                    }
                });
        }
        return {
            username,
            password,
            confirm_password,
            error_message,
            register
        }
    }
}
</script>

<style scoped>
button {
    width: 100%;
}
div.error_message {
    color: red;
}
</style>