export default class Player {

    playerName;
    warned;
    rank;
    winRate;
    statsHidden;
    nickProbability;
    prefix;

    constructor(playerName, warned, rank, winRate, statsHidden, nickProbability, prefix) {
        this.playerName = playerName;
        this.warned = warned;
        this.rank = rank;
        this.winRate = winRate;
        this.statsHidden = statsHidden;
        this.nickProbability = nickProbability;
        this.prefix = prefix;
    }

    static fromObject(obj) {
        return new Player(obj.name, obj.warned, obj.rank, obj.winRate, obj.statsHidden, obj.nickProbability, obj.prefix);
    }
}