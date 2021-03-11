import Player from "../../objects/Player.js";

export default {
    data() {
        return {
            players: []
        }
    },
    template: `
      <div class="overview-container">
      <div v-for="player in players" class="overview-card" :class="isDangerous(player)">
        <img :src="'https://minotar.net/helm/' + player.playerName" :alt="player.playerName">
        <div class="overview-info">
          <b>{{ player.playerName }}</b>
          <div v-if="!player.statsHidden">
            <span v-if="player.rank > 0">Rang: {{ player.rank }}</span>
            <span v-else>Rang: -</span>
            <span v-if="player.winRate >= 0">Winrate: {{ player.winRate }}%</span>
          </div>
          <span v-else>Stats versteckt!</span>
          
        </div>
      </div>
      </div>
    `,
    mounted: function () {
        this.requestStats();
    },
    methods: {
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
            request.send(JSON.stringify({i:0}));
            setTimeout(this.requestStats, 500);
        },
        isDangerous(player) {
            if (player.warned || player.isHidden) {
                return 'warned';
            }
            return '';
        }
    }

}