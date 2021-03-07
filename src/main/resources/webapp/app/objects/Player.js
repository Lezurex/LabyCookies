export default class Player {

    playerName;
    warned;
    rank;
    winrate;

    constructor(playerName, warned, rank, winrate) {
        this.playerName = playerName;
        this.warned = warned;
        this.rank = rank;
        this.winrate = winrate;
    }
}