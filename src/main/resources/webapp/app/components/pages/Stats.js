import Page from "../../objects/Page.js";
import Player from "../../objects/Player.js";

export default {
    data() {
        return {
            players: [
                new Player("Lezurex_", true, 88, 99),
                new Player("Fredmaster07", false, 8, 100),
            ]
        }
    },
    template: `
      <div class="overview-container">
      <div v-for="player in players" class="overview-card" :class="player.warned ? 'warned' : ''">
        <img :src="'https://minotar.net/helm/' + player.playerName" :alt="player.playerName">
        <div class="overview-info">
          <b>{{ player.playerName }}</b>
          <span>Rang: {{ player.rank }}</span>
          <span>Winrate: {{ player.winrate }}%</span>
        </div>
      </div>
      </div>
    `
}