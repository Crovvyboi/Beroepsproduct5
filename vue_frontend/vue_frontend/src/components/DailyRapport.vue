<template>
    <div>
        <v-card flat title="Graphs">
            <Line  id="phLine"           
                v-if="loaded" :data="pHdata" 
                :options="chartOptions" 
                style="width: 1000px; height: 350px;"/> <br>
            <Line  id="phLine"           
                v-if="loaded" :data="tempData" 
                :options="chartOptions" 
                style="width: 1000px; height: 350px;"/> <br>
            <Line  id="phLine"           
                v-if="loaded" :data="o2Data" 
                :options="chartOptions" 
                style="width: 1000px; height: 350px;"/> <br>
        </v-card>

    </div>
    <div>
        <v-card flat title="Outliers">
            <template v-slot:text>
                <v-text-field
                    v-model="search"
                    label="Search"
                    prepend-inner-icon="mdi-magnify"
                    single-line
                    variant="outlined"
                    hide-details />
            </template>
            <v-data-table
                v-model:page="page"
                v-if="loaded"    
                :headers="outlierTableHeaders"
                :items="outlierTableItems"
                :search="search"
                :items-per-page="itemsPerPage"
                height="350"
            >   
                <template v-slot:top>
                    <v-text-field
                        :model-value="itemsPerPage"
                        class="pa-2"
                        hide-details
                        label="Items per page"
                        min="-1"
                        max="15"
                        type="number"
                        @update:model-value="itemsPerPage = parseInt($event, 10)"
                    ></v-text-field>
                </template>

                <template v-slot:item.pH="{value}">
                    <v-chip :color="getPhColor(value)">
                        {{ value }}
                    </v-chip>
                </template>
                <template v-slot:item.temp="{value}">
                    <v-chip :color="getTempColor(value)">
                        {{ value }}
                    </v-chip>
                </template>
                <template v-slot:item.o2="{value}">
                    <v-chip :color="getO2Color(value)">
                        {{ value }}
                    </v-chip>
                </template>

                <template v-slot:bottom>
                <div class="text-center pt-2">
                    <v-pagination
                    v-model="page"
                    :length="pageCount"
                    ></v-pagination>
                </div>
                </template>

            </v-data-table>
        </v-card>
    </div> <br>
    

</template>
  
<script>
    import {Chart, Line} from 'vue-chartjs'
    import axios from 'axios'
    import {
        Chart as ChartJS,
        CategoryScale,
        LinearScale,
        PointElement,
        LineElement,
        Title,
        Tooltip,
        Legend
    } from 'chart.js'

    ChartJS.register(
        CategoryScale,
        LinearScale,
        PointElement,
        LineElement,
        Title,
        Tooltip,
        Legend,
        
    )

    export default{
        
        name: 'DailyRapport',
        components: {Line},
        data(){
            return{
                search: '',
                page: 1,
                itemsPerPage: 10,

                loaded: false,
                pHdata: null,
                tempData: null,
                o2Data: null,

                outlierTableHeaders: null,
                outlierTableItems: null,
            }
        },
        async mounted(){
            console.log("Async started")
            this.loaded = false

            // Setup entry arrays
            const phEntries = []
            const tempEntries = []
            const o2Entries = []
            const dateEntries = []

            const outlierTableHeaders = [
                {
                    title: 'pH-value',
                    align: 'end',
                    key: 'pH'
                },
                {
                    title: 'Temperature',
                    align: 'end',
                    key: 'temp'
                },
                {
                    title: 'O2',
                    align: 'end',
                    key: 'o2'
                },
                {
                    title: 'Time',
                    align: 'start',
                    key: 'time'
                }
            ]
            
            const outlierTableItems = [

            ]

            // Get data from api and put into arrays
            try{
                await axios.get("http://localhost:8080/api/bydate/?day=3&month=11&year=2023")
                    .then(response => {
                        console.log(response)

                        response.data.Object.forEach(element => {
                            var outlierJson = {
                                pH: 0,
                                temp: 0,
                                o2: 0,
                                time: element.received_at.$date,
                                outlier: false
                            }


                            dateEntries.push(element.received_at.$date)
                            if ('pH' in element.sensor_readings && element.sensor_readings.pH != null) {
                                phEntries.push(element.sensor_readings.pH)
                                if (element.sensor_readings.pH < 4.5 || element.sensor_readings.pH > 7) {
                                    outlierJson.pH = element.sensor_readings.pH
                                    outlierJson.outlier = true
                                }
                                else{
                                    outlierJson.pH = element.sensor_readings.pH
                                }
                            }
                            else{
                                phEntries.push("NaN")
                                outlierJson.pH = "NaN"
                            }

                            if ('Temperatuur' in element.sensor_readings && element.sensor_readings.Temperatuur != null) {
                                tempEntries.push(element.sensor_readings.Temperatuur)
                                if (element.sensor_readings.Temperatuur < 20 || element.sensor_readings.Temperatuur > 25) {
                                    outlierJson.temp = element.sensor_readings.Temperatuur
                                    outlierJson.outlier = true
                                }
                                else{
                                    outlierJson.temp = element.sensor_readings.Temperatuur
                                }
                            }
                            else{
                                tempEntries.push("NaN")
                                outlierJson.temp = "NaN"
                            }

                            if ('O2' in element.sensor_readings && element.sensor_readings.O2 != null) {
                                o2Entries.push(element.sensor_readings.O2)
                                if (element.sensor_readings.O2 < 6 || element.sensor_readings.O2 > 6.5) {
                                    outlierJson.o2 = element.sensor_readings.O2
                                    outlierJson.outlier = true
                                }
                                else{
                                    outlierJson.o2 = element.sensor_readings.O2
                                }
                            }
                            else{
                                o2Entries.push("NaN")
                                outlierJson.o2 = "NaN"
                            }

                            // Assign outlier data
                            if (outlierJson.outlier) {
                                outlierTableItems.push(outlierJson)
                            }
                        });

                    })

                // Create data sets
                const pHdata = {
                    labels: dateEntries,
                    datasets: [
                    {
                        data: phEntries,
                        label: "ph-values",
                        borderColor: "blue"
                    }]
                }
                const tempData = {
                    labels: dateEntries,
                    datasets: [
                    {
                        data: tempEntries,
                        label: "temperature",
                        borderColor: "red"
                    }]
                }
                const o2Data = {
                    labels: dateEntries,
                    datasets: [
                    {
                        data: o2Entries,
                        label: "o2",
                        borderColor: "green"
                    }]
                }

                console.log(pHdata)
                console.log(tempData)
                console.log(o2Data)

                // Apply data sets to data return
                this.pHdata = pHdata
                this.tempData = tempData
                this.o2Data = o2Data

                this.outlierTableHeaders = outlierTableHeaders
                this.outlierTableItems = outlierTableItems

                this.loaded = true

                console.log("Async ended")
                
            } catch(e){
                console.error(e)
            }
        },
        methods:{
            getPhColor(pH){
                if (pH < 4.5 || pH > 7) {
                    return 'red'
                }
            },
            getTempColor(Temp){
                if (Temp < 20 || Temp > 25) {
                    return 'red'
                }
            },
            getO2Color(o2){
                if (o2 < 6 || o2 > 6.5) {
                    return 'red'
                }
            }
        },
        computed: {
            pageCount () {
                return Math.ceil(this.outlierTableItems.length / this.itemsPerPage)
            },
        },
    }

</script>