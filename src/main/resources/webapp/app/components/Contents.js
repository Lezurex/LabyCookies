import Page from "../objects/Page.js";

export default {
    props: {
        currentPage: Page
    },
    template: `
    <main>
        <h1>{{currentPage.name}}</h1>
        <stats @emitalert="emitAlert" v-if="currentPage.name === 'Übersicht'"></stats>
        <qrcode v-else-if="currentPage.name === 'QR-Code'"></qrcode>
    </main>
    `,
    methods: {
        emitAlert(message) {
            this.$emit("emitalert", message);
        }
    }
}