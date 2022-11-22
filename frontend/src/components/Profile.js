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
    Input,
    Textarea
} from '@chakra-ui/react'
import { EditIcon } from '@chakra-ui/icons'
import { useEffect, useState, onClick, onSubmit, createRef } from "react";
import { useLoaderData } from "react-router-dom";

import axios from "../axiosInstance"

function Profile() {
    const user = useLoaderData();
    const [avatar, setAvatar] = useState(null);
    const [descriptionBody, setDescriptionBody] = useState("");
    const avatarRef = createRef();

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

    const handleEditProfile = e => {
        e.preventDefault();
        console.log("updating profile");
        let formData = new FormData();
        if (descriptionBody !== "")
            formData.append("description", descriptionBody);
        if (avatarRef.current.files[0] != null)
            formData.append("avatar", avatarRef.current.files[0])
        setDescriptionBody()

        axios.put("/user/profile", formData, { headers: {
                "Accept": "application/json",
                "Content-Type": "multipart/form-data",
            }}).then(res => {
        }).catch(err => {
        })
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
                        <Text ml="10px" fontWeight="normal" fontSize="smaller">{user.data.firstname} {user.data.lastname}</Text>
                        <Text ml="10px" fontWeight="normal">{user.data.description}</Text>
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
                <CardBody>
                    <form onSubmit={e => handleEditProfile(e)}>
                        <Textarea
                            placeholder="New description"
                            value={descriptionBody}
                            onChange={e => setDescriptionBody(e.currentTarget.value)}
                            resize="vertical"
                            mb={3}
                        />
                        <Text fontWeight="normal" fontSize="smaller">Change avatar</Text>
                        <Input
                            type="file"
                            ref={avatarRef}
                            multiple={false}
                            accept="image/jpeg,image/png"
                            size="xs"
                        />
                        <Input
                            type="submit"
                            value="Save"
                            bg="green.300"
                            color="white"
                            cursor="pointer"
                            border="none"
                            width="auto"
                            alignSelf="end"
                            size="xs"
                        />
                    </form>
                </CardBody>

            </Card>
        </VStack>
    )
}

export default Profile;
