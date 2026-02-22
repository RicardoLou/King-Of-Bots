<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.winner === 'all'">
            Win
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.winner === 'A' && $store.state.pk.a_id === parseInt($store.state.user.id)">
            Win
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.winner === 'B' && $store.state.pk.b_id === parseInt($store.state.user.id)">
            Win
        </div>
        <div class="result-board-text" v-else>
            Lose
        </div>
        <div class="result-board-btn">
            <button @click="restart" type="button" class="btn btn-warning btn-lg">
                再来!
            </button>
        </div>
    </div>    
</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        const restart = () => {
            store.commit("updatePkStatus", "matching");
            store.commit("updateWinner", "none");
            store.commit("updateOpponentInfo", {
                opponent_username: "我的对手",
                opponent_avatar: "https://big-event-ricardo.oss-cn-hangzhou.aliyuncs.com/38BEBC15045C433F99DC1FE26A212B38-6-2.png",
            })
        }

        return {
            restart
        };
    }
}
</script>

<style scoped>
div.result-board {
    height: 30vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.5);
    position: absolute;
    top: 30vh;
    left: 35vw;
}
div.result-board-text {
    text-align: center;
    color: white;
    font-size: 50px;
    font-weight: 600;
    font-style: italic;
    padding-top: 5vh;
}

div.result-board-btn {
    padding-top: 7vh;
    text-align: center;
}
</style>
