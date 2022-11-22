import { VStack, Text } from "@chakra-ui/react";
import RoomListItem from "./RoomListItem";

function RoomList({ rooms, err, loading }) {
    if (loading)
        return <Text>Loading...</Text>

    if (err != null)
        return <Text>Error: {JSON.stringify(err)}</Text>

    return (
        <VStack spacing={2} alignItems="end">
            {rooms.length === 0 ?
                <Text>Not in any rooms. Join one!</Text>
                : <Text>Rooms <br /></Text>}
            {rooms.map(room => <RoomListItem key={room.id} room={room} />)}
        </VStack>
    )
}

export default RoomList;
