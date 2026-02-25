<template>
        <ContentField>
            <table class="table table-striped table-hover" style="text-align: center;">
                <thead>
                    <tr>
                        <th>玩家</th>
                        <th>天梯分</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="userItem in users" :key="userItem.id">
                        <td>
                            <img :src="userItem.avatar" alt="Avatar" style="width: 30px; height: 30px; border-radius: 50%; margin-right: 5px;">
                            {{ userItem.username }}
                        </td>
                        <td>
                            {{ userItem.rating }}
                        </td>
                    </tr>
                </tbody>
            </table>
            <nav aria-label="...">
            <ul class="pagination" style="float: right;">
                <li class="page-item">
                    <a @click="click_page(-2)" href="#" class="page-link">Previous</a>
                </li>
                <li :class="'page-item ' + page.active" v-for="page in pages" :key="page.number">
                    <a @click="click_page(page.number)" class="page-link" href="#">{{ page.number }}</a>
                </li>
                <li class="page-item">
                    <a @click="click_page(-1)" class="page-link" href="#">Next</a>
                </li>

            </ul>
            </nav>
        </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue'
import { useStore } from 'vuex'
import { ref } from 'vue'
import axios from 'axios';

export default {
    name: 'RanklistIndexView',
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let current_page = 1;
        let users = ref([]);
        let total_users = 0;
        let pages = ref([]);

        const click_page = page => {
            if (page === -2) page = current_page - 1;
            else if (page === -1) page = current_page + 1;
            let max_pages = parseInt(Math.ceil(total_users / 10));

            if (page >= 1 && page <= max_pages) {
                refresh_users(page);
            }
        }

        const update_pages = () => {
            let max_pages = parseInt(total_users / 10);
            let new_pages = [];
            for(let i = current_page - 2; i <= current_page + 2; i ++) {
                if (i > 0 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        active: i === current_page ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        };

        const refresh_users = (page) => {
            if (page) current_page = page;
            axios({
                url: "http://127.0.0.1:3000/ranklist/",
                method: "get",
                params: {
                    pageID: current_page,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            }).then(resp => {
                console.log(resp);
                users.value = resp.data.users;
                total_users = resp.data.user_count;
                update_pages();
            })
        };
        refresh_users();
        return {
            users,
            total_users,
            pages,
            click_page,
        }
    }
}
</script>

<style scoped>

</style>