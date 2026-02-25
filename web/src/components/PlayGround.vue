<template>
    <div class="play-ground">
        <GameMap/>
    </div>
</template>

<script>
import GameMap from '@/components/GameMap.vue'

export default {
    name: 'PlayGround',
    components: {
        GameMap
    }
}
</script>

<style scoped>
.play-ground {
    width: 70vw;
    height: 60vh;
    margin: 40px auto;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
}
</style>