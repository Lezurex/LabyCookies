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

<script>
import Page from "../objects/Page.js";
import Stats from "@/components/pages/Stats.vue";
import QRCodePage from "@/components/pages/QRCodePage.vue";
import Info from "@/components/pages/Info.vue";
import UserStats from "@/components/pages/UserStats.vue";
import AlertRules from "@/components/pages/AlertRules.vue";

export default {
  props: {
    currentPage: Page
  },
  methods: {
    emitAlert(message, error, timeout) {
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
}
</script>