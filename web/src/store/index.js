import { createStore } from 'vuex'
import UserModule from './user'
import PkModule from './pk'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: UserModule,
    pk: PkModule
  }
})
