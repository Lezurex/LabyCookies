<template>
  <sidebar @changepage="changePage" :currentPage="currentPage" :pages="pages"></sidebar>
  <contents @emitalert="displayAlert" :currentPage="currentPage"></contents>
  <alert :message="alert.message" :visible="alert.visible"></alert>
</template>

<script>

import Page from "@/objects/Page.js";
import Alert from "@/components/Alert.vue";
import Sidebar from "@/components/Sidebar.vue";
import Contents from "@/components/Contents.vue";

export default {
  data() {
    return {
      pages: [
        new Page("Übersicht", "<i class=\"ip ip-grid\"></i>"),
        new Page("Deine Stats", "<i class=\"ip ip-statistic-grow\"></i>"),
        new Page("Alarm-Regeln", "<i class=\"ip ip-slider\"></i>"),
        new Page("QR-Code", "<i class=\"ip ip-wlan-full\"></i>"),
        new Page("Info", "<i class=\"ip ip-github\"></i>")
      ],
      currentPage: undefined,
      alert: {
        message: "",
        visible: false,
        timeout: undefined
      }
    }
  },
  methods: {
    changePage(page) {
      this.currentPage = page;
    },
    displayAlert(message) {
      this.alert.message = message;
      this.alert.visible = true;
      let that = this;
      clearTimeout(this.alert.timeout);
      this.alert.timeout = setTimeout(function () {
        that.alert.visible = false;
      }, 5000);
    }
  },
  created: function () {
    this.currentPage = this.pages[0];
  },
  components: {
    alert: Alert,
    sidebar: Sidebar,
    contents: Contents
  }
}
</script>
