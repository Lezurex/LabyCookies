export default class Player {

    playerName;
    warned;
    rank;
    winRate;

    constructor(playerName, warned, rank, winrate) {
        this.playerName = playerName;
        this.warned = warned;
        this.rank = rank;
        this.winRate = winrate;
    }

    static fromObject(obj) {
        return new Player(obj.name, obj.warned, obj.rank, obj.winRate)
    }
}