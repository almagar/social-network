import axios from "axios";

const axiosInstance = axios.create({
    baseURL: process.env.REACT_APP_BASE_URL,
    headers: {
        "Authorization": `Bearer ${localStorage.getItem("sn-token")}`,
    },
});

export default axiosInstance;
