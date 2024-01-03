<template>
    <div>
        <Line 
            id="phLine" 
            v-if="loaded" :data="pHdata" 
            :options="chartOptions" 
            style="width: 1000px; height: 500px;"
            >
            Line chart could not be loaded.
        </Line>
    </div>
    <div>
        <Line 
            id="tempLine" 
            v-if="loaded" :data="tempData" 
            :options="chartOptions" 
            style="width: 1000px; height: 500px;"
            >
            Line chart could not be loaded.
        </Line>
    </div>
    <div>
        <Line 
            id="o2Line" 
            v-if="loaded" :data="o2Data" 
            :options="chartOptions" 
            style="width: 1000px; height: 500px;"
            >
            Line chart could not be loaded.
        </Line>
    </div>
</template>
  
<script>
    import axios from 'axios'
    import {Chart, Line} from 'vue-chartjs'
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
        name: 'LineChart',
        components: {
            Line
        },
        data(){
            return{
                loaded: false,
                pHdata: null,
                tempData: null,
                o2Data: null
            }
        },
        async mounted(){
            console.log("Async started")
            this.loaded = false

            const phEntries = []
            const tempEntries = []
            const o2Entries = []
            const dateEntries = []

            try{
                await axios.get("http://localhost:8080/api/bydate/?day=3&month=11&year=2023")
                    .then(response => {
                        console.log(response)

                        response.data.Object.forEach(element => {
                            dateEntries.push(element.received_at.$date)
                            if ('pH' in element.sensor_readings && element.sensor_readings.pH != null) {
                                phEntries.push(element.sensor_readings.pH)
                            }
                            else{
                                phEntries.push("NaN")
                            }

                            if ('Temperatuur' in element.sensor_readings && element.sensor_readings.Temperatuur != null) {
                                tempEntries.push(element.sensor_readings.Temperatuur)
                            }
                            else{
                                tempEntries.push("NaN")
                            }

                            if ('O2' in element.sensor_readings && element.sensor_readings.O2 != null) {
                                o2Entries.push(element.sensor_readings.O2)
                            }
                            else{
                                o2Entries.push("NaN")
                            }

                        });

                    })

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

                this.pHdata = pHdata
                this.tempData = tempData
                this.o2Data = o2Data

                this.loaded = true

                console.log("Async ended")
                
            } catch(e){
                console.error(e)
            }
        }
    }

</script>