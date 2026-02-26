<template>
        <ContentField>
            <table class="table table-striped table-hover" style="text-align: center;">
                <thead>
                    <tr>
                        <th>对局ID</th>
                        <th>1P</th>
                        <th>2P</th>
                        <th>对局胜者</th>
                        <th>对局时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="recordItem in records" :key="recordItem.record.id">
                        <td>
                            <span :data-bs-target="'#view-record-modal-' + recordItem.record.id">
                                {{ recordItem.record.id }}
                            </span>
                        </td>
                        <td>
                            <img :src="recordItem.a_avatar" alt="1P Avatar" style="width: 30px; height: 30px; border-radius: 50%; margin-right: 5px;">
                            {{ recordItem.a_username }}
                        </td>
                        <td>
                            <img :src="recordItem.b_avatar" alt="2P Avatar" style="width: 30px; height: 30px; border-radius: 50%; margin-right: 5px;">
                            {{ recordItem.b_username }}
                        </td>
                        <td>{{ recordItem.winner }}</td>
                        <td>{{ recordItem.record.createTime }}</td>
                        <td>
                            <button type="button" @click="view_record(recordItem.record.id)" class="btn btn-primary">查看录像</button>
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
import router from '@/router/index.js';
export default {
    name: 'RecordIndexView',
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let current_page = 1;
        let records = ref([]);
        let total_records = 0;
        let pages = ref([]);

        const click_page = page => {
            if (page === -2) page = current_page - 1;
            else if (page === -1) page = current_page + 1;
            let max_pages = parseInt(Math.ceil(total_records / 10));

            if (page >= 1 && page <= max_pages) {
                refresh_records(page);
            }
        }

        const update_pages = () => {
            let max_pages = parseInt(total_records / 10);
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

        const refresh_records = (page) => {
            if (page) current_page = page;
            axios({
                url: "http://47.97.71.119/api/record/list/",
                method: "get",
                params: {
                    pageID: current_page,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            }).then(resp => {
                console.log(resp);
                records.value = resp.data.items;
                total_records = resp.data.record_count;
                update_pages();
            })
        };
        refresh_records();
        const StringTo2D = map => {
            let g = [];
            for(let i = 0, k = 0; i < 13; i ++) {
                let line = [];
                for(let j = 0; j < 14; j ++, k ++) {
                    if (map[k] === '0') {
                        line.push(0);
                    } else {
                        line.push(1);
                    }
                }
                g.push(line);
            }
            return g;
        }
        const view_record = (record_id) => {
            for(const recordItem of records.value) {
                if(recordItem.record.id === record_id) {
                    console.log("a 的操作：" + recordItem.record.asteps);
                    store.commit("updateIsRecord", true);
                    store.commit("updateGame", {
                        gameMap: StringTo2D(recordItem.record.map),
                        a_id: recordItem.record.aid,
                        a_sx: recordItem.record.asx,
                        a_sy: recordItem.record.asy,
                        b_id: recordItem.record.bid,
                        b_sx: recordItem.record.bsx,
                        b_sy: recordItem.record.bsy,
                    });
                    store.commit("updateSteps", {
                        a_steps: recordItem.record.asteps,
                        b_steps: recordItem.record.bsteps,
                    });
                    store.commit("updateWinner", recordItem.record.winner);
                    router.push({ 
                        name: 'record_content_view', 
                        params: { record_id } 
                    });
                    break;
                }
            }
        };
        return {
            records,
            total_records,
            view_record,
            pages,
            click_page,
        }
    }
}
</script>

<style scoped>

</style>