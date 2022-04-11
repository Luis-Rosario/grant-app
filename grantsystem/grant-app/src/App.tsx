import React, { useEffect, useState } from 'react';
import './App.css';
import { BrowserRouter as Router, Switch, Route, Redirect, useHistory } from 'react-router-dom';
import jwt_decode from "jwt-decode";


import Error from './Error';
import Homepage from './Pages/Homepage/Homepage';
import Login from './Pages/Sign in/Login';
import Register from './Pages/Sign in/Register/Register';
import Profile from './Pages/Profile/Profile';
import ApplicationInfo from './Pages/ApplicationInfo/ApplicationInfo';
import GrantInfo from './Pages/GrantInfo/GrantInfo';
import MyPanels from './Pages/Panels/MyPanels';
import axios from 'axios';
import { BASE_PATH } from "./typescript-axios-client/base"
import *  as api from './typescript-axios-client/api'
import { SafeReviewerDTO, SafeStudentDTO } from './typescript-axios-client/api';

type JWT = {
  exp: number,
  iat: number,
  roles: string[],
  sub: string,
  username: string,
}

function App() {

  const [userType, setuserType] = useState('')
  const [userId, setUserId] = useState(NaN)
  let history = useHistory();

  const logAnon = () => {

    localStorage.clear()
    localStorage["roles"] = "ANONYMOUS"
    axios.post(BASE_PATH + "/anon")
      .then(res => {
        if (res.status === 200) {
          window.localStorage["jwt"] = res.headers.authorization
          setuserType("ANONYMOUS");
        }
      })
  }

  const initUserState = () => {

    if (localStorage["roles"] === undefined) {
      logAnon()
    }

    else {

      if (localStorage["jwt"] !== undefined) {

        let decoded: JWT = jwt_decode(localStorage["jwt"]);

        //if token has expired sign in as anon (exp in sec and Date.now() in ms) 
        if (decoded.exp < (Date.now() / 1000)) {
          logAnon();
        }
      }

      if (localStorage["roles"].includes('ROLE_STUDENT') && isNaN(userId)) {


        new api.StudentControllerApi().getAllStudentsUsingGET(undefined, undefined, localStorage["username"], { headers: { 'Authorization': localStorage["jwt"] } }).then(
          res => res && ((localStorage["id"] = (res.data[0] as SafeStudentDTO).id), setUserId((res.data[0] as SafeStudentDTO).id))).catch(
            (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
        setuserType("ROLE_STUDENT")
      }

      else if (localStorage["roles"].includes('ROLE_REVIEWER') && isNaN(userId)) {

        new api.ReviewerControllerApi().getReviewersUsingGET(undefined, undefined, localStorage["username"], { headers: { 'Authorization': localStorage["jwt"] } }).then(
          res => res && ((localStorage["id"] = (res.data[0] as SafeReviewerDTO).id), setUserId((res.data[0] as SafeReviewerDTO).id))).catch(
            (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
        setuserType("ROLE_REVIEWER")
      }

      else if (localStorage["roles"].includes('ROLE_SPONSOR') && isNaN(userId)) {
        setuserType("ROLE_SPONSOR")
      }

      else if (localStorage["roles"].includes('ANONYMOUS') && (userType === '')) {    //Edge case where on startup localstorage had annon user
        logAnon()
      }
    }

  }


  useEffect(() => {
    initUserState()
  }, []);


  return (
    <Router>

      <Switch>

        <Route
          exact
          path="/"
          render={() => {
            return (
              <Redirect to="/home" />
            )
          }}
        />
        <Route path='/home' exact render={() => (!isNaN(userId) || userType.includes('ANONYMOUS')) && <Homepage userId={userId} userType={userType} />} />
        <Route path='/login' exact component={Login} />
        <Route path='/register' exact component={Register} />
        <Route path='/profile/:handle' render={() => <Profile userId={userId} userType={userType} />} />
        <Route path='/appinfo/:handle' render={() => <ApplicationInfo userId={userId} userType={userType} username={window.localStorage['username']} />} />
        <Route path='/grant/:handle' render={() => <GrantInfo userId={userId} userType={userType} />} />
        <Route path='/panels' render={() => !isNaN(userId) && <MyPanels userId={userId} userType={userType} />} />
        <Route path='/panels/:handle' render={() => !isNaN(userId) && <MyPanels userId={userId} userType={userType} />} />
        <Route path='/profile' exact render={() => !isNaN(userId) && <Profile userId={userId} userType={userType} />} />
        <Route path='/error' exact component={Error} />
        <Redirect to='/error' />
      </Switch>



    </Router>

  );
}

export default App;
