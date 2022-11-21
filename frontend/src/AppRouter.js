import { ChakraProvider } from "@chakra-ui/react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "./components/Layout";
import Posts from "./components/Posts";
import Profile from "./components/Profile";
import getPostsFromFollowing from "./loaders/posts";
import getProfileData from "./loaders/profile";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        path: "",
        element: <Posts />,
        loader: getPostsFromFollowing,
      },
      {
        path: "profile",
        element: <Profile />,
        loader: getProfileData,
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
