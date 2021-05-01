<template>
  <sidebar @changepage="changePage" :currentPage="currentPage" :pages="pages"></sidebar>
  <contents @emitalert="displayAlert" :currentPage="currentPage"></contents>
  <alert :error="alert.error" :message="alert.message" :visible="alert.visible"></alert>
</template>

<script lang="ts">
import Page from "./objects/Page";
import Alert from "@/components/Alert.vue";
import Sidebar from "@/components/Sidebar.vue";
import Contents from "@/components/Contents.vue";
import { defineComponent } from "vue";
export default defineComponent({
  data() {
    return {
      pages: [
        new Page("Übersicht", "<i class=\"ip ip-grid\"></i>"),
        new Page("Deine Stats", "<i class=\"ip ip-statistic-grow\"></i>"),
        new Page("Alarm-Regeln", "<i class=\"ip ip-slider\"></i>"),
        new Page("QR-Code", "<i class=\"ip ip-wlan-full\"></i>"),
        new Page("Info", "<i class=\"ip ip-github\"></i>")
      ],
      currentPage: undefined as Page | undefined,
      alert: {
        message: "",
        visible: false,
        timeout: undefined as number | undefined,
        error: true
      }
    }
  },
  methods: {
    changePage(page : Page) {
      this.currentPage = page;
    },
    displayAlert(message : string, error = true, timeout = 5000) {
      this.alert.message = message;
      this.alert.visible = true;
      this.alert.error = error;
      let that = this;
      clearTimeout(this.alert.timeout);
      this.alert.timeout = setTimeout(function () {
        that.alert.visible = false;
      }, timeout);
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
})
</script>