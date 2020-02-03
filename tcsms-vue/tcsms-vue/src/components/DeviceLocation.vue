<template>
  <div>
    <div class="deviceCard"></div>
    <el-collapse accordion>
      <el-collapse-item>
        <template slot="title">
          <i class="el-icon-s-operation" style="font-size: 15px"><span>所有设备</span></i>
        </template>
        <el-menu class="el-menu-demo" mode="horizontal">
          <el-col :span="2" v-for="device in deviceList" v-bind:key="device.deviceId">
            <el-popover
              placement="right"
              trigger="click">
              <device-card :device="device"/>
              <el-button slot="reference" style="border-color: white;visibility: visible">{{device.deviceId}}
              </el-button>
            </el-popover>
            <el-divider direction="vertical" style="visibility: visible"></el-divider>
          </el-col>
        </el-menu>
      </el-collapse-item>
    </el-collapse>
    <baidu-map class="map" :center="map.center" :zoom="map.zoom" @ready="handler">
      <!--缩放-->
      <bm-navigation anchor="BMAP_ANCHOR_TOP_LEFT"></bm-navigation>
      <!--定位-->
      <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
      <!--点-->
      <bm-marker :position="map.center" :dragging="map.dragging" animation="BMAP_ANIMATION_DROP">
        <!--提示信息-->
        <bm-info-window :show="map.show">Hello~</bm-info-window>
      </bm-marker>
    </baidu-map>
  </div>
</template>

<script>
  import DeviceCard from '../components/DeviceCard'

  export default {
    name: "DeviceLocation",
    components: {DeviceCard},
    data() {
      return {
        deviceList: [],
        map: {
          center: {lng: 121.4472540000, lat: 31.3236200000},
          zoom: 15,
          show: true,
          dragging: true
        },
      };
    },
    mounted() {   //初始化页面要在mounted方法中调用自己也得初始化方法
      this.initPage();
    },
    methods: {
      handler({BMap, map}) {
        let me = this;
        console.log(BMap, map);
        // 鼠标缩放
        map.enableScrollWheelZoom(true);
        // 点击事件获取经纬度
        map.addEventListener('click', function (e) {
          console.log(e.point.lng, e.point.lat)
        })
      },
      initPage() {
        this.axios.post('http://localhost:8080/deviceInfo', {})
          .then((response) => {
            console.log(response);
            if (response.status === 200) {
              console.log(response);
              this.deviceList = response.data;
            } else {
            }
          });
      }
    },
  }
</script>
<style scoped>

  .map {
    width: 100%;
    height: 400px;
  }
</style>
