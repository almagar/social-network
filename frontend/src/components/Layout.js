import { Grid, GridItem } from '@chakra-ui/react'
import { Link, Outlet } from "react-router-dom";

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
            <GridItem pl='2' bg='orange.300' area={'nav'}>
                Nav
            </GridItem>
            <GridItem pl='2' bg='green.300' area={'main'}>
                <Link to="/hej">Hej</Link>
                <Outlet />
            </GridItem>
            <GridItem pl='2' bg='blue.300' area={'side'}>
                Side
            </GridItem>
        </Grid>
    )
}

export default Layout;
