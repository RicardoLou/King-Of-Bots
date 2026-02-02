import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import UserIndexView from '../views/user/UserIndexView.vue'
import NotFound from '../views/error/NotFound.vue'
import UserLoginView from '@/views/user/accout/UserLoginView.vue'
import UserRegisterView from '@/views/user/accout/UserRegisterView.vue'

const routes = [
    {
        path: '/pk',
        name: 'pk_view',
        component: PkIndexView
    },
    {
        path: '/ranklist',
        name: 'ranklist_view',
        component: RanklistIndexView
    },
    {
        path: '/record',
        name: 'record_view',
        component: RecordIndexView
    },
    {
        path: '/user',
        name: 'user_view',
        component: UserIndexView
    },
    {
        path: '/404',
        name: 'not_found_view',
        component: NotFound
    },
    {
        path: '/user/accout/login',
        name: 'login',
        component: UserLoginView
    },
    {
        path: '/user/accout/register',
        name: 'register',
        component: UserRegisterView
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

export default router
