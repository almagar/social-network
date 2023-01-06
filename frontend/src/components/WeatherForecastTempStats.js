import { Box, Skeleton } from "@chakra-ui/react";
import { useEffect, useRef, useState } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Line } from "react-chartjs-2";

import axios from "../axiosInstance";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

function WeatherForecastTempStats({ temperatureChartRef, location }) {
    const [data, setData] = useState(null);
    const [err, setErr] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setData(null);
        setErr(null);
        setLoading(true);
        axios.get(`${process.env.REACT_APP_VERTX_URL}/weather/temps`, { params: { location } })
            .then(res => {
                // convert data to correct format
                let temp = {
                    labels: [],
                    datasets: [{
                        type: "line",
                        label: "Temperature",
                        data: [],
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    }, {
                        type: "line",
                        label: "Min temperature",
                        data: [],
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        showLine: false,
                    }, {
                        type: "line",
                        label: "Max temperature",
                        data: [],
                        backgroundColor: 'rgba(255, 206, 86, 0.2)',
                        showLine: false,
                    }
                ],
                };
                Object.entries(res.data).sort((a, b) => a[0] > b[0]).forEach(([k, v]) => {
                    const d = new Date(parseInt(k, 10) * 1000);
                    temp.labels.push(`${d.getDay()} ${d.getHours()}:${d.getMinutes()}`);
                    // datasets[0] for the line chart, showing actual temp
                    temp.datasets[0].data.push(v.temp);
                    // datasets[1] for the line chart without lines, showing min temp
                    temp.datasets[1].data.push(v.tempMin);
                    // datasets[2] for the line chart without lines, showing max temp
                    temp.datasets[2].data.push(v.tempMax);
                });
                setData(temp);
            })
            .catch(err => {
                console.log(err);
                setErr(err);
            })
            .finally(() => setLoading(false));
    }, [location]);

    if (err !== null)
        return (
            <p>Can't get weather forecast data</p>
        )
    
    if (loading)
        return <p>Loading...</p>

    return (
        <Skeleton isLoaded={!loading}>
            <Box position="relative" maxWidth="300px" width="300px" height="300px" overflowX="scroll">
                <Box width="600px">
                    <Line
                        style={{ position: "absolute", top: 0, left: 0 }}
                        ref={temperatureChartRef}
                        data={data}
                        options={{
                            scales: {
                                y: {
                                    ticks: {
                                        stepSize: 0.01
                                    }
                                }
                            }
                        }}
                    />
                </Box>
            </Box>
        </Skeleton>
    );
}

export default WeatherForecastTempStats;