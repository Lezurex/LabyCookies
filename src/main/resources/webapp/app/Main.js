import Sidebar from "./components/Sidebar.js";
import Contents from "./components/Contents.js";
import Page from "./objects/Page.js";

const app = Vue.createApp({
    data() {
        return {
            pages: [
                new Page("Stats", "<i class=\"ip ip-statistic-grow\"></i>"),
                new Page("Coming soon", "<i class=\"ip ip-clock\"></i>")
            ],
            currentPage: undefined,
        }
    },
    template: `
        <sidebar @changepage="changePage" :currentPage="currentPage" :pages="pages"></sidebar>
        <contents></contents>
    `,
    methods: {
        changePage(page) {
            this.currentPage = page;
        }
    },
    mounted: function () {
        this.currentPage = this.pages[0];
    }
});

app.component("sidebar", Sidebar);
app.component("contents", Contents)

const mountedApp = app.mount("#app");