import { Grid, GridItem, LinkBox, VStack, Text } from '@chakra-ui/react'
import { NavLink, Outlet } from "react-router-dom";
import RoomList from './RoomList';

function Layout(props) {
    return (
        <Grid
            templateAreas={`"nav main side"`}
            gridTemplateColumns={'1fr 3fr 1fr'}
            minH='100vh'
            gap='1'
            color='blackAlpha.700'
            fontWeight='bold'
        >
            <GridItem pl='2' area={'nav'} py="50px">
                <VStack spacing={4} justifyContent="left">
                    <LinkBox justifySelf="left" as={NavLink} to="/profile">Profile</LinkBox>
                    <LinkBox justifySelf="left" as={NavLink} to="/chat">Chat</LinkBox>
                </VStack>
            </GridItem>
            <GridItem
                pl='2'
                area={'main'}
                py="50px"
                borderLeft="1px solid lightgray"
                borderRight="1px solid lightgray"
            >
                <Outlet />
            </GridItem>
            <GridItem
                pl='2'
                area={'side'}
                py="50px"
                justifyContent="right"
                textAlign="right"
            >
                <VStack
                    spacing={4}
                    justifyContent="right"
                    textAlign="right"
                >
                    <Text>Side</Text>
                    <RoomList />
                </VStack>
            </GridItem>
        </Grid>
    )
}

export default Layout;
