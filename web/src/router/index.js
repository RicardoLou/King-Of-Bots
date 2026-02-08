import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import UserIndexView from '../views/user/UserIndexView.vue'
import NotFound from '../views/error/NotFound.vue'
import UserLoginView from '@/views/user/accout/UserLoginView.vue'
import UserRegisterView from '@/views/user/accout/UserRegisterView.vue'
import store from '@/store/index.js'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView.vue'

const routes = [
    {
        path: '/pk',
        name: 'pk_view',
        component: PkIndexView,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/ranklist',
        name: 'ranklist_view',
        component: RanklistIndexView,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/record',
        name: 'record_view',
        component: RecordIndexView,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/user',
        name: 'user_view',
        component: UserIndexView,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/404',
        name: 'not_found_view',
        component: NotFound,
        meta: {
            requiresAuth: false
        }
    },
    {
        path: '/user/accout/login',
        name: 'login_view',
        component: UserLoginView,
        meta: {
            requiresAuth: false
        }
    },
    {
        path: '/user/accout/register',
        name: 'register_view',
        component: UserRegisterView,
        meta: {
            requiresAuth: false
        }
    },
    {
        path: '/user/bot/index',
        name: 'user_bot_index_view',
        component: UserBotIndexView,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/',
        name: 'Home',
        redirect: '/pk'
    },
    {
        path: '/:catchAll(.*)',
        redirect: '/404/'
    }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth && !store.state.user.is_login) {
        next({ name: 'login_view' })
    } else {
        next()
    }
})
export default router
