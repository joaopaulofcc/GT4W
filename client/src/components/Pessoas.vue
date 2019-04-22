<template>
    <b-container fluid>
        <div class="row mt-3"></div>
        <b-row>
            <b-col>
                <b-form-group>
                    <b-input-group>
                        <b-form-input size="sm" v-model="filter" placeholder="Digite para filtrar"></b-form-input>
                        <b-input-group-append>
                            <b-button size="sm" :disabled="!filter" @click="filter = ''">Limpar</b-button>
                        </b-input-group-append>
                    </b-input-group>
                </b-form-group>
            </b-col>
            <b-col cols="" class="text-center">
                <b-button size="sm" @click="refreshTable"><font-awesome-icon icon="sync"/></b-button>
                &nbsp;
                <b-button size="sm" v-b-modal.modal-create @click="cleanVariables"><font-awesome-icon icon="user-plus"/></b-button>
            </b-col>
            <section v-if="totalPessoas === 1">
                <b-col class="text-right">
                    {{totalPessoas}} registro cadastrado
                </b-col>
            </section>
            <section v-else>
                <b-col class="text-right">
                    {{totalPessoas}} registros cadastrados
                </b-col>
            </section>

        </b-row>
        <b-alert variant="danger" dismissible :show="dismissCountDownError" @dismissed="dismissCountDownError=0" @dismiss-count-down="countDownChangedError">
            Erro ao comunicar com o servidor, tente novamente mais tarde!
            <b-progress
                    variant="warning"
                    :max="dismissSecsError"
                    :value="dismissCountDownError"
                    height="4px"
            ></b-progress>
        </b-alert>
        <b-alert variant="success" dismissible :show="dismissCountDownSuccess" @dismissed="dismissCountDownSuccess=0" @dismiss-count-down="countDownChangedSuccess">
            Operação realizada com sucesso!
            <b-progress
                    variant="warning"
                    :max="dismissSecsSuccess"
                    :value="dismissCountDownSuccess"
                    height="4px"
            ></b-progress>
        </b-alert>
        <b-table ref="dataTable" id="dataTable" dark hover bordered responsive :per-page="perPage" :current-page="currentPage" :busy="isBusy" :items="pessoas" :fields="fields" :filter="filter" @filtered="onFiltered">
            <template slot="actions" slot-scope="data">
                <b-button size="sm" v-b-modal.modal-edit @click="getRowData(data.item)"><font-awesome-icon icon="edit"/></b-button>
                &nbsp;
                <b-button size="sm" @click="deletePessoa(data.item.cpf, data.index)"><font-awesome-icon icon="trash"/></b-button>
            </template>
            <div slot="table-busy" class="text-center text-danger my-2">
                <b-spinner class="align-middle"></b-spinner>
                <strong> Carregando...</strong>
            </div>
        </b-table>
        <b-pagination
            v-model="currentPage"
            :total-rows="totalPessoas"
            :per-page="perPage"
            aria-controls="dataTable"
            align="right"
            :pageClass="'page-item'">
        ></b-pagination>

        <b-modal id="modal-edit" ref="modal-edit" header-bg-variant="dark" header-text-variant="light" body-bg-variant="dark" footer-bg-variant="light" title="Editar">
            <b-card class="border-0" bg-variant="dark" text-variant="white">
                <section v-if="errorIbge">
                    <p>Pedimos desculpas, não estamos conseguindo recuperar as informações do IBGE no momento. Por favor, tente novamente mais tarde.</p>
                </section>
                <section v-else>
                    <b-alert variant="danger" dismissible :show="cardAlert">
                        {{textCardAlert}}
                    </b-alert>
                    <b-form-group label-cols-lg="3" label="Dados Pessoais" label-size="lg" label-class="font-weight-bold pt-0" class="mb-0">

                        <b-form-group label-cols-sm="3" label="CPF:" label-align-sm="right" label-for="input_cpf_edit">
                            <the-mask mask="###.###.###-##" class="col-10" id="input_cpf_edit" v-model="newCpf" disabled/>
                        </b-form-group>

                        <b-form-group label-cols-sm="3" label="Nome:" label-align-sm="right" label-for="input_nome_edit">
                            <b-form-input class="col-10" id="input_nome_edit" v-model="newNome" required></b-form-input>
                        </b-form-group>

                        <b-form-group label-cols-sm="3" label="UF:" label-align-sm="right" label-for="select_uf_edit">
                            <b-form-select class="col-10" id="select_uf_edit" v-model="newUf">
                                <option v-for="uf in sortedUfs" :key="uf.sigla" :value="uf.sigla">{{uf.sigla}}</option>
                            </b-form-select>
                        </b-form-group>

                        <b-form-group label-cols-sm="3" label="Nascimento:" label-align-sm="right" label-for="input_datanasc_edit">
                            <b-form-input class="col-10" id="input_datanasc_edit" type="date" v-model="newDataNasc"></b-form-input>
                        </b-form-group>

                        <b-form-group label-cols-sm="3" label="Peso:" label-align-sm="right" label-for="input_peso_edit">
                            <b-form-input class="col-10" id="input_peso_edit" v-model="newPeso"></b-form-input>
                        </b-form-group>

                    </b-form-group>
                </section>
            </b-card>

            <div slot="modal-footer" class="w-100">
                <b-container fluid>
                    <b-row class="mb-1 float-right">
                        <b-col>
                            <b-button id="button_fechar_edit" variant="primary" size="sm" class="float-right" @click="hideModal('modal-edit')">
                                Fechar
                            </b-button>
                        </b-col>
                        <b-col>
                            <b-button id="button_salvar_edit" variant="outline-primary" size="sm" class="float-right" @click="editPessoa">
                                Salvar
                            </b-button>
                        </b-col>
                    </b-row>
                </b-container>
            </div>
        </b-modal>

        <b-modal id="modal-create" ref="modal-create" header-bg-variant="dark" header-text-variant="light" body-bg-variant="dark" footer-bg-variant="light" title="Cadastrar">

            <b-card class="border-0" bg-variant="dark" text-variant="white">
                <section v-if="errorIbge">
                    <p>Pedimos desculpas, não estamos conseguindo recuperar as informações no momento. Por favor, tente novamente mais tarde.</p>
                </section>
                <section v-else>
                    <b-alert variant="danger" dismissible :show="cardAlert">
                        {{textCardAlert}}
                    </b-alert>
                    <b-form-group label-cols-lg="3" label="Dados Pessoais" label-size="lg" label-class="font-weight-bold pt-0" class="mb-0">

                        <b-form-group label-cols-sm="3" label="CPF:" label-align-sm="right" label-for="input_cpf_create">
                            <the-mask mask="###.###.###-##" id="input_cpf_create" v-model="newCpf" required/>
                        </b-form-group>

                        <b-form-group label-cols-sm="3" label="Nome:" label-align-sm="right" label-for="input_nome_create">
                            <b-form-input id="input_nome_create" v-model="newNome" required></b-form-input>
                        </b-form-group>

                        <b-form-group label-cols-sm="3" label="UF:" label-align-sm="right" label-for="select_uf_create">
                            <b-form-select id="select_uf_create" v-model="newUf">
                                <option v-for="uf in sortedUfs" :key="uf.sigla" :value="uf.sigla">{{uf.sigla}}</option>
                            </b-form-select>
                        </b-form-group>

                        <b-form-group label-cols-sm="3" label="Nascimento:" label-align-sm="right" label-for="input_datanasc_create">
                            <b-form-input id="input_datanasc_create" type="date" v-model="newDataNasc"></b-form-input>
                        </b-form-group>

                        <b-form-group label-cols-sm="3" label="Peso:" label-align-sm="right" label-for="input_peso_create">
                            <the-mask :mask="['###.##', '##.##', '##.#', '##']" masked="true" id="input_peso_create" v-model="newPeso"/>
                        </b-form-group>

                    </b-form-group>
                </section>
            </b-card>

            <div slot="modal-footer" class="w-100">
                <b-container fluid>
                    <b-row class="mb-1 float-right">
                        <b-col>
                            <b-button id="button_fechar_create" variant="primary" size="sm" class="float-right" @click="hideModal('modal-create')">
                                Fechar
                            </b-button>
                        </b-col>
                        <b-col>
                            <b-button id="button_salvar_create" variant="outline-primary" size="sm" class="float-right" @click="createPessoa">
                                Salvar
                            </b-button>
                        </b-col>
                    </b-row>
                </b-container>
            </div>
        </b-modal>

    </b-container>
