<template>
  <div>
    <h1 class="chart-title">Actuele Data</h1>
    <div class="chart-container">
      <div>
        <canvas ref="temperatureChart"></canvas>
        <div class="chart-value">
          <p>Temperatuur: {{ temperatureValue }} °C</p>
        </div>
      </div>
      <div>
        <canvas ref="acidityChart"></canvas>
        <div class="chart-value">
          <p>Zuurtegraad: {{ acidityValue }}</p>
        </div>
      </div>
      <div>
        <canvas ref="oxygenChart"></canvas>
        <div class="chart-value">
          <p>Zuurstof: {{ oxygenValue }} %</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue';
import Chart from 'chart.js/auto';

export default {
  setup() {
    const temperatureChart = ref(null);
    const acidityChart = ref(null);
    const oxygenChart = ref(null);
    
    const temperatureValue = ref(null);
    const acidityValue = ref(null);
    const oxygenValue = ref(null);

    onMounted(() => {
      createChart(temperatureChart, [18, 30], temperatureValue, 'Actuele Temperatuur', '°C');
      createChart(acidityChart, [7, 7], acidityValue, 'Actuele Zuurtegraad');
      createChart(oxygenChart, [80, 20], oxygenValue, 'Actuele Zuurstof');
    });

    function createChart(chartRef, dataValues, valueRef, label, unit = '') {
      const ctx = chartRef.value.getContext('2d');
      chartRef.value.width = 500; 
      chartRef.value.height = 300;

      let backgroundColors, borderColors;

      if (label === 'Actuele Temperatuur') {
        // Specifieke kleuren voor zuurtegraad
        backgroundColors = ['rgba(0, 51, 204, 0.5)', 'rgba(0, 153, 255, 0.5)'];
        borderColors = ['rgba(0, 51, 204, 0.8)', 'rgba(0, 153, 255, 0.1)'];
      } else if (label === 'Actuele Zuurtegraad') {
        // Specifieke kleuren voor zuurstof
        backgroundColors = ['rgba(51, 153, 102, 0.5)', 'rgba(102, 153, 153, 0.5)'];
        borderColors = ['rgba(51, 153, 102, 0.8)', 'rgba(102, 153, 153, 0.1)'];
      } else {
        // Standaard kleuren voor andere grafieken
        backgroundColors = ['rgba(122, 173, 255, 0.5)', 'rgba(214, 230, 255, 0.5)'];
        borderColors = ['rgba(122, 173, 255, 0.8)', 'rgba(214, 230, 255, 0.1)'];
      }

      const data = {
        labels: [label],
        datasets: [{
          label: unit,
          data: dataValues,
          backgroundColor: backgroundColors,
          borderColor: borderColors,
          borderWidth: 1
        }]
      };

      valueRef.value = data.datasets[0].data[0];

      const config = {
        type: 'doughnut',
        data,
        options: {
          circumference: 180,
          rotation: 270,
          cutout: '60%',
          plugins: {
            legend: {
              display: false
            },
            tooltip: {
              filter: (tooltipItem) => {
                console.log(tooltipItem)
                return tooltipItem.dataIndex === 0;
              }
            }
          }
        }
      };

      new Chart(ctx, config);
    }

    return {
      temperatureChart,
      acidityChart,
      oxygenChart,
      temperatureValue,
      acidityValue,
      oxygenValue,
    };
  },
};
</script>

<style>
.chart-container {
  display: flex;
  justify-content: space-around;
}

.chart-title {
  text-align: center;
  margin-top: 20px;
  font-weight: bold;
}

.chart-value {
  text-align: center;
  margin-top: 0px;
  font-weight: bold;
}
</style>
