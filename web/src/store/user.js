import axios from 'axios'

export default {
    state: {
        id: '',
        username: '',
        avatar: '',
        token: '',
        is_login: false
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.avatar = user.avatar;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        logout(state) {
            state.id = '';
            state.username = '';
            state.avatar = '';
            state.token = '';
            state.is_login = false;
        }
    },
    actions: {
        login(context, data) {
            axios({
                url: "http://47.97.71.119/api/user/account/token",
                method: "post",
                params: {
                    username: data.username,
                    password: data.password,
                }
            })
            .then(resp => {
                if (resp.data.error_message === "success") {
                    localStorage.setItem("token", resp.data.token);
                    context.commit("updateToken", resp.data.token);
                    data.success(resp.data);
                } else {
                    data.error(resp.data);
                }
            })
            .catch(err => {
                data.error(err);
            });
        },
        getInfo(context,data) {
            axios({
                url: "http://47.97.71.119/api/user/account/info",
                method: "get",
                headers: {
                    "Authorization": "Bearer " + context.state.token
                }
            })
            .then(resp => {
                if (resp.data.error_message === "success") {
                    context.commit("updateUser", {
                        ...resp.data,
                        is_login: true
                    });
                    console.log(resp.data);
                    data.success();
                } else {
                    data.error(resp.data);
                }
            })
            .catch(err => {
                data.error(err);
            });
        },
        logout(context) {
            localStorage.removeItem("token");
            context.commit("logout");
        }
    },
    modules: {
    }
}