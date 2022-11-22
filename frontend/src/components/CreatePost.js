import { Box, Input, Textarea, useToast } from "@chakra-ui/react";
import { createRef, useState } from "react";

import axios from "../axiosInstance";

function CreatePost() {
    const [postBody, setPostBody] = useState("");
    const postImageRef = createRef();
    const toast = useToast();

    const handleCreatePost = e => {
        e.preventDefault();

        console.log("creating post")
        let formData = new FormData();
        if (postBody !== "")
            formData.append("body", postBody);
        if (postImageRef != null && postImageRef.current != null &&
            postImageRef.current.files != null && postImageRef.current.files[0] != null)
            formData.append("image", postImageRef.current.files[0])

        axios.post("/post", formData, { headers: {
            "Accept": "application/json",
            "Content-Type": "multipart/form-data",
        }}).then(res => {
            toast({
                description: `Published post!`,
                status: 'success',
                duration: 5000,
                isClosable: true,
            })
        }).catch(err => {
            toast({
                description: `Couldn't publish post`,
                status: 'error',
                duration: 5000,
                isClosable: true,
            })
        })
        .finally(() => setPostBody(""))
        
    }

    return (
        <form onSubmit={e => handleCreatePost(e)} style={{ width: "60%" }}>
            <Box display="flex" flexDirection="column">
                <Textarea
                    placeholder="Share something with your friends!"
                    value={postBody}
                    onChange={e => setPostBody(e.currentTarget.value)}
                    resize="vertical"
                    mb={2}
                />
                <Input
                    type="file"
                    ref={postImageRef}
                    multiple={false}
                    accept="image/jpeg,image/png"
                    size="xs"
                    mb={2}
                />
                <Input
                    type="submit"
                    value="Publish post"
                    bg="green.300"
                    color="white"
                    cursor="pointer"
                    border="none"
                    width="auto"
                    alignSelf="end"
                    size="xs"
                />
            </Box>
        </form>
    )
}

export default CreatePost;
