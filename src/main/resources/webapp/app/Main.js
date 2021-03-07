import Sidebar from "./components/Sidebar.js";
import Contents from "./components/Contents.js";
import Page from "./objects/Page.js";
import Stats from "./components/pages/Stats.js";

const app = Vue.createApp({
    data() {
        return {
            pages: [
                new Page("Übersicht", "<i class=\"ip ip-grid\"></i>"),
                new Page("Coming soon", "<i class=\"ip ip-clock\"></i>")
            ],
            currentPage: undefined,
        }
    },
    template: `
        <sidebar @changepage="changePage" :currentPage="currentPage" :pages="pages"></sidebar>
        <contents :currentPage="currentPage"></contents>
    `,
    methods: {
        changePage(page) {
            this.currentPage = page;
        }
    },
    created: function () {
        this.currentPage = this.pages[0];
    }
});

app.component("sidebar", Sidebar);
app.component("contents", Contents);
app.component("stats", Stats)

const mountedApp = app.mount("#app");