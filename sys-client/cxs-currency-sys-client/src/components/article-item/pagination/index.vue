<template>
  <nav class="navigation pagination" role="navigation">
    <div class="nav-links">
      <el-link v-if="pages <= 10" v-for="page in pages" :key="page" class="page-numbers current">{{page}}</el-link>
      <a class="page-numbers" href="/article?pageIndex=2">
        <span class="fa fa-angle-right"></span>
      </a>
    </div>
  </nav>
</template>

<script>
export default {
  name: "MyPagination",
  props: {
    queryChange: {
      type: Function
    },
    pages: {
      type: Number,
      default: 0
    }
  }
}
</script>

<style scoped lang="less">
.pagination {
  float: right;
  margin-top: 10px;

  .current {
    background-color: #409eff;
    color: #ffffff;
    border: 1px solid #409eff;
  }

  a {
    float: left;
    background: #fff;
    margin: 0 0 10px 5px;
    padding: 8px 11px;
    line-height: 100%;
    border-radius: 2px;
    box-shadow: 0 1px 1px rgb(0 0 0 / 4%);
  }
}
</style>
