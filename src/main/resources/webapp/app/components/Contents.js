import Page from "../objects/Page.js";

export default {
    props: {
        currentPage: Page
    },
    template: `
    <main>
        <h1>{{currentPage.name}}</h1>
        <stats v-if="currentPage.name === 'Übersicht'"></stats>
    </main>
    `
}