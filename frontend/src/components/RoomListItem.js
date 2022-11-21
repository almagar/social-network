import { LinkBox } from "@chakra-ui/react";
import { Link } from "react-router-dom";

function RoomListItem({ room }) {
    return (
        <LinkBox as={Link} to={`/chat/${room.id}`}>{room.name}</LinkBox>
    )
}

export default RoomListItem;
