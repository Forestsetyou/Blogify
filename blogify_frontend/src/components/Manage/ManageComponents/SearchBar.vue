<template>
  <div style="display: inline-flex">
    <el-input
        v-model="keyword"
        :disabled="input_disabled"
        :placeholder="input_placeholder"
        prefix-icon="el-icon-search"
        style="width: 350px;margin-right: 10px"
        clearable
        @clear="search"
        @keydown.enter.native="search"></el-input>
    <el-button
        :disabled="input_disabled"
        icon="el-icon-search"
        type="primary"
        @click="search">
      搜索
    </el-button>
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  props: {
    input_disabled: {
      type: Boolean,
      default: false
    },
    input_placeholder: {
      type: String,
      default: '请输入关键词进行搜索，可以直接回车搜索...'
    }
  },
  data () {
    return {
      keyword: ''
    }
  },
  mounted () {
    this.input_placeholder = this.input_disabled ? '' : this.input_placeholder
  },
  methods: {
    search () {
      this.$emit('inputSearch', this.keyword)
    }
  }
}
</script>

<style scoped>

</style>
