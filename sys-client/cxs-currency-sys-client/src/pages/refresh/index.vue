<template>
  <div></div>
</template>

<script>
export default {
  name: 'refresh',
  data () {
    return {
    }
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      vm.$router.replace({name: to.query.to})
    })
  }
}

</script>
<style scoped>
</style>
