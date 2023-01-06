import { RepeatIcon } from "@chakra-ui/icons";
import { Heading, Tab, Text, TabList, TabPanel, TabPanels, Tabs, VStack, Button, useToast, Input } from "@chakra-ui/react";
import { useRef, useState } from "react";
import WeatherForecastConditionStats from "./WeatherForecastConditionStats";
import WeatherForecastTempStats from "./WeatherForecastTempStats";

import axios from "axios";
import axiosInstance from "../axiosInstance";

function WeatherForecastStats() {
    const [location, setLocation] = useState("stockholm");
    const [tabIndex, setTabIndex] = useState(0);
    const conditionChartRef = useRef(null);
    const temperatureChartRef = useRef(null);
    const toast = useToast();

    const makePostFromChart = async () => {
        const ref = tabIndex === 0 ? conditionChartRef : temperatureChartRef;
        const b64ImageUri = ref.current.toBase64Image();

        const blobRes = await axios.get(b64ImageUri, { responseType: "blob" });
        console.log(blobRes);

        let formData = new FormData();
        formData.append("body", `Look at this weather data from ${location}`);
        formData.append("image", new File([blobRes.data], "chart.png", { type: "image/png" }));

        axiosInstance.post("/post", formData, { headers: {
            "Accept": "application/json",
            "Content-Type": "multipart/form-data",
        }}).then(res => {
            toast({
                description: "Shared chart!",
                status: 'success',
                duration: 3000,
                isClosable: true,
            })
        }).catch(err => {
            console.log(err);
            toast({
                description: "Couldn't share chart",
                status: 'error',
                duration: 3000,
                isClosable: true,
            })
        });
    }

    return (
        <VStack alignItems="start">
            <Heading as="h5" size="md">Weather forecast stats</Heading>
            <Text fontSize="sm" fontWeight="normal" mb={4}>Data for every 3h for the next 5 days</Text>
            <Input type="text" placeholder="Location..." value={location} onChange={e => setLocation(e.currentTarget.value)} />
            <Tabs onChange={index => setTabIndex(index)}>
                <TabList>
                    <Tab>Conditions</Tab>
                    <Tab>Temperatures</Tab>
                </TabList>
                <TabPanels>
                    <TabPanel>
                        <WeatherForecastConditionStats conditionChartRef={conditionChartRef} location={location} />
                    </TabPanel>
                    <TabPanel>
                        <WeatherForecastTempStats temperatureChartRef={temperatureChartRef} location={location} />
                    </TabPanel>
                </TabPanels>
            </Tabs>
            <Button
                rightIcon={<RepeatIcon />}
                alignSelf="end"
                colorScheme="green"
                onClick={makePostFromChart}
            >
                Share
            </Button>
        </VStack>
    );
}

export default WeatherForecastStats;