<template>
    <div  v-if="loaded"     >
        <div>
            <VueDatePicker id="manual" :transitions="false" :max-date="new Date()" v-model="date" 
                    @date-update="onDateChange" 
                    @closed="onTimePickerClose"
                    @cleared="onTimePickerClose"
                    :clearable="true"                 
                    :width="1000"
                    :height="500"/>
        </div>
        <div>
            <v-card flat title="Entries"
                        :width="1000">
                <template v-slot:text>
                    <v-text-field
                        v-model="search"
                        label="Search"
                        prepend-inner-icon="mdi-magnify"
                        single-line
                        variant="outlined"
                        hide-details />
                </template>
                <template
                    v-for="(header, i) in entryHeaders"
                    v-slot:[`header.${header.key}`]="{ }"
                >
                    {{ header.title }}
                    <div @click.stop :key="i">
                    <v-text-field
                        :key="i"
                        
                        class="pa"
                        type="text"
                        :placeholder="header.key"
                    ></v-text-field>
                    </div>
                </template>
                <v-data-table
                    id="entryTable"
                    v-model:page="page"
                    :filters="filters"
                    :items="entries"
                    :search="search"
                    :items-per-page="itemsPerPage"
                    height="350"
                >   
                    <template v-slot:top>
                        <v-select
                            :model-value="itemsPerPage"
                            :items="perPageOptions"
                            attach
                            hide-details
                            label="Items per page"
                            @update:model-value="itemsPerPage = parseInt($event, 10)" />
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
        </div>
        <!-- Graphs -->
        <div>
            <v-card flat title="Graphs">
                <Line  id="phLine"           
                    :data="pHdata" 
                    :options="phBox"
                    :width="1000"
                    :height="500"/> <br>
                <Line  id="phLine"           
                    :data="tempData" 
                    :options="tempBox" 
                    :width="1000"
                    :height="500"/> <br>
                <Line  id="phLine"           
                    :data="o2Data" 
                    :options="o2Box" 
                    :width="1000"
                    :height="500"/> <br>
            </v-card>

        </div>
    </div>

</template>

