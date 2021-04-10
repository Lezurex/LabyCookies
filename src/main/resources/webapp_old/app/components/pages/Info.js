export default {
    data() {
        return {
            version: "Lädt..."
        }
    },
    template: `
        <p>Das StatsAddon wurde entwickelt von VoxCrafter_LP und Lezurex.<br>
        Der gesamte Quellcode zu diesem Projekt ist auf GitHub zu finden.</p>
        <a class="info-link" href="https://github.com/Lezurex/LabyCookies"><i class="ip ip-github"></i> GitHub</a><br><br>
        <p>Du hast einen Fehler gefunden, hast Ideen oder Fragen? Dann melde dich<br>entweder über Discord (Lezurex#4269) & (VoxCrafter_LP#4269) oder <a href="https://github.com/Lezurex/LabyCookies/issues/new">erstelle ein Issue</a>.</p>
        <p>Aktuelle Version: {{version}}</p>
    `,
    mounted: function () {
        let request = new XMLHttpRequest();
        request.open("POST", window.location.origin + "/api/version");
        let that = this;
        request.addEventListener("load", function () {
            if (request.status === 200) {
                that.version = JSON.parse(request.responseText).data[0].version;
            }
        });
        request.send("{'i':1}");
    }
}