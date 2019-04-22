# Aplicação Cliente ![VueJS 3.3.3](https://img.shields.io/badge/vue-3.5.5-green.svg)
Essa aplicação foi desenvolvida utilizando o framework VueJS 3.5.5. Seu objetivo é fornecer um frontend para a aplicação "server" contida também nesse repositório.

Ela fornece as seguintes funcionalidades:

    1. Tela de CRUD:
        - Criar novo registro de Pessoa;
        - Editar registro já existente;
        - Remoção de registro;
        - Visualização dos dados em tabela com paginação do lado cliente;
        - Validação de campos e utilização de máscaras;
        - Filtro para busca nos dados exibidos;
        - Ordenação de colunas da tabela de exibição.
    
    2. Exibição de informações geográficas utilizando LeafletJS, por meio da biblioteca Vue2leaflet:
        - Utilização de mapa base com imagens de satélite;
        - Utilização de camada WMS fornecida pela GT4W (com opção de ocultar/exibir);
        - Exibição de Markers e PopUps com a contagem de Pessoas cadastradas em cada estado;

## Execução da Aplicação

### Modo de Desenvolvimento

```sh
$ cd client
$ yarn install
$ yarn run serve
```

### Modo de Produção
```sh
$ cd client
$ yarn install
$ yarn run build
```


## Dependências

| Biblioteca | URL |
| ------ | ------ |
| BootstrapVue | https://github.com/KoRiGaN/Vue2Leaflet |
| VueTheMask | https://github.com/vuejs-tips/vue-the-mask |
| VuejsDialog | https://www.npmjs.com/package/vuejs-dialog |
| FontAwesomeIcon | https://github.com/FortAwesome/vue-fontawesome |
| LeafletJS | https://www.npmjs.com/package/vue2-leaflet |
| Vue2Leaflet | https://github.com/KoRiGaN/Vue2Leaflet |
| Axios | https://github.com/axios/axios |