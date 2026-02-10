
export default {
    state: {
        status: "matching", // matching 表示匹配中，显示匹配界面, playing 表示正在游戏中，显示游戏界面
        socket: null,
        opponent_username: "",
        opponent_avatar: "",
        gameMap: null,
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
        updateGameMap(state, gameMap) {
          state.gameMap = gameMap;
        },
    },
    actions: {
    },
    modules: {
    }
}