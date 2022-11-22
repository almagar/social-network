import { useEffect, useState } from "react";
import { Input, VStack, Text } from '@chakra-ui/react';
import MinimalProfile from "./MinimalProfile";

import axios from "../axiosInstance"

function Search() {
    const [searchQuery, setSearchQuery] = useState("");
    const [searchRes, setSearchRes] = useState(null);

    useEffect(() => {

    }, [])

    const getSearchResults = () => {
        axios.get(`/user`, { params: { username: searchQuery }})
            .then(res => {
                if (res.data !== "") {
                    setSearchRes(res.data.data);
                }
            })
    }

    return (
        <VStack spacing={8}>
            <Input
                placeholder='Search'
                value={searchQuery}
                width="90%"
                onChange={e => {
                    setSearchQuery(e.currentTarget.value);
                    getSearchResults();
                }}
            />
            {
                searchRes != null ?
                    searchRes.length === 0 ?
                        <Text>No result</Text>
                        : searchRes.map(user => <MinimalProfile key={user.id} user={user}/>)
                    : null
            }

        </VStack>
    )
}

export default Search;