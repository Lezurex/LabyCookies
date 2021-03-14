import Player from "../../objects/Player.js";

export default {
    data() {
        return {
            stats30d: undefined,
            stats1d: undefined,
            statsAll: undefined,
            mounted: undefined,
        }
    },
    template: `
      <p>Hier kannst du deine eigenen Stats einsehen. Um sie zu laden, joinen in deinen bevorzugten Spielmodus (am besten eine leere Runde).</p>
      <div class="stats-container">
      <div v-if="stats30d !== undefined" class="stats-card">
        <h2>30 Tage</h2>
        <span v-if="stats30d.rank === -1">Lädt...</span>
        <span v-else>
          <span>Rang: {{ stats30d.rank }}</span>
          <span>Winrate: {{ stats30d.winRate }}</span>
          <span>Spiele: {{ stats30d.playedGames }}</span>
          <span>Wins: {{ stats30d.wins }}</span>
          <span v-if="stats30d.cookies > 0">Cookies: {{ stats30d.cookies }}</span>
          <span v-if="stats30d.cookiesPerGame > 0">Cookies/Spiel: {{ stats30d.cookiesPerGame }}</span>

        </span>
      </div>
      <div v-if="statsAll !== undefined" class="stats-card">
        <h2>AllTime</h2>
        <span v-if="statsAll.rank === -1">Lädt...</span>
        <span v-else>
          <span>Rang: {{ statsAll.rank }}</span>
          <span>Winrate: {{ statsAll.winRate }}</span>
          <span>Spiele: {{ statsAll.playedGames }}</span>
          <span>Wins: {{ statsAll.wins }}</span>
          <span v-if="statsAll.cookies > 0">Cookies: {{ statsAll.cookies }}</span>
          <span v-if="statsAll.cookiesPerGame > 0">Cookies/Spiel: {{ statsAll.cookiesPerGame }}</span>

        </span>
      </div>
      <div v-if="stats1d !== undefined" class="stats-card">
        <h2>1 Tag</h2>
        <span v-if="stats1d.winRate === -1">Lädt...</span>
        <span v-else>
          <span>Winrate: {{ stats1d.winRate }}</span>
          <span>Spiele: {{ stats1d.playedGames }}</span>
          <span>Wins: {{ stats1d.wins }}</span>
          <span v-if="stats1d.cookies > 0">Cookies: {{ stats1d.cookies }}</span>
          <span v-if="stats1d.cookiesPerGame > 0">Cookies/Spiel: {{ stats1d.cookiesPerGame }}</span>

        </span>
      </div>
      </div>
    `,
    mounted: function () {
        this.mounted = true;
        this.getStats();
    },
    unmounted: function () {
        this.mounted = false;
    },
    methods: {
        getStats() {
            let request = new XMLHttpRequest();
            request.open("POST", window.location.origin + "/api/userstats");
            let that = this;
            request.addEventListener("load", function () {
                if (request.status === 200) {
                    let data = JSON.parse(request.responseText).data;
                    for (let index in data) {
                        let player = Player.fromObject(data[index]);
                        switch (player.statsType) {
                            case "STATS 30 TAGE":
                                that.stats30d = player;
                                break;
                            case "STATSALL":
                                that.statsAll = player;
                                break
                            case "STATS 1 TAG":
                                that.stats1d = player;
                                break;
                        }
                    }
                }
            });
            request.send("{i:1}")
            if (this.mounted) {
                setTimeout(function () {
                    that.getStats()
                }, 500);
            }
        }
    }
}