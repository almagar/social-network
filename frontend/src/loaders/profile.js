import axios from "../axiosInstance";

async function getProfileData({ params }) {
    // get profile by username specified in the url params if it exists
    if (params.username != null && params.username != undefined && params.username !== "") {
        return axios.get(`/user/${params.username}`).then(res => {
            return {
                ...res.data,
                status: res.status,
                statusText: res.statusText
            }
        });
    }

    // get logged in user
    return axios.get("/user/profile").then(res => {
        return {
            ...res.data,
            status: res.status,
            statusText: res.statusText
        }
    });
}

export default getProfileData;