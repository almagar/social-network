import axios from "../axiosInstance";

async function getPostsFromFollowing() {
    const params = {
        page: 0,
        pageSize: 20,
    }
    return axios.get("/post/following", { params })
        .then(res => {
            return [{
                ...res.data,
                status: res.status,
                statusText: res.statusText
            }, null]
        })
        .catch(err => {
            return [null, err];
        });
}

export default getPostsFromFollowing;