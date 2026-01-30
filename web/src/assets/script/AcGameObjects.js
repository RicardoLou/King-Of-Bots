const AC_GAME_OBJECTS = [];

export class AcGameObject {
    constructor() {
        AC_GAME_OBJECTS.push(this);
        this.timedelta = 0; // 每帧的时间间隔
        this.has_started = false;
    }
    start() { // 创建时执行一次

    }
    update() { // 每一帧执行一次，除第一帧外

    }

    on_destroy() { // 销毁前执行一次

    }

    destroy() { // 销毁时执行一次
        this.on_destroy();
        for(let i in AC_GAME_OBJECTS) {
            const obj = AC_GAME_OBJECTS[i];
            if(obj === this) {
                AC_GAME_OBJECTS.splice(i, 1);
                break;
            }
        }
    }
}

let last_timestamp; // 上一次执行的时刻
// 游戏渲染循环，每帧调用一次
const step = timestamp => {
    for(let obj of AC_GAME_OBJECTS) {
        if(!obj.has_started) {
            obj.has_started = true;
            obj.start();
        }
        else {
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }
    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step);