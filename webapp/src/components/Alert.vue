<template>
  <div class="notification" :class="getCSSClasses">{{message}}</div>
</template>
<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  props: {
    message: String,
    visible: Boolean,
    error: Boolean
  },
  computed: {
    getCSSClasses() {
      let str = "";
      if (this.error) {
        str += "notification-error";
      } else {
        str += "notification-success";
      }
      if (!this.visible)
        str += " hidden";
      return str;
    }
  }
})
</script>