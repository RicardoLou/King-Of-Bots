
export default {
    state: {
        status: "matching", // matching 表示匹配中，显示匹配界面, playing 表示正在游戏中，显示游戏界面
        socket: null,
        opponent_username: "我的对手",
        opponent_avatar: "https://big-event-ricardo.oss-cn-hangzhou.aliyuncs.com/38BEBC15045C433F99DC1FE26A212B38-6-2.png",
        gameMap: null,
        a_id: 0,
        a_sx: 0,
        a_sy: 0,
        b_id: 0,
        b_sx: 0,
        b_sy: 0,
        gameObject: null,
        winner: "none", // none、all、A、B
    },
    getters: {
    },
    mutations: {
        updatePkSocket(state, socket) {
          state.socket = socket;
        },
        updateOpponentInfo(state, data) {
          state.opponent_username = data.opponent_username;
          state.opponent_avatar = data.opponent_avatar;
        },
        updatePkStatus(state, status) {
          state.status = status;
        },
        updateGame(state, game) {
          state.gameMap = game.gameMap || game.gamemap;
          state.a_id = game.a_id;
          state.a_sx = game.a_sx;
          state.a_sy = game.a_sy;
          state.b_id = game.b_id;
          state.b_sx = game.b_sx;
          state.b_sy = game.b_sy;
        },
        updateGameObject(state, gameObject) {
          state.gameObject = gameObject;
        },
        updateWinner(state, winner) {
            state.winner = winner;
        }
    },
    actions: {
    },
    modules: {
    }
}