import { Grid, GridItem, LinkBox, VStack, Text, Stack } from '@chakra-ui/react'
import { NavLink, Outlet, useLoaderData } from "react-router-dom";
import Sidebar from './Sidebar';
import WeatherForecastStats from './WeatherForecastStats';

function Layout(props) {
    const data = useLoaderData();

    return (
        <Grid
            templateAreas={`"nav main side"`}
            gridTemplateColumns={'1fr 3fr 1fr'}
            minH='100vh'
            gap='1'
            color='blackAlpha.700'
            fontWeight='bold'
        >
            <GridItem px='2' area={'nav'} py="50px">
                <VStack spacing={20}>
                    <VStack spacing={4} width="80%" alignItems="start">
                        <LinkBox justifySelf="left" as={NavLink} to="/">Home</LinkBox>
                        <LinkBox justifySelf="left" as={NavLink} to="/profile">Profile</LinkBox>
                        <LinkBox justifySelf="left" as={NavLink} to="/search">Search</LinkBox>
                    </VStack>
                    <Stack width="80%" alignItems="start">
                        <WeatherForecastStats />
                    </Stack>
                </VStack>
            </GridItem>
            <GridItem
                px='2'
                area={'main'}
                pt="50px"
                pb="10px"
                borderLeft="1px solid lightgray"
                borderRight="1px solid lightgray"
            >
                <Outlet context={{ user: data.data }} />
            </GridItem>
            <GridItem
                px='5'
                area={'side'}
                py="50px"
                justifyContent="right"
                textAlign="right"
            >
                <Sidebar />
            </GridItem>
        </Grid>
    )
}

export default Layout;
