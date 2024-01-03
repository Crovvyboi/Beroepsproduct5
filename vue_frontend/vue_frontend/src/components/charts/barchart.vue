<template>
    <Bar 
        id="mychart"
        :options="chartOptions"
        :data="chartData"
       >
       Bar chart could not be loaded.
    </Bar>
</template>
  
<script>

    import {Bar} from 'vue-chartjs'
    import {Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale} from 'chart.js'
  
    ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)
  
    export default{
        name: 'BarChart',
        components: {Bar},
        data(){
            return{
                chartData:{
                    labels: ['January', 'February', 'March'],
                    datasets: [{data: [40, 20, 12]}]
                },
                chartOptions:{
                    responsive: true
                }
            }
        }
    }
  
  </script>