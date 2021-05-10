export default class Player {

    /**
     * Object used for mapping Minecraft color codes to CSS colors.
     * @type {{"§9": string, "§1": string, "§a": string, "§2": string, "§c": string, "§5": string, "§e": string, "§6": string}}
     */
    static colorMap : any = {
        "§5": "purple",
        "§6": "orange",
        "§9": "blue",
        "§c": "red",
        "§e": "yellow",
        "§a": "green",
        "§d": "pink",
        "§b": "aqua"
    }

    playerName : string;
    warned : boolean;
    rank : number;
    winRate : number;
    playedGames : number;
    wins : number;
    cookies : number;
    cookiesPerGame : number;
    statsHidden : boolean;
    nickProbability : number;
    prefix : string;
    statsType : string;

    /**
     * Initializes a new player object with all of its attributes
     * @constructor
     * @param playerName {string} The player's ingame name
     * @param warned {boolean} Whether or not the player has been warned
     * @param rank {number} Position of the player in ranking
     * @param winRate {number} WinRate of the player
     * @param playedGames {number} Number of games played
     * @param wins {number} Number of wins
     * @param cookies {number} Number of collected cookies
     * @param cookiesPerGame {number} Number of average cookies per game played
     * @param statsHidden {boolean} Whether or not the player has hidden his stats
     * @param nickProbability {number} Probability from 0 to 100 of the player being nicked
     * @param prefix {string} The team prefix of the player
     * @param statsType {string} How the stats have been checked (30d, alltime, etc.)
     */

    // @ts-ignore
    constructor(playerName: string, warned: boolean, rank: number, winRate: number, playedGames: number, wins: number, cookies: number, cookiesPerGame: number, statsHidden: boolean, nickProbability: number, prefix: string, statsType: string) {
        this.playerName = playerName;
        this.warned = warned;
        this.rank = rank;
        this.winRate = winRate;
        this.playedGames = playedGames;
        this.wins = wins;
        this.cookies = cookies;
        this.cookiesPerGame = cookiesPerGame;
        this.statsHidden = statsHidden;
        this.nickProbability = nickProbability;
        this.prefix = prefix;
        this.statsType = statsType;
    }

    /**
     * Calculates the appropriate color for the players card
     * @returns {string} CSS class for the card's color
     */
    calcCardColor() {
        if (this.warned) {
            return 'warned';
        } else if (this.statsHidden) {
            return 'hidden';
        } else if (this.nickProbability >= 45) {
            return 'nicked';
        }
        return '';
    }

    /**
     * Decides the team color and converts it into a CSS color.
     * @returns {string} The appropriate CSS color for the prefix or an empty string if no color is required
     */
    getCSSColor() {
        if (this.prefix.length > 2) {
            if (this.prefix.includes("Content") ||
                this.prefix.includes("Suprem") ||
                this.prefix.includes("Mod") ||
                this.prefix === "§a" ||
                this.prefix.includes("Admin") ||
                this.prefix.includes("Sup")) {
                return "";
            }
            const substr = this.prefix.substr(0, 2);
            return "color: " + Player.colorMap[substr];
        } else {
            return "";
        }
    }

    /**
     * Converts a JSON object into a {@link Player} instance.
     * @param obj {Object} JSON object with all required attributes (name, warned, rank, winRate, statsHidden, nickProbability, prefix)
     * @returns {Player} New instance of a {@link Player} with the attributes set to the object's values
     */
    static fromObject(obj: { name: string; warned: boolean; rank: number; winRate: number; playedGames: number; wins: number; cookies: number; cookiesPerGame: number; statsHidden: boolean; nickProbability: number; prefix: string; statsType: string; }) {
        return new Player(obj.name, obj.warned, obj.rank, obj.winRate, obj.playedGames, obj.wins, obj.cookies, obj.cookiesPerGame, obj.statsHidden, obj.nickProbability, obj.prefix, obj.statsType);
    }
}