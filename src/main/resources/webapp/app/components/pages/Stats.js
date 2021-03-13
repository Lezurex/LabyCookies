import Player from "../../objects/Player.js";

export default {
    data() {
        return {
            players: [],
            colorMap: {
                "§1": "darkblue",
                "§2": "darkgreen",
                "§5": "purple",
                "§6": "orange",
                "§9": "blue",
                "§c": "red",
                "§e": "yellow",
                "§a": "green"
            }
        }
    },
    template: `
      <div class="overview-container">
      <div v-for="player in players" class="overview-card" :class="isDangerous(player)">
        <img :src="'https://minotar.net/helm/' + player.playerName" :alt="player.playerName">
        <div class="overview-info">
          <b :style="getCSSColor(player)">{{ player.playerName }}</b>
          <div v-if="!player.statsHidden && player.nickProbability < 45">
            <span v-if="player.rank > 0">Rang: {{ player.rank }}</span>
            <span v-else>Rang: -</span>
            <span v-if="player.winRate >= 0">Winrate: {{ player.winRate }}%</span>
            <span v-else>Winrate: -</span>
          </div>
          <span v-else-if="player.isHidden">Stats versteckt!</span>
          <span v-else-if="player.nickProbability >= 45">
            <span v-if="player.nickProbability === 100">Genickter Spieler!</span>
            <span v-else>Spieler zu {{ player.nickProbability }}% genickt!</span>
          </span>

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
            request.send(JSON.stringify({i: 0}));
            let timeout = 500;
            window.location.hostname === "localhost" ? timeout = 500 : timeout = 2000;
            setTimeout(this.requestStats, 500);
        },
        isDangerous(player) {
            if (player.warned || player.isHidden || player.nickProbability >= 45) {
                return 'warned';
            }
            return '';
        },
        getCSSColor(player) {
            if (player.prefix.length > 2) {
                if (player.prefix.includes("Content") ||
                    player.prefix.includes("Suprem") ||
                    player.prefix.includes("Mod") ||
                    player.prefix === "§a" ||
                    player.prefix.includes("Admin") ||
                    player.prefix.includes("Sup")) {
                    return "";
                }
                let substr = player.prefix.substr(0, 2);
                return "color: " + this.colorMap[substr];
            } else {
                return "";
            }
        }
    }

}