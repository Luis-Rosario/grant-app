import React, { ChangeEvent, SyntheticEvent, useState } from 'react';
import { NavLink } from "react-router-dom";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import './Login.css';
import axios from 'axios';
import { BASE_PATH } from "../../typescript-axios-client/base"

function Login() {

    const [username, setusername] = useState({})
    const [password, setpassword] = useState({})
    const [errorMessage, setErrorMessage] = useState(false)


    const usernameChangeHandler = (e: ChangeEvent<HTMLInputElement>) => { setusername(e.target.value); setErrorMessage(false) }
    const passwordChangeHandler = (e: ChangeEvent<HTMLInputElement>) => { setpassword(e.target.value); setErrorMessage(false) }


    const handleSubmit = (event: SyntheticEvent) => {
        event.preventDefault();
        axios.post(BASE_PATH + "/login", { 'username': username, 'password': password })
            .then(res => {

                if (res.status === 200) {
                    window.localStorage["jwt"] = res.headers.authorization
                    window.localStorage["roles"] = res.headers.roles
                    window.localStorage["username"] = username
                    window.location.replace("/home")
                }

            }).catch((error) => {
                if (error.response.status === 401)
                    setErrorMessage(true)
            });
    }

    return (
        <div className="login__container">
            <h1 className="login__header">Login</h1>
            <div className="login__inputs">
                <Form onSubmit={handleSubmit}>
                    <Form.Group controlId="loginUsername">
                        <Form.Label className="login__labels">Username:</Form.Label>
                        <Form.Control required type="text" placeholder="Enter username" onChange={usernameChangeHandler} />
                    </Form.Group>
                    <Form.Group controlId="loginPassword">
                        <Form.Label className="login__labels">Password:</Form.Label>
                        <Form.Control required type="password" placeholder="Enter password" onChange={passwordChangeHandler} />
                    </Form.Group>
                    {errorMessage && <h6 className="error__message">Incorrect Username Or password</h6>}

                    <div className="login__button">
                        <Button variant="primary" type="submit">
                            Login
                        </Button>
                    </div>

                </Form>
            </div>

            <div className="register__linkContainer">
                <label>Don't have an account?</label> <NavLink className="register__link" to="/register"> Register</NavLink>
                <div className="anon__linkContainer">
                    <label>Or</label>
                    <NavLink className="anon__link" to="/home"> Procede without login in</NavLink>
                </div>
            </div>

        </div >
    );
}

export default Login;