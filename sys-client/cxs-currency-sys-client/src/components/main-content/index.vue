<template>
  <div class="content-all">
    <router-view>

    </router-view>
  </div>
</template>

<script>
export default {
  name: "main-content",
  data() {
    return {

    }
  },
}
</script>

<style scoped lang="less">
.content-all {
  min-height: 60vh;
  padding-bottom: 100px;
}
</style>
