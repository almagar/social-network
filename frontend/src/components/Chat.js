import { useEffect, useRef, useState } from "react"
import { Box, Image, Grid, GridItem, Input, InputGroup, InputRightElement, Text, useToast, VStack } from "@chakra-ui/react";
import { useOutletContext, useParams } from "react-router-dom";
import { Buffer } from "buffer";

import SockJS from "sockjs-client";
import Stomp from "stompjs";

import axios from "../axiosInstance";


function Chat() {
    const params = useParams();
    const { user } = useOutletContext();

    const ws = new SockJS(`${process.env.REACT_APP_BASE_URL}/chat`);
    const client = Stomp.over(ws);
    client.reconnect_delay = 5000;

    const [room, setRoom] = useState(null);
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState(null);

    const [connected, setConnected] = useState(false);
    const [subscription, setSubscription] = useState(null);
    const [messages, setMessages] = useState([]);
    const [inputMsg, setInputMsg] = useState("");
    const imageRef = useRef();

    const [addUserInput, setAddUserInput] = useState("");
    const addUserToast = useToast();

    const handleSendMessage = async e => {
        e.preventDefault();
        console.log("Sending message: " + inputMsg);
        let msg = {
            fromUser: "",
        }
        if (inputMsg != null && inputMsg != "") {
            msg["msg"] = inputMsg;
        }

        if (imageRef != null && imageRef.current != null && imageRef.current.files != null &&
            imageRef.current.files[0] != null) {
            const ab = await imageRef.current.files[0].arrayBuffer();
            msg["b64Image"] = Buffer.from(ab).toString("base64");
        }


        client.send(`/app/chat/${params.roomId}`, {
            "Authorization": `Bearer ${localStorage.getItem("sn-token")}`,
        }, JSON.stringify(msg));
        setInputMsg("");
    }

    const handleReceivedMessage = msg => {
        let body = JSON.parse(msg.body);
        const filtered = messages.filter(m => {
            console.log("m.id: " + m.id)
            console.log("msg.id: " + body.id)
            return m.id === body.id
        });
        if (filtered.length > 0) return;

        if (body["b64Image"] != null && body["b64Image"] != "") {
            let buf = Buffer.from(body["b64Image"], "base64");
            body["b64Image"] = buf.buffer;
        }

        setMessages(messages => {
            console.log("messages2")
            console.log(messages)
            return [...messages, body]
        });
    }

    const getRoom = async () => {
        axios.get(`/chatroom/${params.roomId}`)
            .then(res => {
                console.log("rooms:")
                console.log(res.data)
                setRoom(res.data.data);
            })
            .catch(err => {
                console.log(err);
                setErr(err);
            })
            .finally(() => {
                setLoading(false);
            })
    }

    const addUserToRoom = async e => {
        e.preventDefault();
        try {
            const res = await axios.get(`/user/${addUserInput}`);
            const data = {
                chatRoomId: room.id,
                userId: res.data.data.id,
            }
            axios.post(`/chatroom/add`, data)
                .then(res => {
                    addUserToast({
                        description: `Added ${addUserInput} to the room!`,
                        status: 'success',
                        duration: 5000,
                        isClosable: true,
                    })
                })
                .catch(err => {
                    addUserToast({
                        description: `Couldn't add ${addUserInput} to the room, are you the owner?`,
                        status: 'errro',
                        duration: 5000,
                        isClosable: true,
                    })
                })
                .finally(() => setAddUserInput(""))
        } catch (e) {
        }
    }

    useEffect(() => {
        if (room != null) return;
        getRoom();
    }, [room])

    useEffect(() => {
        if (connected) return;
        if (room == null) return;

        client.connect({
            "Authorization": `Bearer ${localStorage.getItem("sn-token")}`,
        }, () => {
            setConnected(true);
            if (subscription != null) return;

            const sub = client.subscribe(`/broker/chat/${params.roomId}`, message => {
                if (subscription != null) return;
                handleReceivedMessage(message);
            }, {
                "Authorization": `Bearer ${localStorage.getItem("sn-token")}`,
            })
            setSubscription(sub);
        })
    }, [room, connected, client, params.roomId])

    useEffect(() => {
        if (subscription == null) return;
        return () => {
            client.unsubscribe(subscription);
        }
    }, [])

    if (loading)
        return <Text>Loading...</Text>

    if (err != null)
        return <Text>Error: {JSON.stringify(err)}</Text>

    return (
        <Grid
            templateAreas={`"info"
                            "messages"
                            "input"`}
            gridTemplateRows={'50px 1fr auto'}
            gridTemplateColumns={'1fr'}
            gap='1'
            pb={0}
            minHeight="100%"
            color='blackAlpha.700'
            fontWeight='bold'
        >
            <GridItem p='3' borderRadius={5} bg='gray.100' area={'info'} display="flex" justifyContent="space-between">
                <Text>{room.name}</Text>
                <form width="auto" onSubmit={e => addUserToRoom(e)}>
                    <InputGroup size="sm" width="auto">
                        <Input
                            width="auto"
                            type="text"
                            placeholder="Username"
                            value={addUserInput}
                            onChange={e => setAddUserInput(e.currentTarget.value)}
                        />
                        <InputRightElement width="auto">
                            <Input type="submit" value="Add user" size="sm" />
                        </InputRightElement>
                    </InputGroup>
                </form>
            </GridItem>
            <GridItem px='2' minHeight="100%" maxHeight="100%" height="100%" area={'messages'} overflowY="scroll">
                <VStack>
                    {messages.map(msg => {
                        const isOwnMessage = msg.fromUser.id === user.id;
                        return (
                            <Box key={msg.id} alignSelf={isOwnMessage ? 'end' : 'start'} display="inline-block">
                                <Box py={1} px={3} bg={isOwnMessage ? 'blue.300' : 'gray.200'} borderRadius={3}>
                                    <Text fontWeight={400}>{msg.msg}</Text>
                                </Box>
                                {msg["b64Image"] != null ? <Image src={`data:image/png;base64,${Buffer.from(msg["b64Image"], "binary").toString("base64")}`} />
                                : null}
                                <Text fontWeight={400} fontSize={"0.7em"} textAlign={isOwnMessage ? 'end' : 'start'}>{msg.fromUser.username}</Text>
                            </Box>
                        )
                    })}
                </VStack>
            </GridItem>
            <GridItem px='2' area={'input'} height="auto">
                <form onSubmit={e => handleSendMessage(e)} style={{ display: "block", flexDirection: "column" }}>
                    <Input
                        type="file"
                        ref={imageRef}
                        multiple={false}
                        accept="image/jpeg,image/png"
                        size="xs"
                        mb={2}
                    />
                    <InputGroup size="sm">
                        <Input
                            type="text"
                            placeholder="Message..."
                            value={inputMsg}
                            onChange={e => {
                                setInputMsg(e.currentTarget.value);
                            }}
                        />
                        <InputRightElement width="auto">
                            <Input type="submit" value="Send" size="sm" />
                        </InputRightElement>
                    </InputGroup>
                </form>
            </GridItem>
        </Grid>
    )
}

export default Chat;
