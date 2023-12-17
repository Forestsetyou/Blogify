<template>
  <div class="com-container">
    <div class="com-chart" ref="blog_ref">
    </div>
  </div>
</template>

<script>
export default {
  props: ['psMsg'],
  data () {
    return {
      statisticsByMonth: {
        views: {},
        essays: {},
        praise: {},
        comment: {}
      },
      chartInstance: null,
      timeList: [],
      selectList: ['阅读量', '文章数', '点赞数', '评论数'],
      statisticsList: ['views', 'essays', 'appreciation', 'comment']
    }
  },
  mounted () {
    this.initChart()
    window.addEventListener('resize', this.screenAdapter)
  },
  watch: {
    'psMsg': {
      handler (value) {
        this.refreshChart()
      }
    }
  },
  methods: {
    initChart () {
      const initOption = {
        backgroundColor: '#fff', // 背景颜色
        legend: {
          // orient 设置布局方式，默认水平布局，可选值：'horizontal'（水平） ¦ 'vertical'（垂直）
          orient: 'horizontal',
          // x 设置水平安放位置，默认全图居中，可选值：'center' ¦ 'left' ¦ 'right' ¦ {number}（x坐标，单位px）
          x: 'center',
          // y 设置垂直安放位置，默认全图顶端，可选值：'top' ¦ 'bottom' ¦ 'center' ¦ {number}（y坐标，单位px）
          y: 'top',
          data: ['阅读量']
        },
        //  图表距边框的距离,可选值：'百分比'¦ {number}（单位px）
        grid: {
          top: '10%', // 等价于 y: '16%'
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        // 提示框
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        axisPointer: { // 坐标轴指示器，坐标轴触发有效
          type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
        },
        xAxis: {
          name: '月份',
          type: 'category',
          // 设置X轴数据旋转倾斜
          axisLabel: {
            rotate: 20, // 旋转角度
            interval: 0 // 设置X轴数据间隔几个显示一个，为0表示都显示
          },
          // boundaryGap值为false的时候，折线第一个点在y轴上
          boundaryGap: false,
          data: this.timeList
        },
        yAxis: {
          name: '数值',
          type: 'value'
        },
        series: [
          {
            name: '阅读量', // * 需变化量
            data: [820, 932, 301, 1434, 1290, 1330, 1320], // * 需变化量
            type: 'line',
            // 设置小圆点消失
            // 注意：设置symbol: 'none'以后，拐点不存在了，设置拐点上显示数值无效
            symbol: 'none',
            // 设置折线弧度，取值：0-1之间
            smooth: 0.5,
            itemStyle: {
              normal: {
                color: '#3888fa',
                lineStyle: {
                  color: '#3888fa',
                  width: 2
                },
                areaStyle: {
                  color: '#f3f8ff'
                }
              }
            },
            animationDuration: 2800,
            animationEasing: 'cubicInOut'
          }
        ]
      }
      this.chartInstance = this.$echarts.init(this.$refs.blog_ref, this.theme)
      this.chartInstance.setOption(initOption)
      this.$axios.get('/blog/statistics?q=month')
        .then(resp => {
          if (resp.data.code === 200) {
            let data = resp.data.data
            let statisticsByMonth = {}
            for (let resource of this.statisticsList) {
              statisticsByMonth[resource] = {}
              for (let monthCount of data[resource]) {
                statisticsByMonth[resource][monthCount['date_month']] = monthCount['count']
              }
            }
            this.statisticsByMonth = statisticsByMonth
            this.initTime()
            this.refreshChart()
          }
        }).catch(failResp => {})
    },
    dateFormat (date) {
      return `${date.getFullYear()}-${(date.getMonth() + 1 + '').padStart(2, '0')}`
    },
    initTime () {
      let now = new Date()
      let month = now.getMonth() + 1
      let year = now.getFullYear()
      let timeList = []
      for (let i = 0; i < 7; i++) { // 从现在起向前倒退6个月
        let timeStr = this.dateFormat(now)
        timeList = timeList.concat(timeStr)
        for (let resource of this.statisticsList) {
          if (this.statisticsByMonth[resource][timeStr] === undefined) {
            this.statisticsByMonth[resource][timeStr] = 0
          }
        }
        if (month === 0) {
          month = 11
          year -= 1
        } else month -= 1
        now.setFullYear(year)
        now.setMonth(month)
      }
      this.timeList = timeList.reverse()
      let option = {
        xAxis: {
          data: this.timeList
        }
      }
      this.chartInstance.setOption(option)
    },
    refreshChart () {
      let countList = []
      let resource = this.statisticsList[this.psMsg]
      for (let monthDate of this.timeList) {
        countList = countList.concat(this.statisticsByMonth[resource][monthDate])
      }
      let option = {
        legend: {
          data: [this.selectList[this.psMsg]]
        },
        series: [
          {
            name: this.selectList[this.psMsg],
            data: countList
          }
        ]
      }
      this.chartInstance.setOption(option)
    },
    screenAdapter () {
      this.chartInstance.resize()
    }
  }
}
</script>

<style scoped lang="less">

.com-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.com-chart {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

canvas {
  border-radius: 20px;
}
</style>
