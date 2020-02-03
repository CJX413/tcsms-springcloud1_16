<template>
  <div>
    <el-card class="box-card" style="width: 480px">
      <div slot="header" class="clearfix">
        <span>设备名：{{device.deviceId}}</span>
      </div>
      <el-row>
        <el-col :span="12"><span>是否注册：{{device.isRegistered}}</span></el-col>
        <el-col :span="12"><span>设备型号：{{device.deviceModel}}</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="12"><span>纬度：{{device.latitude}}</span></el-col>
        <el-col :span="12"><span>经度：{{device.longitude}}</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="12"><span>吊臂高度：{{device.bigHeight}}</span></el-col>
        <el-col :span="12"><span>顶部高度：{{device.bigHeight+device.smallHeight}}</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="12"><span>大臂：{{device.bigLength}}</span></el-col>
        <el-col :span="12"><span>小臂：{{device.smallLength}}</span></el-col>
      </el-row>
      <el-row>
        <el-col :span="12"><span>额定力矩：{{device.rlt}}</span></el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "DeviceCard",
    data() {
      return {};
    },
    props: ['device'],
    methods: {}
  }
</script>

<style scoped>

</style>
