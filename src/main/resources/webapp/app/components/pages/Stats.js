import Player from "../../objects/Player.js";

export default {
    data() {
        return {
            players: [],
            mounted: undefined
        }
    },
    template: `
      <div class="overview-container">
      <span v-if="players.length === 0">Entweder sind keine Spieler in der Runde, oder du befindest dich auf der Lobby.</span>
      <div v-for="player in players" class="overview-card" :class="player.calcCardColor()">
        <img :src="'https://minotar.net/helm/' + player.playerName" :alt="player.playerName">
        <div class="overview-info">
          <b :style="player.getCSSColor()">{{ player.playerName }}</b>
          <div v-if="!player.statsHidden">
            <span v-if="player.rank > 0">Rang: {{ player.rank }}</span>
            <span v-else>Rang: -</span>
            <span v-if="player.winRate >= 0">Winrate: {{ player.winRate }}%</span>
            <span v-else>Winrate: -</span>
            <span v-if="player.playedGames >= 0">Spiele: {{ player.playedGames }}</span>
            <span v-else>Spiele: -</span>
            <span v-if="player.wins >= 0">Wins: {{ player.wins }}</span>
            <span v-else>Wins: -</span>
            <span v-if="player.cookies >= 0">Cookies: {{ player.cookies }}</span>
            <span v-if="player.cookiesPerGame >= 0">Cookies/Spiel: {{ player.cookiesPerGame }}</span>
          </div>
          <span v-else>Stats versteckt!</span>
          <span v-if="player.nickProbability >= 45">
            <span v-if="player.nickProbability === 100">Genickter Spieler!</span>
            <span v-else>Spieler zu {{ player.nickProbability }}% genickt!</span>
          </span>

        </div>
      </div>
      </div>
    `,
    mounted: function () {
        this.mounted = true;
        this.requestStats();
    },
    unmounted: function () {
        this.mounted = false;
    },
    methods: {
        /**
         * Fetches the newest data from the Minecraft client and updates the overview
         */
        requestStats() {
            let that = this;
            let request = new XMLHttpRequest();
            request.open("POST", window.location.origin + "/api/stats");
            request.timeout = 1000;
            request.addEventListener("load", function () {
                if (request.status === 200) {
                    let data = JSON.parse(request.responseText).data;
                    that.players = [];
                    data.forEach(stats => {
                        that.players.push(Player.fromObject(stats));
                    });
                } else {
                }
            });
            request.addEventListener("error", function () {
                that.$emit("emitalert", "Verbindung zum Minecraft-Client fehlgeschlagen!");
            })
            request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            request.send(JSON.stringify({i: 0}));
            let timeout = 500;
            window.location.hostname === "localhost" ? timeout = 500 : timeout = 2000;
            if (this.mounted) {
                setTimeout(function () {
                    that.requestStats()
                }, timeout);
            }
        }
    }

}