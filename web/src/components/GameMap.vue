<template>
    <div ref="parent" class="game-map">
        <canvas ref="canvas" tabindex="0"></canvas>
        <div v-if="is_user_a" class="player-mark-a">你的位置</div>
        <div v-if="is_user_b" class="player-mark-b">你的位置</div>
    </div>
</template>
<script>
import { GameMap } from '@/assets/script/GameMap';
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useStore } from 'vuex';


export default {
    setup() {
        let parent = ref(null);
        let canvas = ref(null);
        const store = useStore();

        onMounted(() => {
            store.commit(
                "updateGameObject", 
                new GameMap(canvas.value.getContext('2d'), parent.value, store));
        });

        onUnmounted(() => {
            const game = store.state.pk.gameObject;
            if (game) {
                game.destroy();
            }
        });

        return {
            parent,
            canvas,
            is_user_a: computed(() => store.state.pk.status === 'playing' && parseInt(store.state.user.id) === parseInt(store.state.pk.a_id)),
            is_user_b: computed(() => store.state.pk.status === 'playing' && parseInt(store.state.user.id) === parseInt(store.state.pk.b_id)),
        }
    }
}
</script>
<style scoped>
.game-map {
    width: 60vw;
    height: 60vh;
    margin: 40px auto;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
}

.player-mark-a {
    position: absolute;
    bottom: 20px;
    left: 20px;
    color: white;
    background-color: #4876EC;
    padding: 5px 10px;
    border-radius: 5px;
    font-weight: bold;
    font-size: 20px;
}

.player-mark-b {
    position: absolute;
    top: 20px;
    right: 20px;
    color: white;
    background-color: #F94848;
    padding: 5px 10px;
    border-radius: 5px;
    font-weight: bold;
    font-size: 20px;
}
</style>