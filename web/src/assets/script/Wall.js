import { AcGameObject } from './AcGameObjects.js';

export class Wall extends AcGameObject {
    constructor(r, c, gameMap) {
        super();

        this.gameMap = gameMap;
        this.r = r;
        this.c = c;
        this.color = "#B37226"
    }
    start() {
        
    }
    update() {
        this.render();
    }
    render() {
        const L = this.gameMap.L;
        const ctx = this.gameMap.ctx;
        ctx.fillStyle = this.color;
        ctx.fillRect(this.c * L, this.r * L, L, L);
    }
}