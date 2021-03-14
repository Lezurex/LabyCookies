import Sidebar from "./components/Sidebar.js";
import Contents from "./components/Contents.js";
import Page from "./objects/Page.js";
import Stats from "./components/pages/Stats.js";
import Alert from "./components/Alert.js";
import QRCodePage from "./components/pages/QRCodePage.js";
import Info from "./components/pages/Info.js";
import UserStats from "./components/pages/UserStats.js";

const app = Vue.createApp({
    data() {
        return {
            pages: [
                new Page("Übersicht", "<i class=\"ip ip-grid\"></i>"),
                new Page("Deine Stats", "<i class=\"ip ip-statistic-grow\"></i>"),
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
    template: `
        <sidebar @changepage="changePage" :currentPage="currentPage" :pages="pages"></sidebar>
        <contents @emitalert="displayAlert" :currentPage="currentPage"></contents>
        <alert :message="alert.message" :visible="alert.visible"></alert>
    `,
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
    }
});

app.component("sidebar", Sidebar);
app.component("contents", Contents);
app.component("stats", Stats);
app.component("alert", Alert);
app.component("qrcode", QRCodePage);
app.component("info", Info);
app.component("userStats", UserStats);

const mountedApp = app.mount("#app");