<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.avatar" alt="" style="width: 100%;">
                        <hr>
                        <div style="text-align: center; font-weight: bold; font-size: 150%;">{{ $store.state.user.username }}</div>
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%">我的Bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                            创建Bot
                        </button>

                        <!-- Modal -->
                        <div class="modal fade" id="add-bot-btn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">创建Bot</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">名称</label>
                                        <input v-model="botName" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">简介</label>
                                        <textarea v-model="description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-code" class="form-label">代码</label>
                                        <VAceEditor v-model:value="code" @init="editorInit" lang="c_cpp" theme="textmate" style="height: 300px" />
                                    </div>
                                </div>
                                    <div class="modal-footer">
                                        <div class="error-message">{{ error_message }}</div>
                                        <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>
                                        <span style="cursor: pointer; color: #0d6efd;" data-bs-toggle="modal" :data-bs-target="'#view-bot-modal-' + bot.id">
                                            {{ bot.botName }}
                                        </span>
                                        
                                        <!-- View Bot Modal -->
                                        <div class="modal fade" :id="'view-bot-modal-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Bot详情</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label class="form-label">名称</label>
                                                            <input type="text" class="form-control" :value="bot.botName" readonly>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">创建时间</label>
                                                            <input type="text" class="form-control" :value="bot.createTime" readonly>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">简介</label>
                                                            <textarea v-model="bot.description" class="form-control" rows="3" readonly></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">代码</label>
                                                            <VAceEditor v-model:value="bot.code" @init="editorInit" lang="c_cpp" theme="textmate" style="height: 300px" readonly />
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>{{ bot.createTime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-' + bot.id">修改</button>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>

                                        <div class="modal fade" :id="'update-bot-modal-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">修改Bot</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label :for="'update-bot-title-' + bot.id" class="form-label">名称</label>
                                                        <input v-model="bot.botName" type="text" class="form-control" :id="'update-bot-title-' + bot.id" placeholder="请输入Bot名称">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label :for="'update-bot-description-' + bot.id" class="form-label">简介</label>
                                                        <textarea v-model="bot.description" class="form-control" :id="'update-bot-description-' + bot.id" rows="3" placeholder="请输入Bot简介"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label :for="'update-bot-code-' + bot.id" class="form-label">代码</label>
                                                        <VAceEditor v-model:value="bot.code" @init="editorInit" lang="c_cpp" theme="textmate" style="height: 300px" />
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="error-message">{{ bot.error_message }}</div>
                                                    <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor'; 
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-textmate';
import 'ace-builds/src-noconflict/ext-language_tools';

export default {
    components: {
        VAceEditor
    },
    setup() {
        ace.config.set( "basePath", "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/" );
        const store = useStore();
        let bots = ref([]);
        const botName = ref('');
        const description = ref('');
        const code = ref('');
        const error_message = ref('');

        const refresh_bots = () => {
            axios({
                url: "http://127.0.0.1:3000/user/bot/botList",
                method: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            }).then(resp => {
                console.log(resp);
                bots.value = resp.data;
            })
        }

        onMounted(() => {
            store.dispatch("getInfo", {
                success: () => {
                    console.log("get info success");
                },
                error: () => {
                    console.log("get info error");
                }
            })
            refresh_bots();
        })

        const add_bot = () => {
            error_message.value = "";
            axios({
                url: "http://127.0.0.1:3000/user/bot/add",
                method: "post",
                data: {
                    botName: botName.value,
                    description: description.value,
                    code: code.value,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            }).then(resp => {
                if(resp.data.error_message === "success") {
                    botName.value = "";
                    description.value = "";
                    code.value = "";
                    Modal.getInstance(document.querySelector('#add-bot-btn')).hide();
                    refresh_bots();
                } else {
                    error_message.value = resp.data.error_message;
                }
            })
        }

        const update_bot = (bot) => {
            bot.error_message = "";
            axios({
                url: "http://127.0.0.1:3000/user/bot/update",
                method: "post",
                data: {
                    bot_id: bot.id,
                    botName: bot.botName,
                    description: bot.description,
                    code: bot.code,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            }).then(resp => {
                if(resp.data.error_message === "success") {
                    Modal.getInstance(document.querySelector('#update-bot-modal-' + bot.id)).hide();
                    refresh_bots();
                } else {
                    bot.error_message = resp.data.error_message;
                }
            })
        }

        const remove_bot = (bot) => {
            axios({
                url: "http://127.0.0.1:3000/user/bot/remove",
                method: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            }).then(resp => {
                if(resp.data.error_message === "success") {
                    refresh_bots();
                }
            })
        }

        const editorInit = () => {
            require('ace-builds/src-noconflict/ext-language_tools');
            require('ace-builds/src-noconflict/snippets/c_cpp');
        }

        return {
            bots,
            botName,
            description,
            code,
            error_message,
            add_bot,
            update_bot,
            remove_bot,
            editorInit,
        }
    }
}
</script>

<style scoped>
div.error-message {
    color: red;
}
</style>