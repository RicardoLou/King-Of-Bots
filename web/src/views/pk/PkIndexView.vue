<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.winner != 'none'" />
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import ResultBoard from '../../components/ResultBoard.vue'
import { onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => {
            store.commit("updateOpponentInfo", {
                opponent_username: "我的对手",
                opponent_avatar: "https://big-event-ricardo.oss-cn-hangzhou.aliyuncs.com/38BEBC15045C433F99DC1FE26A212B38-6-2.png",
            })
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected!");
                store.commit("updatePkSocket", socket);
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === "matchSuccess") {  // 匹配成功
                    store.commit("updateOpponentInfo", {
                        opponent_username: data.opponent_username,
                        opponent_avatar: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updatePkStatus", "playing");
                    }, 200);
                    store.commit("updateGame", data.game);
                } else if (data.event === "move") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    if (!game) return;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if (data.event === "result") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    if (data.winner === 'all' || data.winner === 'A') {
                        snake1.status = "die";
                    }
                    if (data.winner === 'all' || data.winner === 'B') {
                        snake0.status = "die";
                    }
                    store.commit("updateWinner", data.winner);
                }
            }

            socket.onclose = () => {
                console.log("disconnected!");
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
