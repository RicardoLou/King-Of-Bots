<template>
    <div class="match-ground">
        <div class="row">
            <div class="col-md-6">
                <div class="user-avatar"><img :src="$store.state.user.avatar" alt="" ></div>
                <hr>
                <div style="text-align: center; font-weight: bold; font-size: 150%; color: white; padding-top: 10px;">{{ $store.state.user.username }}</div>
            </div>
            <div class="col-md-6">
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
export default {
    name: 'MatchGround',
    components: {
    },
    setup() {
        let matchButton = ref("开始匹配");
        const store = useStore();
        
        const click_matchButton = () => {
            if (matchButton.value === "开始匹配") {
                matchButton.value = "匹配中....";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start_match",
                }));
            } else {
                matchButton.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop_match",
                }));
            }
        }
        return {
            matchButton,
            click_matchButton,
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

</style>