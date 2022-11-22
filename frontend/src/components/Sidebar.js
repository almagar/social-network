import { VStack } from "@chakra-ui/react";
import { useEffect, useState } from "react";

import axios from "../axiosInstance";
import CreateRoom from "./CreateRoom";
import RoomList from "./RoomList";

function Sidebar() {

    const [rooms, setRooms] = useState([]);
    const [err, setErr] = useState(null);
    const [loading, setLoading] = useState(true);

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

    useEffect(() => {
        getJoinedRooms();
    }, [])

    return (
        <VStack
            spacing={4}
            width="80%"
            alignItems="end"
        >
            <CreateRoom getJoinedRooms={getJoinedRooms} />
            <RoomList rooms={rooms} err={err} loading={loading} />
        </VStack>
    )
}

export default Sidebar;