import { ChakraProvider } from "@chakra-ui/react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Chat from "./components/Chat";
import Layout from "./components/Layout";
import Posts from "./components/Posts";
import Profile from "./components/Profile";
import Search from "./components/Search";
import Whiteboard from "./components/Whiteboard";
import getPostsFromFollowing from "./loaders/posts";
import getProfileData from "./loaders/profile";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    loader: getProfileData,
    children: [
      {
        path: "",
        element: <Posts />,
        loader: getPostsFromFollowing,
      },
      {
        path: "profile/:username",
        element: <Profile />,
        loader: getProfileData,
      },
      {
        path: "profile",
        element: <Profile />,
        loader: getProfileData,
      },
      {
        path: "search",
        element: <Search />,
      },
      {
        path: "chat/:roomId",
        element: <Chat />,
      },
      {
        path: "whiteboard",
        element: <Whiteboard />,
      },
    ],
  },
]);

function AppRouter() {
  return (
    <ChakraProvider>
      <RouterProvider router={router} />
    </ChakraProvider>
  )
}

export default AppRouter;
