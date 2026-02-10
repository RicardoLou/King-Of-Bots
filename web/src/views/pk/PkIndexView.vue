<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>

<script>
import PlayGround from '@/components/PlayGround.vue'
import MatchGround from '@/components/MatchGround.vue'
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    name: 'PkIndexView',
    components: {
        PlayGround,
        MatchGround
    },
    setup() {
        const store = useStore();

        let socket = null;
        let socketURL = "ws://127.0.0.1:3000/websocket/" + store.state.user.token + "/";

        onMounted(() => {
            store.commit("updateOpponentInfo", {
                opponent_username: "未知的敌人",
                opponent_avatar: "https://big-event-ricardo.oss-cn-hangzhou.aliyuncs.com/38BEBC15045C433F99DC1FE26A212B38-6-2.png",
            });
            socket = new WebSocket(socketURL);
            
            socket.onopen = () => {
                console.log("connected");
                store.commit("updatePkSocket", socket);
            }
            socket.onmessage = msg => {
                let data = JSON.parse(msg.data);
                if (data.event === "matchSuccess") {
                    store.commit("updateOpponentInfo", {
                        opponent_username: data.opponent_username,
                        opponent_avatar: data.opponent_avatar,
                    });
                    setTimeout(() => {
                        store.commit("updatePkStatus", "playing");
                    }, 2000);
                    store.commit("updateGameMap", data.gameMap);
                }
                console.log(data);
            }
            socket.onclose = () => {
                console.log("disconnected");
            }
        });
        
        onUnmounted(() => {
            socket.close();
            store.commit("updatePkStatus", "matching");
        })
    }
}
</script>

<style scoped>

</style>