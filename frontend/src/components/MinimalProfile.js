import { Card, CardHeader, CardBody, CardFooter, Text, Avatar, AvatarBadge, Button, LinkOverlay, LinkBox } from '@chakra-ui/react'
import { useEffect, useState, onClick } from "react";
import { Link } from 'react-router-dom';

import axios from "../axiosInstance"
import AvatarFromData from './AvatarFromData';

function MinimalProfile({ user }) {
    const [avatar, setAvatar] = useState(null);
    const [isFollowing, setIsFollowing] = useState(null);

    const getAvatar = async uri => {
        axios.get(uri, { responseType: "arraybuffer" })
            .then(res => {
                if (res.data != "") {
                    setAvatar(res);
                }
            })
            .catch(err => {
                console.log(err);
            })
    }

    const getIsFollowing = async user => {
        axios.get(`/user/${user.id}/is-following`)
            .then(res => {
                if (res.data != "") {
                    setIsFollowing(res.data.data);
                }
                console.log(res.data.data);
            })
            .catch(err => {
                console.log(err);
            })
    }

    const followUser = async userId => {
        axios.put(`/user/${userId}/follow`)
            .then(res => {
                setIsFollowing(true);
            });
    }

    const unFollowUser = async userId => {
        axios.put(`/user/${user.id}/unfollow`)
            .then(res => {
                setIsFollowing(false);
            });
    }

    useEffect(() => {
        getAvatar(user.imageUri);
        getIsFollowing(user);
    }, [user])

    return (
        <Card width="90%">
            <CardBody display="flex" alignItems="center">
                <AvatarFromData name={user.username} avatar={avatar} />
                <LinkBox as={Link} to={`/profile/${user.username}`} ml="10px">{user.username}</LinkBox>
                {
                    isFollowing != null ?
                        isFollowing ?
                            <Button bg="red.300" ml="auto" onClick={() => unFollowUser(user.id)}>Unfollow</Button>
                            : <Button bg="green.300" ml="auto" onClick={() => followUser(user.id)}>Follow</Button>
                        : null
                }
            </CardBody>
        </Card>
    )
}

export default MinimalProfile;