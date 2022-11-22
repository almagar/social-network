import { Avatar, Box, Text, Card, CardHeader, CardFooter, CardBody, Flex, LinkBox, Button, IconButton, Image } from "@chakra-ui/react";
import { AddIcon, ChatIcon, RepeatIcon, EditIcon } from "@chakra-ui/icons";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { Buffer } from "buffer";

import axios from "../axiosInstance";
import ImageFromData from "./ImageFromData";
import AvatarFromData from "./AvatarFromData";

function PostCard({ post }) {
    const [image, setImage] = useState(null);

    const getPostImage = async uri => {
        console.log("getting image: " + uri)
        axios.get(uri, { responseType: "arraybuffer" })
            .then(res => {
                console.log("got image")
                if (res.data != null) {
                    console.log(res)
                    setImage(res);
                }
            })
            .catch(err => {
                console.log("didn't get post")
                console.log(err);
            })
    }

    useEffect(() => {
        getPostImage(post.imageUri);
    }, [post])

    return (
        <Card maxW='md'>
            <CardHeader>
                <Flex spacing='4'>
                    <Flex flex='1' gap='4' alignItems='center' flexWrap='wrap'>
                        <AvatarFromData name={`${post.creator.username}`} avatar={post.creator.avatarUri} />

                        <Box>
                            <LinkBox as={Link} to={`/profile/${post.creator.username}`}>{post.creator.username}</LinkBox>
                        </Box>
                    </Flex>
                    <IconButton
                        variant='ghost'
                        colorScheme='gray'
                        aria-label='See menu'
                        icon={<EditIcon />}
                    />
                </Flex>
            </CardHeader>
            <CardBody>
                <Text fontWeight={400}>{post.body}</Text>
            </CardBody>
            {
                image != null &&
                <ImageFromData
                    objectFit="cover"
                    image={image}
                    alt="post image"
                    fallbackSrc="https://via.placeholder.com/150"
                />
            }

            <CardFooter
                justify='space-between'
                flexWrap='wrap'
                sx={{
                    '& > button': {
                        minW: '136px',
                    },
                }}
            >
                <Button flex='1' variant='ghost' leftIcon={<AddIcon />}>
                    Like
                </Button>
                <Button flex='1' variant='ghost' leftIcon={<ChatIcon />}>
                    Comment
                </Button>
                <Button flex='1' variant='ghost' leftIcon={<RepeatIcon />}>
                    Share
                </Button>
            </CardFooter>
        </Card>
    )
}

export default PostCard;
