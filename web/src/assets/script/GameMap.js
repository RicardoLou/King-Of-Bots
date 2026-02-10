import { AcGameObject } from './AcGameObjects.js';
import { Wall } from './Wall.js';

export class GameMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();
        
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.store = store;
        this.rows = 13;
        this.cols = 14;
        this.walls = [];

        this.inner_walls_count = 10; // 障碍物数量（对称）
    }

    create_walls() {
        const g = this.store.state.pk.gameMap;
        // 渲染墙
        for(let r = 0; r < this.rows; r ++) {
            for(let c = 0; c < this.cols; c ++) {
                if(g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    start() {
        this.create_walls();
    }

    update_size() { // 用户窗口大小改变时调用
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }
    update() {
        this.update_size();
        this.render();
    }
    render() {
        const color_even = '#AAD751', 
              color_odd = '#DBE8A6';
        for(let i = 0; i < this.rows; i++) {
            for(let j = 0; j < this.cols; j++) {
                if((i + j) % 2 === 0) {
                    this.ctx.fillStyle = color_even;
                }
                else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(j * this.L, i * this.L, this.L, this.L);
            }
        }
    }
}