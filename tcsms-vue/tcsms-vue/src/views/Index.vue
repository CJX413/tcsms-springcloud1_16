<template>
  <el-container>
    <el-header style="padding: 0">
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b" style="height: inherit">
        <el-row>
          <el-col :span="1" :offset="18">
            <el-menu-item index="1">
              <el-badge :value="3" class="item">
                <i class="el-icon-message" style="font-size: 25px;"></i></el-badge>
            </el-menu-item>
          </el-col>
          <el-col :span="3">
            <el-submenu index="2">
              <template slot="title">
                <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
                <span>陈嘉兴</span>
              </template>
              <el-menu-item index="2-1">
                <i class="el-icon-user-solid"></i>
                <span>个人信息</span>
              </el-menu-item>
              <el-menu-item index="2-2">
                <i class="el-icon-edit"></i>
                <span>完善信息</span>
              </el-menu-item>
              <el-menu-item index="2-3">
                <i class="el-icon-switch-button"></i>
                <span>注销</span>
              </el-menu-item>
            </el-submenu>
          </el-col>
          <el-col :span="2">
            <el-menu-item index="3">
              <el-link type="primary" href="">管理系统</el-link>
            </el-menu-item>
          </el-col>
        </el-row>
      </el-menu>
    </el-header>
    <el-divider></el-divider>
    <el-container>
      <el-aside width="200px">
        <el-menu
          default-active="2"
          class="el-menu-vertical-demo"
          @select="handleSelect" style="text-align: left">
          <el-menu-item index="1">
            <i class="el-icon-location"></i>
            <span>设备位置</span>
          </el-menu-item>
          <el-menu-item index="2">
            <i class="el-icon-view"></i>
            <span>视频监控</span>
          </el-menu-item>
          <el-menu-item index="3">
            <i class="el-icon-coordinate"></i>
            <span>注册驾驶员</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-s-custom"></i>
            <span>注册监控员</span>
          </el-menu-item>
          <el-menu-item index="5">
            <i class="el-icon-video-camera-solid"></i>
            <span>监控回放</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <device-location></device-location>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import DeviceLocation from '../components/DeviceLocation.vue'

  export default {
    name: "Index",
    components: {DeviceLocation},
    data() {
      return {
        activeIndex: '1',
      };
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
    }
  }
</script>
<style scoped>

</style>
