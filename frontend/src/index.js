import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import Keycloak from 'keycloak-js';

let initOptions = {
  url: "http://localhost:8282/auth",
  redirectUri: "http://localhost:3000/",
  realm: "socialnetwork",
  clientId: "socialnetworkclient",
  onLoad: "login-required",
  checkLoginIframe: false
}


const keycloak = new Keycloak(initOptions);

keycloak.init({
    onLoad: initOptions.onLoad,
    checkLoginIframe: initOptions.checkLoginIframe,
    redirectUri: initOptions.redirectUri
  })
  .then(auth => {

    if (!auth) {
      window.location.reload();
    } else {
      console.info("Authenticated");
    }

    const root = ReactDOM.createRoot(document.getElementById("root"));
    root.render(
      <React.StrictMode>
        <App />
      </React.StrictMode>
    );

    localStorage.setItem("sn-token", keycloak.token);
    localStorage.setItem("sn-refresh-token", keycloak.refreshToken);

    setTimeout(() => {
      keycloak.updateToken(70).then(refreshed => {
        if (refreshed) {
          console.debug("Token refreshed" + refreshed);
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