</template>

<script>

    import moment from 'moment'
    import axios from 'axios';

    import {APIService} from '../APIService';

    const apiService = new APIService();

    export default {

        name: 'ListaPessoas',
        props:['pessoas'],
        components: {

        },

        data() {

            return {

                perPage: 10,
                currentPage: 1,
                newCpf: null,
                newNome: null,
                newUf: null,
                newDataNasc: null,
                newPeso: null,
                totalPessoas: 0,

                dismissSecsError: 5,
                dismissCountDownError: 0,
                dismissSecsSuccess: 5,
                dismissCountDownSuccess: 0,

                cardAlert:false,
                textCardAlert: "",

                isBusy: false,

                ibgeUfs: [],
                fields: [],
                filter: null,
                rowData:[],
                errorIbge: false,
            };

        },

        methods: {

            showCardAlert(text) {
                this.cardAlert = true;
                this.textCardAlert = text;
            },

            hideCardAlert() {
                this.cardAlert = false;
            },

            showAlertError() {
                this.dismissCountDownError = this.dismissSecsError
            },

            countDownChangedError(dismissCountDown) {
                this.dismissCountDownError = dismissCountDown
            },

            showAlertSuccess() {
                this.dismissCountDownSuccess = this.dismissSecsSuccess
            },

            countDownChangedSuccess(dismissCountDown) {
                this.dismissCountDownSuccess = dismissCountDown
            },

            hideModal(modal) {
                this.$refs[modal].hide()
            },

            refreshTable(){
                this.getPessoas()
            },

            cleanVariables(){
                this.newCpf = "";
                this.newNome = "";
                this.newUf = "";
                this.newDataNasc = "";
                this.newPeso = "";
            },

            getRowData(rowData){
                this.newCpf = rowData.cpf;
                this.newNome = rowData.nome;
                this.newUf = rowData.uf;
                this.newDataNasc = rowData.dataNasc;
                this.newPeso = rowData.peso;
            },

            editPessoa(bvModalEvt) {
                bvModalEvt.preventDefault();
                if (this.newCpf.length !== 11 || this.newNome.length === 0 || this.newUf.length !== 2) {
                    this.showCardAlert("Os campos CPF, Nome e UF são obrigatórios!")
                } else {
                    apiService.editPessoa(this.newCpf, this.newNome, this.newUf, this.newDataNasc, this.newPeso).then(response => {
                        if (response.status === 200) {
                            this.refreshTable();
                            this.hideModal('modal-edit');
                            this.cleanVariables();
                            this.showAlertSuccess()
                        } else {
                            this.showAlertError()
                        }
                    });
                }
            },

            createPessoa(bvModalEvt) {
                bvModalEvt.preventDefault();
                if (this.newCpf.length !== 11 || this.newNome.length === 0 || this.newUf.length !== 2) {
                    this.showCardAlert("Os campos CPF, Nome e UF são obrigatórios!")
                } else {
                    apiService.createPessoa(this.newCpf, this.newNome, this.newUf, this.newDataNasc, this.newPeso).then(response => {
                        if (response.status === 200) {
                            this.pessoas.push({"cpf": this.newCpf, "nome": this.newNome, "uf": this.newUf, "dataNasc": this.newDataNasc, "peso": this.newPeso});
                            this.totalPessoas = this.pessoas.length;
                            this.hideModal('modal-create');
                            this.cleanVariables();
                            this.showAlertSuccess()
                        } else {
                            this.showAlertError()
                        }
                    });
                }
            },

            onFiltered(filteredItems) {
                this.totalPessoas = filteredItems.length;
                this.currentPage = 1;
            },

            getUf(){
                axios.get("https://servicodados.ibge.gov.br/api/v1/localidades/estados").then(data => {
                    this.ibgeUfs = data.data;
                })
                .catch(error => {
                    this.errorIbge = true;
                })
            },

            deletePessoa(cpf,index){
                let _this = this;
                this.$dialog
                    .confirm('Deseja mesmo remover esse cadastro?')
                    .then(function() {
                        apiService.deletePessoa(cpf).then(response => {
                            if (response.status === 200) {
                                _this.pessoas.splice(index, 1);
                                _this.totalPessoas = _this.pessoas.length;
                                _this.showAlertSuccess();
                            } else {
                                _this.showAlertError();
                            }
                        });
                    })
                    .catch(function() {
                    });

            },

            getPessoas(){
                apiService.getPessoas().then(response => {
                    if (response.status === 200) {
                        this.totalPessoas = response.data.totalElements;
                        this.pessoas = response.data.content;
                        this.fields = [
                            {
                                key: "cpf",
                                label: "CPF",
                                formatter: value => {
                                    return value.slice(0, 3) + "." + value.slice(3, 6) + "." + value.slice(6, 9) + "-" + value.slice(9, 11);
                                }
                            },
                            {
                                key: "nome",
                                label: "Nome",
                                sortable: true
                            },
                            {
                                key: "uf",
                                label: "UF",
                                sortable: true
                            },
                            {
                                key: "peso",
                                label: "Peso",
                                sortable: true
                            },
                            {
                                key: "dataNasc",
                                label: "Nascimento",
                                sortable: true,
                                formatter: value => {
                                    return moment(String(value)).format('DD/MM/YYYY')
                                }
                            },
                            {
                                key: "actions",
                                label: "Ações",
                                class: "text-center"
                            }];
                        this.$emit('pessoasChanged', this.pessoas);
                        this.showAlertSuccess()

                    } else {
                        this.showAlertError()
                    }
                });
            },
        },

        mounted() {
            this.isBusy = true;
            this.getPessoas();
            this.getUf();
            this.isBusy = false;
        },

        computed: {
            sortedUfs: function() {
                function compare(a, b) {
                    if (a.sigla < b.sigla)
                        return -1;
                    if (a.sigla > b.sigla)
                        return 1;
                    return 0;
                }

                return this.ibgeUfs.sort(compare);
            },
        }
    }
</script>

<style>
    .page-item.active .page-link {
        background-color: #6c757d;
        border-color: #6c757d;
    }

    .page-link {
        position: relative;
        display: block;
        padding: 0.5rem 0.75rem;
        margin-left: -1px;
        line-height: 1.25;
        color: #212529;
        background-color: #fff;
        border: 1px solid #dee2e6;
    }

    .page-link:focus {
        z-index: 2;
        outline: 0;
        -webkit-box-shadow: 0 0 0 0.2rem rgba(0, 0, 0, 0.25);
        box-shadow: 0 0 0 0.2rem rgba(222, 226, 230, 0.25);
    }
</style>