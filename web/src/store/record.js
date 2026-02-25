
export default {
    state: {
      isRecord: false, // 当前是对战还是录像
      a_steps: "", // 1P的操作记录
      b_steps: "", // 2P的操作记录
      record_winner: -1, // 录像的赢家
    },
    getters: {
    },
    mutations: {
      updateIsRecord(state, isRecord) {
        state.isRecord = isRecord;
      },
      updateSteps(state, steps) {
        state.a_steps = steps.a_steps;
        state.b_steps = steps.b_steps;
      },
      updateWinner(state, winner) {
        state.record_winner = winner;
      }
    },
    actions: {
    },
    modules: {
    }
}