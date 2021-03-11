export default class Player {

    playerName;
    warned;
    rank;
    winRate;
    statsHidden;

    constructor(playerName, warned, rank, winRate, statsHidden) {
        this.playerName = playerName;
        this.warned = warned;
        this.rank = rank;
        this.winRate = winRate;
        this.statsHidden = statsHidden;
    }

    static fromObject(obj) {
        return new Player(obj.name, obj.warned, obj.rank, obj.winRate, obj.statsHidden);
    }
}