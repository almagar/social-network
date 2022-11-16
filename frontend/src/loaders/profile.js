import axios from "../axiosInstance";

async function getProfileData() {
    return axios.get("/user/profile").then(res => {
        return {
            ...res.data,
            status: res.status,
            statusText: res.statusText
        }
    });
}

export default getProfileData;