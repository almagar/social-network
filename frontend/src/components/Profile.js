import {
    Card,
    CardHeader,
    CardBody,
    CardFooter,
    Text,
    Avatar,
    AvatarBadge,
    Button,
    VStack,
    Stat,
    StatLabel,
    StatNumber,
    IconButton,
    FormControl,
    FormLabel,
    Input
} from '@chakra-ui/react'
import { EditIcon } from '@chakra-ui/icons'
import { useEffect, useState, onClick, onSubmit } from "react";
import { useLoaderData } from "react-router-dom";

import axios from "../axiosInstance"

function Profile() {
    const user = useLoaderData();
    const [avatar, setAvatar] = useState(null);


    const getAvatar = async uri => {
        axios.get(uri)
            .then(res => {
                if (res.data != "") {
                    setAvatar(res.data);
                }
            })
            .catch(err => {
                console.log(err);
            })
    }

    const editProfile = async (avatar, description) => {
        axios.put(`/user/profile`, { params: { avatar, description } })
    }

    useEffect(() => {
        console.log(user);
    }, []);

    return (
        <VStack spacing={8}>
            <Card width="90%">
                <CardBody display="webkit-flex" alignItems="center">
                    <Avatar name={user.data.username} src={`data:image/png;base64,${avatar}`} />
                    <div>
                        <Text ml="10px" fontSize="x-large">{user.data.username}</Text>
                        <Text ml="10px" fontWeight="normal">Desc</Text>
                    </div>
                    <Stat ml="auto" width="100px">
                        <StatLabel>Followers</StatLabel>
                        <StatNumber>{user.data.nrOfFollowers}</StatNumber>
                    </Stat>
                    <Stat ml="auto" width="100px">
                        <StatLabel>Following</StatLabel>
                        <StatNumber>{user.data.nrOfFollowing}</StatNumber>
                    </Stat>
                </CardBody>
                <CardBody display="webkit-flex" alignItems="center">
                    <form onSubmit={() => editDescription()}>
                        <FormLabel>Update description</FormLabel>
                        <Input type='text' />
                    </form>
                </CardBody>

            </Card>
        </VStack>
    )
}

export default Profile;
