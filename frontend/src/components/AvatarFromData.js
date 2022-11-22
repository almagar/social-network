import { Avatar } from "@chakra-ui/react";
import { Buffer } from "buffer";

function AvatarFromData(props) {
    const { avatar } = props;
    return (
        <Avatar
            {...props}
            src={avatar == null ? "" : `data:${avatar.headers["content-type"]};base64,${Buffer.from(avatar.data, "binary").toString("base64")}`}
        />
    )
}

export default AvatarFromData;
