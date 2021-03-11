import Player from "../../objects/Player.js";

export default {
    data() {
        return {
            players: []
        }
    },
    template: `
      <div class="overview-container">
      <div v-for="player in players" class="overview-card" :class="player.warned ? 'warned' : ''">
        <img :src="'https://minotar.net/helm/' + player.playerName" :alt="player.playerName">
        <div class="overview-info">
          <b>{{ player.playerName }}</b>
          <span>Rang: {{ player.rank }}</span>
          <span>Winrate: {{ player.winRate }}%</span>
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
            request.addEventListener("load", function () {
                if (request.status === 200) {
                    let data = JSON.parse(request.responseText).data;
                    that.players = [];
                    data.forEach(stats => {
                        that.players.push(Player.fromObject(stats));
                    });
                }
            });
            request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            request.send(JSON.stringify({i:0}));
            setTimeout(this.requestStats, 500);
        }
    }

}