<template>
    <PlayGround v-if="$store.state.record.isRecord" />
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'

export default {
    components: {
        PlayGround,
    },
}
</script>

<style scoped>
</style>