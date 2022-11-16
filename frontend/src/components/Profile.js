import { useEffect } from "react";
import { useLoaderData } from "react-router-dom";

function Profile() {
    const user = useLoaderData();

    useEffect(() => {
        console.log(user);
    }, []);

    return (
        <div>
            <h3>{user.data.username}</h3>
            {user.data.description != null ? <p>{user.data.description}</p> : null}
        </div>
    )
}

export default Profile;
