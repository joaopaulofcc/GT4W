import axios from 'axios';
const API_URL = 'http://localhost:8080/api';

export class APIService
{

    constructor()
    {
    }

    createPessoa(pk, nome, uf, dataNasc, peso)
    {
        const url = `${API_URL}/pessoas/`;
        return axios.post(url, {cpf: pk, nome: nome, uf: uf, dataNasc: dataNasc, peso: peso}).then(response => response);
    }

    editPessoa(pk, nome, uf, dataNasc, peso)
    {
        const url = `${API_URL}/pessoas/${pk}`;
        return axios.put(url, {cpf: pk, nome: nome, uf: uf, dataNasc: dataNasc, peso: peso}).then(response => response);
    }

    getPessoas()
    {
        const url = `${API_URL}/pessoas/`;
        return axios.get(url).then(response => response);
    }

    deletePessoa(pk)
    {
        const url = `${API_URL}/pessoas/${pk}`;
        return axios.delete(url).then(response => response);
    }

}