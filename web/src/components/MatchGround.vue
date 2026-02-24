<template>
    <div class="match-ground">
        <div class="row">
            <div class="col-md-4">
                <div class="user-avatar"><img :src="$store.state.user.avatar" alt="" ></div>
                <hr>
                <div style="text-align: center; font-weight: bold; font-size: 150%; color: white; padding-top: 10px;">{{ $store.state.user.username }}</div>
            </div>
            <div class="col-md-4">
                <div class="user-select-bot">
                    <select v-model="selected_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>亲自出征</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.botName }}</option>
                    </select>
                </div>
            </div>
            <div class="col-md-4">
                <div class="user-avatar"><img :src="$store.state.pk.opponent_avatar" alt="" ></div>
                <hr>
                <div style="text-align: center; font-weight: bold; font-size: 150%; color: white; padding-top: 10px;">{{ $store.state.pk.opponent_username }}</div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12" style="text-align: center; padding-top: 20px;">
                <div style="text-align: center; font-weight: bold; font-size: 200%; color: white; padding-top: 20px;">等待敌人加入...</div>
                <button @click="click_matchButton" type="button" class="btn btn-warning btn-lg" style="margin-top: 10px;">{{ matchButton }}</button>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import axios from 'axios';
export default {
    name: 'MatchGround',
    components: {
    },
    setup() {
        let matchButton = ref("开始匹配");
        const store = useStore();
        let bots = ref([]);
        let selected_bot = ref("-1");
        
        const click_matchButton = () => {
            if (matchButton.value === "开始匹配") {
                matchButton.value = "匹配中....";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start_match",
                    bot_id: selected_bot.value,
                }));
            } else {
                matchButton.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop_match",
                }));
            }
            console.log(selected_bot.value);
        };
        
        const refresh_bots = () => {
            axios({
                url: "http://127.0.0.1:3000/user/bot/botList",
                method: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            }).then(resp => {
                console.log(resp);
                bots.value = resp.data;
            })
        };
        refresh_bots();
        return {
            matchButton,
            click_matchButton,
            bots,
            refresh_bots,
            selected_bot,
        }
    },
}
</script>

<style scoped>
.match-ground {
    width: 70vw;
    height: 60vh;
    margin: 40px auto;
    background-color: rgba(50, 50, 50, 0.5);
}
div.user-avatar {
    text-align: center;
    padding-top: 100px;
}
div.user-avatar > img {
    border-radius: 50%;
    height: 20vh;
    width: 20vh;
}
div.user-select-bot {
    padding-top: 25vh;
    width: 60%;
    margin: 0 auto;
}


</style>