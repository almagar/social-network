import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import AppRouter from './AppRouter';
import reportWebVitals from './reportWebVitals';
import Keycloak from 'keycloak-js';

let initOptions = {
  url: `${process.env.REACT_APP_KEYCLOAK_URL}/auth`,
  redirectUri: process.env.REACT_APP_REDIRECT_URI,
  realm: "socialnetwork",
  clientId: "socialnetworkclient",
  onLoad: "login-required",
  checkLoginIframe: false,
  token: null,
  refreshToken: null
}


const keycloak = new Keycloak(initOptions);

keycloak.init(initOptions)
  .then(auth => {

    if (!auth) {
      window.location.reload();
      return;
    } else {
      console.info("Authenticated");
      localStorage.setItem("sn-token", keycloak.token);
      localStorage.setItem("sn-refresh-token", keycloak.refreshToken);
    }

    const root = ReactDOM.createRoot(document.getElementById("root"));
    root.render(
      <AppRouter />
    );

    setInterval(() => {
      keycloak.updateToken(130).then(refreshed => {
        if (refreshed) {
          console.debug("Token refreshed " + refreshed);
          localStorage.setItem("sn-token", keycloak.token);
          localStorage.setItem("sn-refresh-token", keycloak.refreshToken);
        } else {
          console.warn("Token not refreshed, valid for "
            + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + " seconds");
        }
      }).catch(() => {
        console.error("Failed to refresh token");
      });
    }, 60000)

    // If you want to start measuring performance in your app, pass a function
    // to log results (for example: reportWebVitals(console.log))
    // or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
    reportWebVitals();
  }).catch(() => {
    console.error("Keycloak authentication failed")
  });
