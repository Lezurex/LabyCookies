<template>
  <nav class="sidenavbar">
    <ul class="sidenavbar-nav">
      <li class="logo">
        <a href="#" class="sidenav-link">
          <span class="link-text">Stats</span>
          <i>
            <img src="https://www.gommehd.net/uploads/images/game/bYQCloWF9bAGe13Hg8eAdryEKzDZ-SL-.png" alt="">
          </i>
        </a>
      </li>
      <li :key="page" @click="changePage(page)" v-for="page in pages" class="sidenav-item">
        <a href="#" class="sidenav-link" :class="page === currentPage ? 'active' : ''">
          <div v-html="page.icon"></div>
          <span class="link-text">{{ page.name }}</span>
        </a>
      </li>
    </ul>
  </nav>
</template>

<script lang="ts">
import Page from "./../objects/Page";
import {defineComponent} from "vue";

export default defineComponent({
  props: {
    pages: [],
    currentPage: Page
  },
  data() {
    return {

    }
  },
  methods: {
    changePage(page : Page) {
      this.$emit("changepage", page);
    }
  }
})
</script>