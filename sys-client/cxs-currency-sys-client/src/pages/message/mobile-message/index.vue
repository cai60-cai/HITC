<template>
  <span>工大圈子提醒您,请使用PC访问系统</span>
</template>

<script>
export default {
name: "MobileMessage",
  mounted() {
    this.checkPc()
  },
  methods: {
    checkPc(){
      if (this.$device.windows) {
        this.$router.replace('/')
      }
    }
  }
}
</script>

<style scoped>

</style>
