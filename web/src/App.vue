<template>
  <div>
    <div>Bot name: {{ bot_name }}</div>
  </div>
  <router-view/>
</template>

<script>
import $ from 'jquery';
import { ref } from 'vue';

export default {
  name: "APP",
  setup: () => {
    let bot_name = ref("");

    $.ajax({
      url: "http://localhost:8080/pk/getBotInfo/",
      type: "get",
      success: resp => {
        bot_name.value = resp
      }
    })
    return {
      bot_name
    }
  }
}
</script>
<style>
body {
  background-image: url(assets/background.jpg);
  background-size: cover;
  
}
</style>
