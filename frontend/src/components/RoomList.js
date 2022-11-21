import { VStack, Text } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import axios from "../axiosInstance";
import RoomListItem from "./RoomListItem";

function RoomList() {
    const [rooms, setRooms] = useState([]);
    const [err, setErr] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getJoinedRooms()
    }, [])

    const getJoinedRooms = async () => {
        axios.get("/chatroom")
            .then(res => {
                console.log("rooms:")
                console.log(res.data)
                setRooms(res.data.data);
            })
            .catch(err => {
                console.log(err);
                setErr(err);
            })
            .finally(() => {
                setLoading(false);
            })
    }

    if (loading)
        return <Text>Loading...</Text>

    if (err != null)
        return <Text>Error: {JSON.stringify(err)}</Text>

    return (
        <VStack spacing={2}>
            {rooms.length === 0 ?
                <Text>Not in any rooms. Join one!</Text>
                : <Text>Your rooms:</Text>}
            {rooms.map(room => <RoomListItem key={room.id} room={room} />)}
        </VStack>
    )
}

export default RoomList;
