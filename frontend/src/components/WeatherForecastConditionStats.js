import { Skeleton } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Pie } from "react-chartjs-2";

import axios from "../axiosInstance";

ChartJS.register(ArcElement, Tooltip, Legend);

function WeatherForecastConditionStats({ conditionChartRef, location }) {
    const [data, setData] = useState(null);
    const [err, setErr] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setData(null);
        setErr(null);
        setLoading(true);
        axios.get(`${process.env.REACT_APP_VERTX_URL}/weather/conditions`, { params: { location } })
            .then(res => {
                // convert data to correct format
                let temp = {
                    labels: [],
                    datasets: [{
                        label: "%",
                        data: [],
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)',
                        ],
                    }],
                };
                Object.entries(res.data).sort((a, b) => a[0] > b[0]).forEach(([k, v]) => {
                    temp.datasets[0].data.push(v);
                    temp.labels.push(k);
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
            <Pie
                ref={conditionChartRef}
                data={data}
            />
        </Skeleton>
    );
}

export default WeatherForecastConditionStats;