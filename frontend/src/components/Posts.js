import { Divider, Text, VStack } from "@chakra-ui/react";
import { useEffect } from "react";
import { useLoaderData } from "react-router-dom";
import CreatePost from "./CreatePost";
import PostCard from "./PostCard";

function Posts() {
    const [posts, err] = useLoaderData();

    useEffect(() => {
        console.log("posts")
        console.log(posts);
    }, [posts])

    if (err != null)
        return <p>something went wrong: {JSON.stringify(err)}</p>

    return (
        <VStack spacing={8}>
            <CreatePost />
            <Divider />
            {posts.data.length === 0 ?
                <Text>No posts, try following someone!</Text>
                : posts.data.map(post => <PostCard key={post.id} post={post} />)}
        </VStack>
    )
}

export default Posts;
