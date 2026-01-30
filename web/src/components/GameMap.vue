<template>
    <div ref="parent" class="game-map">
        <canvas ref="canvas"></canvas>
    </div>
</template>
<script>
import { GameMap } from '@/assets/script/GameMap';
import { ref, onMounted } from 'vue';


export default {
    setup() {
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(() => {
            new GameMap(canvas.value.getContext('2d'), parent.value);
        });

        return {
            parent,
            canvas
        }
    }
}
</script>
<style scoped>
.game-map {
    width: 60vw;
    height: 60vh;
    margin: 40px auto;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>