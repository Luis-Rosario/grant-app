
import React, { ChangeEvent, useState, SyntheticEvent, useEffect } from 'react';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import axios from "axios";
import { BASE_PATH } from "../../../typescript-axios-client/base"
import * as api from "../../../typescript-axios-client"
import { StudentDTOReq, ReviewerDTOReq, InstitutionDTORes } from "../../../typescript-axios-client/api";
import Select from "react-select";

import "./Register.css";


type FormFiels = {
    username: string,
    password: string,
    name: string,
    birth: string,
    address: string,
    course: string,
    phone: string,
    email: string,
    institution: number,
}

type SponsorSelectEntry = {
    value: number,
    label: string,
}

function Register() {


    const [typeUser, settypeUser] = useState({} as string)
    const [formFields, setformFields] = useState({ username: '', password: '', name: '', birth: '', address: '', course: '', phone: '', email: '', institution: -1 } as FormFiels)
    const [institutions, setInstitutions] = useState([] as InstitutionDTORes[])

    const options: SponsorSelectEntry[] = []

    const institutionList = () => {
        new api.InstitutionControllerApi().getInstitutionsUsingGET({ headers: { 'Authorization': localStorage["jwt"] } }).then(res => {
          
            res && setInstitutions(res.data)})
    }

    useEffect(institutionList,[])

    institutions.map((inst: InstitutionDTORes) => options.push({ value: inst.id, label: inst.name }))


    let selectHandler = (selectedSponsor: any) => {
        formFields.institution = selectedSponsor.value
        setformFields(formFields);
    }

    let inputChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        let field = e.target.id
        if (field === 'registerName') {
            formFields.name = e.target.value
        }

        else if (field === 'registerBirthdate') {
            formFields.birth = e.target.value
        }

        else if (field === 'registerAddress') {
            formFields.address = e.target.value
        }

        else if (field === 'registerCourse') {
            formFields.course = e.target.value
        }

        else if (field === 'registerUsername') {
            formFields.username = e.target.value
        }

        else if (field === 'registerPassowrd') {
            formFields.password = e.target.value
        }

        else if (field === 'registerPhone') {
            formFields.phone = e.target.value
        }

        else if (field === 'registerEmail') {
            formFields.email = e.target.value
        }

        setformFields(formFields);
    }


    let typeUserChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        let type: string = e.target.defaultValue;
       
        settypeUser(type);
       
    }



    function getUserTypeForm() {
        return (
            <div>
                <h1 className="userType__header">Register</h1>

                <h4 className="userType__subheader">I am a:</h4>
                <div className="userType__radio">
                    <div className="radio__pair">
                        <input type="radio" name="studentType" value="STUDENT" onChange={typeUserChangeHandler}></input>
                        <span>Student</span>
                    </div>
                    <div className="radio__pair">
                        <input type="radio" name="studentType" value="SPONSOR" onChange={typeUserChangeHandler}></input>
                        <span>Sponsor</span>
                    </div>
                    <div className="radio__pair">
                        <input type="radio" name="studentType" value="REVIEWER" onChange={typeUserChangeHandler}></input>
                        <span>Reviewer</span>
                    </div>
                </div>
            </div>
        )
    }

    function StudentRegister() {

      
        return (

            <div>

                <div className="register__body">


                    <div className="form__inputPair">
                        <Form.Group controlId="registerName">
                            <Form.Label className="login__labels">Name:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter name" onChange={inputChangeHandler} />
                        </Form.Group>

                        <Form.Group controlId="registerBirthdate">
                            <Form.Label className="login__labels">Birth date:</Form.Label>
                            <Form.Control required type="date" placeholder="Enter birthdate" onChange={inputChangeHandler} />
                        </Form.Group>
                    </div>

                    <div className="form__inputPair">
                        <Form.Group controlId="registerAddress">
                            <Form.Label className="login__labels">Address:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter address" onChange={inputChangeHandler} />
                        </Form.Group>

                        <Form.Group controlId="registerEmail">
                            <Form.Label className="login__labels">Email:</Form.Label>
                            <Form.Control required type="email" placeholder="Enter email" onChange={inputChangeHandler} />
                        </Form.Group>
                    </div>

                    <div className="form__inputPair">
                        <Form.Group controlId="registerCourse">
                            <Form.Label className="login__labels">Course:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter course" onChange={inputChangeHandler} />
                        </Form.Group>

                        <Form.Group controlId="registerInstitutionSelect">
                            <Form.Label className="login__labels">Institution:</Form.Label>
                            <Select className="select__sponsor" id="sponsorSelect" options={options} onChange={selectHandler} />
                        </Form.Group>
                    </div>

                    <div className="form__inputPair">
                        <Form.Group controlId="registerUsername">
                            <Form.Label className="login__labels">Username:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter username" onChange={inputChangeHandler} />
                        </Form.Group>
                        <Form.Group controlId="registerPassowrd">
                            <Form.Label className="login__labels">Password:</Form.Label>
                            <Form.Control required type="password" placeholder="Enter password" onChange={inputChangeHandler} />
                        </Form.Group>
                    </div>


                </div >

                <div className="register__button">
                    <Button variant="primary" type="submit">
                        Register
                        </Button>
                </div>

            </div>
        )
    }


    function SponsorRegister() {
        return (
            <div>

                <div className="register__body">


                    <div className="form__inputPair">
                        <Form.Group controlId="registerName">
                            <Form.Label className="login__labels">Name:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter name" onChange={inputChangeHandler} />
                        </Form.Group>

                        <Form.Group controlId="registerEmail">
                            <Form.Label className="login__labels">Email:</Form.Label>
                            <Form.Control required type="email" placeholder="Enter email" onChange={inputChangeHandler} />
                        </Form.Group>

                    </div>

                    <div className="form__inputPair">
                        <Form.Group controlId="registerPhone">
                            <Form.Label className="login__labels">Phone number:</Form.Label>
                            <Form.Control required type="tel" placeholder="Enter phone number" onChange={inputChangeHandler} />
                        </Form.Group>

                    </div>

                    <div className="form__inputPair">
                        <Form.Group controlId="registerUsername">
                            <Form.Label className="login__labels">Username:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter username" onChange={inputChangeHandler} />
                        </Form.Group>
                        <Form.Group controlId="registerPassowrd">
                            <Form.Label className="login__labels">Password:</Form.Label>
                            <Form.Control required type="password" placeholder="Enter password" onChange={inputChangeHandler} />
                        </Form.Group>
                    </div>


                </div >

                <div className="register__button">
                    <Button variant="primary" type="submit">
                        Register
                    </Button>
                </div>

            </div>

        )
    }

    function ReviewerRegister() {


        return (


            <div>

                <div className="register__body">


                    <div className="form__inputPair">
                        <Form.Group controlId="registerName">
                            <Form.Label className="login__labels">Name:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter name" onChange={inputChangeHandler} />
                        </Form.Group>

                        <Form.Group controlId="registerBirthdate">
                            <Form.Label className="login__labels">Birth date:</Form.Label>
                            <Form.Control required type="date" placeholder="Enter birthdate" onChange={inputChangeHandler} />
                        </Form.Group>
                    </div>

                    <div className="form__inputPair">
                        <Form.Group controlId="registerAddress">
                            <Form.Label className="login__labels">Address:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter address" onChange={inputChangeHandler} />
                        </Form.Group>

                        <Form.Group controlId="registerEmail">
                            <Form.Label className="login__labels">Email:</Form.Label>
                            <Form.Control required type="email" placeholder="Enter email" onChange={inputChangeHandler} />
                        </Form.Group>
                    </div>

                    <div className="form__inputPair">

                        <Form.Group controlId="registerInstitutionSelect">
                            <Form.Label className="login__labels">Institution:</Form.Label>
                            <Select className="select__sponsor" id="sponsorSelect" options={options} onChange={selectHandler} />
                        </Form.Group>
                    </div>

                    <div className="form__inputPair">
                        <Form.Group controlId="registerUsername">
                            <Form.Label className="login__labels">Username:</Form.Label>
                            <Form.Control required type="text" placeholder="Enter username" onChange={inputChangeHandler} />
                        </Form.Group>
                        <Form.Group controlId="registerPassowrd">
                            <Form.Label className="login__labels">Password:</Form.Label>
                            <Form.Control required type="password" placeholder="Enter password" onChange={inputChangeHandler} />
                        </Form.Group>
                    </div>


                </div >

                <div className="register__button">
                    <Button variant="primary" type="submit">
                        Register
                        </Button>
                </div>

            </div>
        )
    }


    function handleSubmit(event: SyntheticEvent) {
      
        event.preventDefault();

        if (formFields.institution === -1 && typeUser !== 'SPONSOR') {
           
            alert("Por favor selecione uma instituição")
        }

        else {


            if (typeUser === 'STUDENT') {
                let user: StudentDTOReq = {
                    address: formFields.address, applications: [], birthDate: formFields.birth,
                    course: formFields.course, email: formFields.email, id: 0, institutionId: formFields.institution,
                    name: formFields.name, password: formFields.password, roles: [typeUser], username: formFields.username, cvId: 0
                }
                axios.post(BASE_PATH + "/signup", user)
                    .then(res => {
                       
                        if (res.status === 200) {
                            window.location.replace("/login")
                        }
                    })
            }
            else if (typeUser === 'REVIEWER') {
                let user: ReviewerDTOReq = {
                    address: formFields.address, birthDate: formFields.birth,
                    email: formFields.email, id: 0, institutionId: formFields.institution,
                    name: formFields.name, password: formFields.password, roles: [typeUser], username: formFields.username, evals: []
                }
             
                axios.post(BASE_PATH + "/signup", user)
                    .then(res => {
                      
                    })
            }
        }
    }

    return (
        <div className="register__container">
            {getUserTypeForm()}
            <Form onSubmit={handleSubmit}>
                {typeUser === 'STUDENT' && StudentRegister()}
                {typeUser === 'SPONSOR' && SponsorRegister()}
                {typeUser === 'REVIEWER' && ReviewerRegister()}
            </Form>

        </div>
    );
}

export default Register;

