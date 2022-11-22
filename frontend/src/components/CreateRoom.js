import { Input, InputGroup, InputRightElement, useToast } from "@chakra-ui/react";
import { useState } from "react";

import axios from "../axiosInstance";


function CreateRoom({ getJoinedRooms }) {
    const [inputName, setInputName] = useState("");
    const toast = useToast();

    const handleCreateRoom = e => {
        e.preventDefault();

        const data = { name: inputName };
        axios.post("/chatroom", data, { headers: { "Content-Type": "application/json" }})
            .then(res => {
                console.log("created room")
                console.log(res)
                toast({
                    description: `Created room ${inputName}`,
                    status: 'success',
                    duration: 5000,
                    isClosable: true,
                })
                getJoinedRooms();
            })
            .catch(err => {
                console.log("didn't create room")
                console.log(err)
                toast({
                    description: `Couldn't create room ${inputName}`,
                    status: 'error',
                    duration: 5000,
                    isClosable: true,
                })
            })
            .finally(() => setInputName(""))
        
        setInputName("");
    }

    return (
        <form onSubmit={e => handleCreateRoom(e)}>
            <InputGroup size="xs">
                <Input type="text" placeholder="Name" value={inputName} onChange={e => setInputName(e.currentTarget.value)} />
                <InputRightElement width="auto">
                    <Input type="submit" value="+" size="xs" />
                </InputRightElement>
            </InputGroup>
        </form>
    )
}

export default CreateRoom;