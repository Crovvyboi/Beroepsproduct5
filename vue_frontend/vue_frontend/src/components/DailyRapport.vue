<template >
    <div v-if="loaded" >
        <!-- Date Picker -->
        <div>
            <VueDatePicker id="manual" :max-date="new Date()" v-model="date" 
                @date-update="onDateChange" 
                @closed="onTimePickerClose"                 
                :width="1000"
                :height="500"/>
        </div>
        <!-- Statistic Tables -->
        <div>
            <v-card flat title="Statistics">
                <div>
                    <v-data-table
                        id="entryStatTable"    
                        :headers="entryHeaders"
                        :items="entries"
                    >   
                        <template v-slot:bottom>
                        </template> 
                    </v-data-table>
                </div>
                <div>
                    <v-data-table
                        id="statTable"  
                        :headers="statisticHeaders"
                        :items="statistics"
                    >   
                        <template v-slot:bottom>
                        </template> 
                    </v-data-table>
                </div>

            </v-card>
            <v-card flat title="Weather data">
                <v-data-table
                    id="weatherTable"   
                    :headers="weatherHeaders"
                    :items="weatherData"
                >   
                    <template v-slot:bottom>
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
        <!-- Outlier Table -->
        <div>
            <v-card flat title="Outliers"
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
                <v-data-table
                    id="outlierTable"
                    v-model:page="page"
                    :headers="outlierTableHeaders"
                    :items="outlierTableItems"
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
        <!-- Predictions -->
        <div>
            <v-card flat title="Predictions">
                To be Implemented
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
        annotationPlugin
        
    )


    export default{
        
        name: 'DailyRapport',
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

                date: new Date(),

                loaded: false,
                pHdata: null,
                tempData: null,
                o2Data: null,

                outlierTableHeaders: null,
                outlierTableItems: null,

                entryHeaders:[
                    {
                        title: 'Entries',
                        align: 'start',
                        key: 'entries'
                    },
                    {
                        title: 'Entries yesterday',
                        align: 'start',
                        key: 'yestEntries'
                    },
                    {
                        title: 'Outliers',
                        align: 'start',
                        key: 'outlierCount'
                    },
                    {
                        title: 'Outliers yesterday',
                        align: 'start',
                        key: 'yestOutliers'
                    }],
                entries:[{
                    entries: 0,
                    yestEntries: 0,
                    outlierCount: 0,
                    yestOutliers: 0,
                }],
                statisticHeaders:[  
                    {
                        title: '',
                        align: 'start',
                        key: 'title'
                    },
                    {
                        title: 'Average',
                        align: 'start',
                        key: 'avg'
                    },
                    {
                        title: 'Average yesterday',
                        align: 'start',
                        key: 'avgYesterday'
                    },
                    {
                        title: 'Min value',
                        align: 'start',
                        key: 'min'
                    },
                    {
                        title: 'Max value',
                        align: 'start',
                        key: 'max'
                    },
                    {
                        title: 'Mode',
                        align: 'start',
                        key: 'mode'
                    },
                ],
                statistics: [
                    {
                        title: "pH-value",
                        avg: 0,
                        avgYesterday: 0,
                        min: 0,
                        max: 0,
                        mode: 0
                    },
                    {
                        title: "Temperature",
                        avg: 0,
                        avgYesterday: 0,
                        min: 0,
                        max: 0,
                        mode: 0
                    },
                    {
                        title: "O2",
                        avg: 0,
                        avgYesterday: 0,
                        min: 0,
                        max: 0,
                        mode: 0
                    }
                ],
                weatherHeaders:[
                    {
                        title: 'Average temp',
                        align: 'start',
                        key: 'temp'
                    },
                    {
                        title: 'Min temp',
                        align: 'start',
                        key: 'minTemp'
                    },
                    {
                        title: 'Max temp',
                        align: 'start',
                        key: 'maxTemp'
                    },
                    {
                        title: 'Humidity',
                        align: 'start',
                        key: 'humidity'
                    },
                    {
                        title: 'Sun chance',
                        align: 'start',
                        key: 'sunChance'
                    },
                    {
                        title: 'Sun up',
                        align: 'start',
                        key: 'sunUp'
                    },
                    {
                        title: 'Sun down',
                        align: 'start',
                        key: 'sunDown'
                    },
                ],
                weatherData: [{
                    temp: 0,
                    minTemp: 0,
                    maxTemp: 0,
                    humidity: 0,
                    sunChance: 0,
                    sunUp: 0,
                    sunDown: 0
                }],

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
        methods:{
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
            formatDate(dateString) {
                // https://day.js.org/docs/en/display/format
                const date = dayjs(dateString);
                return date.format('HH:mm:ss');
            },
            onDateChange(value){
                this.date = value
            },
            onTimePickerClose(){
                this.updateData(this.sensorVariableParameters)
            },
            getEntryStats(entries, entryObject){
                if (entries.length != 0) {
                    let min = entries[0]
                    let max = entries[0]
                    let sum = 0

                    // Mode
                    var counts = {}
                    let modecount = 0
                    let mode = 0

                    for(let i=0; i< entries.length;i++){
                        if(entries[i] < min){
                            min = entries[i];
                        }

                        if(entries[i] > max){
                            max = entries[i];
                        }

                        sum += entries[i]

                        counts[entries[i]] = (counts[entries[i]] + 1) || 1;
                    }

                    let avg = parseFloat(sum / entries.length).toFixed(4)

                    for (var key in counts) {
                        if (counts.hasOwnProperty(key)) {
                            if(counts[key] > modecount){
                                modecount = counts[key]
                                mode = key;
                            }
                        
                        }
                    }

                    entryObject.min = min
                    entryObject.max = max
                    entryObject.avg = avg
                    entryObject.mode = mode

                    return entryObject
                }
                else{
                    entryObject.min = "NaN"
                    entryObject.max = "NaN"
                    entryObject.avg = "NaN"
                    entryObject.mode = "NaN"

                    return entryObject
                }
            },
            async getStatistics(entries, outliers, phEntries, tempEntries, o2Entries){

                // General stats
                this.entries[0].entries = entries.length
                this.entries[0].outlierCount = outliers.length

                // pH stats
                this.getEntryStats(phEntries, this.statistics[0])

                // Temp stats
                this.getEntryStats(tempEntries, this.statistics[1])

                // o2 stats
                this.getEntryStats(o2Entries, this.statistics[2])
                    
                var yesterday = new Date(this.date)
                var yestDate = new Date(yesterday.setDate(yesterday.getDate() -1))
                const yestConnstring = "http://localhost:8080/api/bydate/?day="+yestDate.getDate()+"&month="+(yestDate.getMonth() + 1)+"&year="+yestDate.getFullYear()+""

                await axios.get(yestConnstring)
                        .then(response => {
                            console.log(response)

                            var yestEntries = 0;
                            var yestOutliers = 0;

                            var phsum = 0
                            var phcount = 0

                            var tempsum = 0
                            var tempcount = 0

                            var o2sum = 0
                            var o2count = 0

                            if (response.data != "") {

                                response.data.Object.forEach(element => {
                                    yestEntries ++

                                    if ('pH' in element.sensor_readings && element.sensor_readings.pH != null) {
                                        phcount ++
                                        phsum += element.sensor_readings.pH 
                                    }

                                    if ('Temperatuur' in element.sensor_readings && element.sensor_readings.Temperatuur != null) {
                                        tempcount ++
                                        tempsum += element.sensor_readings.Temperatuur 
                                    }

                                    if ('O2' in element.sensor_readings && element.sensor_readings.O2 != null) {
                                        o2count ++
                                        o2sum += element.sensor_readings.O2 
                                    }

                                    if ('pH' in element.sensor_readings && element.sensor_readings.pH < this.sensorVariableParameters.pHmin || 'pH' in element.sensor_readings &&  element.sensor_readings.pH > this.sensorVariableParameters.pHmax) {
                                        yestOutliers++
                                    }
                                    else if ('Temperatuur' in element.sensor_readings && element.sensor_readings.Temperatuur < this.sensorVariableParameters.tempmin || 'Temperatuur' in element.sensor_readings && element.sensor_readings.Temperatuur > this.sensorVariableParameters.tempmax) {
                                        yestOutliers++
                                    }
                                    else if ('O2' in element.sensor_readings && element.sensor_readings.O2 < this.sensorVariableParameters.o2min || 'O2' in element.sensor_readings && element.sensor_readings.O2 > this.sensorVariableParameters.o2max) {
                                        yestOutliers++
                                    }

                                    this.entries[0].yestEntries = yestEntries
                                    this.entries[0].yestOutliers = yestOutliers
                                    this.statistics[0].avgYesterday = parseFloat(phsum / phcount).toFixed(4)
                                    this.statistics[1].avgYesterday = parseFloat(tempsum / tempcount).toFixed(4)
                                    this.statistics[2].avgYesterday = parseFloat(o2sum / o2count).toFixed(4)

                                });
                            }
                            else{
                                this.entries[0].yestEntries = "NaN"
                                this.entries[0].yestOutliers = "NaN"
                                this.statistics[0].avgYesterday = "NaN"
                                this.statistics[1].avgYesterday = "NaN"
                                this.statistics[2].avgYesterday = "NaN"
                            }

                        })

            },
            getWeatherData(entries){
                console.log("Getting weather data", entries)

                var entrycount = 0

                var temp = 0
                var mintemp = 0
                var maxtemp = 0
                var humidity = 0
                var sunChance = 0
                var sunUp = 0
                var sunDown = 0

                if(entries != []){

                    entries.forEach(element => {
                        if ('weather_data' in element && element.weather_data != null) {
                            entrycount++
                            
                            if ('ActualTemp' in element.weather_data && element.weather_data.ActualTemp != null && !isNaN(element.weather_data.ActualTemp)) {
                                temp += Number(element.weather_data.ActualTemp)
                            }
                            if ('MinTemp' in element.weather_data && element.weather_data.MinTemp != null && !isNaN(element.weather_data.MinTemp)) {
                                mintemp = Number(element.weather_data.MinTemp)
                            }
                            if ('MaxTemp' in element.weather_data && element.weather_data.MaxTemp != null && !isNaN(element.weather_data.MaxTemp)) {
                                maxtemp = Number(element.weather_data.MaxTemp)
                            }
                            if ('Luchtv' in element.weather_data && element.weather_data.Luchtv != null && !isNaN(element.weather_data.Luchtv)) {
                                humidity += Number(element.weather_data.Luchtv)
                            }
                            if ('Zonkans' in element.weather_data && element.weather_data.Zonkans != null && !isNaN(element.weather_data.Zonkans)) {
                                sunChance += Number(element.weather_data.Zonkans)
                            }
                            if ('ZonOp' in element.weather_data && element.weather_data.ZonOp != null) {
                                sunUp = element.weather_data.ZonOp
                            }
                            if ('ZonOnder' in element.weather_data && element.weather_data.ZonOnder != null) {
                                sunDown = element.weather_data.ZonOnder
                            }
                            
                        }
                    })

                    this.weatherData[0].temp = parseFloat(temp / entrycount).toFixed(2)
                    this.weatherData[0].minTemp = parseFloat(mintemp).toFixed(2)
                    this.weatherData[0].maxTemp = parseFloat(maxtemp).toFixed(2)
                    this.weatherData[0].humidity = parseFloat(humidity / entrycount).toFixed(2)
                    this.weatherData[0].sunChance = parseFloat(sunChance / entrycount).toFixed(2)
                    this.weatherData[0].sunUp = sunUp
                    this.weatherData[0].sunDown = sunDown

                }
                else{
                    this.weatherData[0].temp = "NaN"
                    this.weatherData[0].minTemp = "NaN"
                    this.weatherData[0].maxTemp = "NaN"
                    this.weatherData[0].humidity = "NaN"
                    this.weatherData[0].sunChance = "NaN"
                    this.weatherData[0].sunUp = "NaN"
                    this.weatherData[0].sunDown = "NaN"
                }

                
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
                    const day = this.date.getDate()
                    const month = this.date.getMonth() + 1
                    const year = this.date.getFullYear()

                    console.log(day + " " + month  + " " + year)

                    await axios.get("http://localhost:8080/api/bydate/?day="+day+"&month="+month+"&year="+year+"")
                        .then(response => {
                            console.log(response)

                            if (response.data != "") {

                                response.data.Object.forEach(element => {
                                    entries.push(element)

                                    var outlierJson = {
                                        pH: 0,
                                        temp: 0,
                                        o2: 0,
                                        time: this.formatDate(element.received_at.$date),
                                        outlier: false
                                    }


                                    dateEntries.push(this.formatDate(element.received_at.$date))
                                    if ('pH' in element.sensor_readings && element.sensor_readings.pH != null) {
                                        phEntries.push(element.sensor_readings.pH)
                                        if (element.sensor_readings.pH < sensorVariableParameters.pHmin || element.sensor_readings.pH > sensorVariableParameters.pHmax) {
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
                                        if (element.sensor_readings.Temperatuur < sensorVariableParameters.tempmin || element.sensor_readings.Temperatuur > sensorVariableParameters.tempmax) {
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
                                        if (element.sensor_readings.O2 < sensorVariableParameters.o2min || element.sensor_readings.O2 > sensorVariableParameters.o2max) {
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
                            }

                        })

                    // Create data sets
                    await this.getStatistics(entries, outlierTableItems, phEntries, tempEntries, o2Entries)
                    await this.getWeatherData(entries)


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
            }
            
        },
        computed: {
            pageCount () {
                return Math.ceil(this.outlierTableItems.length / this.itemsPerPage)
            },
        },
    }

</script>