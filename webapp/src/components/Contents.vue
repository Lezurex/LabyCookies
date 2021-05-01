<template>
  <main>
    <h1>{{currentPage.name}}</h1>
    <stats @emitalert="emitAlert" v-if="currentPage.name === 'Übersicht'"></stats>
    <qrcode v-else-if="currentPage.name === 'QR-Code'"></qrcode>
    <info v-else-if="currentPage.name === 'Info'"></info>
    <userStats v-else-if="currentPage.name === 'Deine Stats'"></userStats>
    <alert-rules @alert="emitAlert" v-else-if="currentPage.name === 'Alarm-Regeln'"></alert-rules>

  </main>
</template>

<script lang="ts">
import Page from "./.././objects/Page";
import Stats from "./pages/Stats.vue";
import QRCodePage from "./pages/QRCodePage.vue";
import Info from "./pages/Info.vue";
import UserStats from "./pages/UserStats.vue";
import AlertRules from "./pages/AlertRules.vue";
import {defineComponent} from "vue";

export default defineComponent({
  props: {
    currentPage: Page
  },
  methods: {
    emitAlert(message : string, error : boolean, timeout : number) {
      this.$emit("emitalert", message, error, timeout);
    }
  },
  components: {
    stats: Stats,
    qrcode: QRCodePage,
    info: Info,
    userStats: UserStats,
    alertRules: AlertRules
  }
})
</script>