<template>
    <l-map style="height: 760px; width: 100%" :zoom="zoom" :center="center">
        <l-control-layers ref="layersControl"></l-control-layers>
        <l-tile-layer :attribution="attribution" layer-type="base" name="Mapa" opacity="0.9" :url="url"></l-tile-layer>
        <l-wms-tile-layer
                v-for="layer in layers"
                :key="layer.name"
                :base-url="baseUrl"
                :layers="layer.layers"
                :visible="layer.visible"
                :name="layer.name"
                :format="layer.format"
                :transparent="layer.transparent"
                attribution='&copy; <a href="https://www.gt4w.com.br/">GT4W</a> contributors'
                layer-type="overlay">
        </l-wms-tile-layer>
        <l-layer-group v-on:add="addMarkersOverlay">
            <template v-for="item in markers">
                <l-marker v-if="item.number > 0" :key="item.id" :lat-lng="item.latlng" @l-add="$event.target.openPopup()">
                    <l-popup :content="item.content"></l-popup>
                </l-marker>
            </template>
        </l-layer-group>
    </l-map>
</template>

<script>

    import L from 'leaflet';
    import { LMap, LTileLayer, LPopup, LWMSTileLayer, LControlLayers, LMarker, LLayerGroup} from 'vue2-leaflet';

    export default {
        name: "Leaflet",
        props:['pessoas'],
        components: {
            LMap,
            LTileLayer,
            LControlLayers,
            LMarker,
            LPopup,
            LLayerGroup,
            'l-wms-tile-layer': LWMSTileLayer
        },

        data() {
            return {
                zoom: 4,
                center: L.latLng(-14, -55),
                url:'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
                attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community',
                baseUrl: 'http://sistemas.gt4w.com.br/geoserver/processo_seletivo/wms',
                layers: [
                    {
                        name: 'UFs Brasil',
                        visible: true,
                        format: "image/png",
                        transparent: true,
                        layers: 'processo_seletivo:ufs_brasil'
                    },
                ],
                markers: [],

                ufsDatas: {
                    "AC": {"lat": "-9.974", "long": "-67.8076", "number": 0, "uf": "AC"},
                    "AL": {"lat": "-9.3957", "long": "-35.4407", "number": 0, "uf": "AL"},
                    "AP": {"lat": "0.0220", "long": "-51.0359", "number": 0, "uf": "AP"},
                    "AM": {"lat": "-3.0607", "long": "-60.0130", "number": 0, "uf": "AM"},
                    "BA": {"lat": "-12.5816", "long": "-38.3039", "number": 0, "uf": "BA"},
                    "CE": {"lat": "-3.7183", "long": "-38.5434", "number": 0, "uf": "CE"},
                    "DF": {"lat": "-15.4647", "long": "-47.5547", "number": 0, "uf": "DF"},
                    "ES": {"lat": "-20.1910", "long": "-40.2016", "number": 0, "uf": "ES"},
                    "GO": {"lat": "-16.4043", "long": "-49.1514", "number": 0, "uf": "GO"},
                    "MA": {"lat": "-2.3147", "long": "-44.1810", "number": 0, "uf": "MA"},
                    "MT": {"lat": "-15.3546", "long": "-56.0558", "number": 0, "uf": "MT"},
                    "MS": {"lat": "-20.2634", "long": "-54.3847", "number": 0, "uf": "MS"},
                    "MG": {"lat": "-19.8157", "long": "-43.9542", "number": 0, "uf": "MG"},
                    "PA": {"lat": "-1.4550", "long": "-48.5024", "number": 0, "uf": "PA"},
                    "PB": {"lat": "-7.0542", "long": "-34.8617", "number": 0, "uf": "PB"},
                    "PR": {"lat": "-25.2540", "long": "-49.1623", "number": 0, "uf": "PR"},
                    "PE": {"lat": "-8.0542", "long": "-34.8813", "number": 0, "uf": "PE"},
                    "PI": {"lat": "-5.0521", "long": "-42.4807", "number": 0, "uf": "PI"},
                    "RJ": {"lat": "-22.5410", "long": "-43.1227", "number": 0, "uf": "RJ"},
                    "RN": {"lat": "-5.7944", "long": "-35.2115", "number": 0, "uf": "RN"},
                    "RS": {"lat": "-30.0159", "long": "-51.1348", "number": 0, "uf": "RS"},
                    "RO": {"lat": "-8.7618", "long": "-63.9028", "number": 0, "uf": "RO"},
                    "RR": {"lat": "2.8195", "long": "-60.6714", "number": 0, "uf": "RR"},
                    "SC": {"lat": "-27.3548", "long": "-48.3257", "number": 0, "uf": "SC"},
                    "SE": {"lat": "-10.5440", "long": "-37.0418", "number": 0, "uf": "SE"},
                    "SP": {"lat": "-23.3251", "long": "-46.3810", "number": 0, "uf": "SP"},
                    "TO": {"lat": "-10.1246", "long": "-48.2137", "number": 0, "uf": "TO"},
                },


            }
        },
        mounted() {

            this.populateUfsDatas();

            for (const key of Object.entries(this.ufsDatas)) {
                this.markers.push({
                    id: key[0],
                    number: this.ufsDatas[key[0]]["number"],
                    latlng: L.latLng(this.ufsDatas[key[0]]["lat"], this.ufsDatas[key[0]]["long"]),
                    content: "UF: " + key[0] + " | Total de Pessoas: " + this.ufsDatas[key[0]]["number"]
                })
            }
        },
        methods: {

            populateUfsDatas(){
                for (const pessoa of this.pessoas) {
                    this.ufsDatas[pessoa["uf"]]["number"] = this.ufsDatas[pessoa["uf"]]["number"] + 1;
                }
            },

            addMarkersOverlay: function (event) {
                const layersControl = this.$refs.layersControl.mapObject;
                layersControl.addOverlay(event.target, 'Mostrar/exibir marcadores');
                event.target.removeEventListener('add');
            },
        }
    };
</script>