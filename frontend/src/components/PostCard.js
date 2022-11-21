import { Avatar, Box, Text, Card, CardHeader, CardFooter, CardBody, Flex, Heading, Button, IconButton, Image } from "@chakra-ui/react";
import { AddIcon, ChatIcon, RepeatIcon, EditIcon } from "@chakra-ui/icons";
import { useEffect, useState } from "react";
import axios from "../axiosInstance";

function PostCard({ post }) {
    const [image, setImage] = useState(null);

    const getPostImage = async uri => {
        axios.get(uri)
            .then(res => {
                console.log("image res")
                console.log(res)
                console.log("image res.data")
                console.log(res.data)
                if (res.data != "") {
                    setImage(res.data);
                }
            })
            .catch(err => {
                console.log(err);
            })
    }

    useEffect(() => {
        console.log(post.imageUri);
        getPostImage(post.imageUri);
    }, [post])

    return (
        <Card maxW='md'>
            <CardHeader>
                <Flex spacing='4'>
                    <Flex flex='1' gap='4' alignItems='center' flexWrap='wrap'>
                        <Avatar name={`${post.creator.username}`}/>

                        <Box>
                            <Heading size='sm'>{post.creator.username}</Heading>
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
                image &&
                <Image
                    objectFit="cover"
                    src={`data:image/png;base64,${image}`} //Buffer.from(str, 'base64') andbuf.toString('base64')
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
