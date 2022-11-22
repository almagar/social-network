import { Image } from "@chakra-ui/react";
import { Buffer } from "buffer";
import { useEffect } from "react";

function ImageFromData(props) {
    const { image } = props;

    useEffect(() => {
        console.log("rendering image")
        console.log(image)
    }, [])

    if (image == null)
        return null;

    return (
        <Image
            {...props}
            src={`data:${image.headers["content-type"]};base64,${Buffer.from(image.data, "binary").toString("base64")}`}
        />
    )
}

export default ImageFromData;
