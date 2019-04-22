<template>
  <div id="app">
      <b-row></b-row>
        <b-navbar type="dark" variant="dark">
            <b-navbar-nav>
                <b-nav-item href="#" @click="actualPage = 'crud'">CRUD</b-nav-item>
                <b-nav-item href="#" @click="actualPage = 'leaflet'">Leaflet</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <section v-if="actualPage === 'crud'">
            <Pessoas v-on:pessoasChanged="pessoasChanged" :pessoas="pessoas"></Pessoas>
        </section>
        <section v-else>
            <Leaflet v-on:pessoasChanged="pessoasChanged" :pessoas="pessoas"></Leaflet>
        </section>
        <footer class="footer">
            <div class="container">
                <span class="text-muted">Desenvolvido por <a href="https://www.about.me/joaopaulofcc">João Paulo F. C. César</a></span>
            </div>
        </footer>

  </div>
</template>

<script>
  import Pessoas from "./components/Pessoas";
  import Leaflet from "./components/Leaflet";
  export default {
    components: {
        Leaflet,
        Pessoas,
    },

    data() {

      return {
          pessoas: [],
          actualPage: 'crud'
      };

    },

    methods: {
        pessoasChanged:function(pessoas){
            this.pessoas = pessoas;
        }
    }

  }
</script>

<style>
  body {
    margin-top: 0px
  }
  .footer {
      position: absolute;
      bottom: 0;
      width: 100%;
      height: 40px;
      text-align: center;
      line-height: 40px;
      background-color: #343a40;
      font-family: 'Open Sans', sans-serif;
      font-weight: 300;
      color: #7f8c8d;
      padding-bottom: 0px;
      font-size: 14px;
  }
</style>