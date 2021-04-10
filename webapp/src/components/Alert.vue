<template>
  <div :class="!visible ? 'hidden' : ''" class="notification">{{message}}</div>
</template>
<script>
export default {
  props: {
    message: String,
    visible: Boolean
  },
}
</script>