<script>
    import {Chart, Line} from 'vue-chartjs'
    import VueDatePicker from '@vuepic/vue-datepicker'
    import '@vuepic/vue-datepicker/dist/main.css'
    import { ref } from 'vue'
    import axios from 'axios'
    import dayjs from 'dayjs'
    import annotationPlugin from 'chartjs-plugin-annotation'
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
        annotationPlugin,
        
    )

    export default{  
        name: 'EntryPage',
        components: {Line, VueDatePicker},
        data(){
            return{
                search: '',
                page: 1,
                itemsPerPage: 10,
                perPageOptions: [10, 25, 50, 100],

                sensorVariableParameters: {
                    pHmin: 4.5,
                    pHmax: 7,
                    tempmin: 20,
                    tempmax: 25,
                    o2min: 6,
                    o2max: 6.5
                },

                date: null,

                loaded: false,
                entryHeaders: [
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
                        title: 'Date',
                        align: 'start',
                        key: 'date',
                    },
                    {
                        title: 'Time',
                        align: 'start',
                        key: 'time'
                    }
                ],
                entries: null,

                pHdata: null,
                tempData: null,
                o2Data: null,


                phBox:
                {
                    plugins: {
                        annotation: {
                            annotations: {
                                box1: {
                                    // Indicates the type of annotation
                                    type: 'box',
                                    yMin: 4.5,
                                    yMax: 6,
                                    backgroundColor: 'rgba(42, 220, 39, 0.25)'
                                }
                            }
                        }
                    },
                    responsive: false,
                    maintainAspectRatio: false
                },
                tempBox:
                {
                    plugins: {
                        annotation: {
                            annotations: {
                            box1: {
                                // Indicates the type of annotation
                                type: 'box',
                                yMin: 20,
                                yMax: 25,
                                backgroundColor: 'rgba(42, 220, 39, 0.25)'
                            }
                        }
                        }
                    },
                    responsive: false,
                    maintainAspectRatio: false
                },
                o2Box:
                {
                    plugins: {
                        annotation: {
                            annotations: {
                            box1: {
                                // Indicates the type of annotation
                                type: 'box',
                                yMin: 6,
                                yMax: 6.5,
                                backgroundColor: 'rgba(42, 220, 39, 0.25)'
                            }
                        }
                        }
                    },
                    responsive: false,
                    maintainAspectRatio: false
                }
            }
        },
        async mounted(){
            this.updateData(this.sensorVariableParameters)
        },
        methods: {
            getPhColor(pH){
                if (pH < this.sensorVariableParameters.pHmin || pH > this.sensorVariableParameters.pHmax) {
                    return 'red'
                }
            },
            getTempColor(Temp){
                if (Temp < this.sensorVariableParameters.tempmin || Temp > this.sensorVariableParameters.tempmax) {
                    return 'red'
                }
            },
            getO2Color(o2){
                if (o2 < this.sensorVariableParameters.o2min || o2 > this.sensorVariableParameters.o2max) {
                    return 'red'
                }
            },
            formatTime(dateString) {
                // https://day.js.org/docs/en/display/format
                const date = dayjs(dateString);
                return date.format('HH:mm:ss');
            },
            formatDate(dateString) {
                // https://day.js.org/docs/en/display/format
                const date = dayjs(dateString);
                return date.format('DD MMM YYYY');
            },
            onDateChange(value){
                this.date = value
            },
            onTimePickerClose(){
                this.updateData(this.sensorVariableParameters)
            },
            async updateData(sensorVariableParameters){
                console.log("Async started")
                this.loaded = false

                // Setup entry arrays
                const entries = []

                const phEntries = []
                const tempEntries = []
                const o2Entries = []
                const dateEntries = []

                var connString = ""
                if (this.date != null) {
                    var day = this.date.getDate()
                    var month = this.date.getMonth() + 1
                    var year = this.date.getFullYear()

                    console.log(day + " " + month  + " " + year)

                    connString = "http://localhost:8080/api/bydate/?day="+day+"&month="+month+"&year="+year+""
                }
                else{
                    connString = "http://localhost:8080/api/"
                }

                // Get data from api and put into arrays
                try{
                    await axios.get(connString)
                        .then(response => {
                            console.log(response)

                            if (response.data != "") {

                                response.data.Object.forEach(element => {

                                    var entryJson = {
                                        pH: 0,
                                        temp: 0,
                                        o2: 0,
                                        date: this.formatDate(element.received_at.$date),
                                        time: this.formatTime(element.received_at.$date)
                                    }

                                    dateEntries.push(this.formatDate(element.received_at.$date))
                                    if ('pH' in element.sensor_readings && element.sensor_readings.pH != null) {
                                        phEntries.push(element.sensor_readings.pH)
                                        entryJson.pH = element.sensor_readings.pH
                                    }
                                    else{
                                        phEntries.push("NaN")
                                        entryJson.pH = "NaN"
                                    }

                                    if ('Temperatuur' in element.sensor_readings && element.sensor_readings.Temperatuur != null) {
                                        tempEntries.push(element.sensor_readings.Temperatuur)
                                        entryJson.temp = element.sensor_readings.Temperatuur
                                    }
                                    else{
                                        tempEntries.push("NaN")
                                        entryJson.temp = "NaN"
                                    }

                                    if ('O2' in element.sensor_readings && element.sensor_readings.O2 != null) {
                                        o2Entries.push(element.sensor_readings.O2)
                                        entryJson.o2 = element.sensor_readings.O2
                                    }
                                    else{
                                        o2Entries.push("NaN")
                                        entryJson.o2 = "NaN"
                                    }

                                    entries.push(entryJson)
                                });
                            }

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

                    // Apply data sets to data return
                    this.entries = entries
                    this.pHdata = pHdata
                    this.tempData = tempData
                    this.o2Data = o2Data

                    this.loaded = true

                    console.log("Async ended")
                } catch(e){
                    console.error(e)
                }
            }
        },
        computed: {
            pageCount () {
                return Math.ceil(this.entries.length / this.itemsPerPage)
            },
        },
    }
</script>