export default class Page {

    icon;
    name;

    // @ts-ignore
    constructor(name: string, icon: string) {
        this.icon = icon;
        this.name = name;
    }
}