import React, { ChangeEvent, useEffect, useRef, useState } from 'react'
import Navbar from "../../Navbar"

import "./Profile.css";
import profileImage from "../../misc/avatar-profile.jpg"
import { Button, Form } from 'react-bootstrap';
import *  as api from '../../typescript-axios-client/api'
import { SafeStudentDTO, SafeSponsorDTOReq } from '../../typescript-axios-client/api';
import { SafeReviewerDTO } from '../../typescript-axios-client/api';
import Footer from '../../Footer';
import { useHistory } from 'react-router-dom';
import { Dialog } from '@material-ui/core';





const EditInfoForm = ({ userDets, userType }: { userDets: (api.SafeStudentDTO | api.SafeReviewerDTO), userType: string }) => {



    let history = useHistory();
    let coursePlaceholder: string = (userDets as api.SafeStudentDTO).course

    const [address, setAddress] = useState(userDets.address)
    const [email, setEmail] = useState(userDets.email)
    const [course, setCourse] = useState(coursePlaceholder)
   
   
    const updateUser = (user: (api.SafeStudentDTO | api.SafeReviewerDTO), userType: string):  (api.SafeStudentDTO | api.SafeReviewerDTO) => {

        let userToRet:  (api.SafeStudentDTO | api.SafeReviewerDTO) = undefined as unknown as (api.SafeStudentDTO | api.SafeReviewerDTO);
       
        if (userType.includes("ROLE_STUDENT")) {

            userToRet = {
                address: address,
                applications: (user as api.SafeStudentDTO).applications,
                birthDate: user.birthDate,
                course: course,
                cvId: (user as api.SafeStudentDTO).cvId,
                email: email,
                id: user.id,
                institutionId: user.institutionId,
                name: user.name,
                username: (user as api.SafeStudentDTO).username,
            }

        }
        if (userType.includes("ROLE_REVIEWER")) {

            userToRet = {
                address: address,
                evals: (user as api.SafeReviewerDTO).evals,
                birthDate: user.birthDate,
                email: email,
                id: user.id,
                institutionId: user.institutionId,
                name: user.name,
                username: (user as api.SafeReviewerDTO).username,
            }

        }
        return userToRet;
    }

    return (
        <Form className="formModal__container">
            <div>
            </div>
            <div key={0}>
                <Form.Group controlId={'question_0'}>
                    <Form.Label className={'question_0'}>Address</Form.Label>
                    <Form.Control as="textarea" rows={1} placeholder="Enter address" onChange={(event: ChangeEvent<HTMLInputElement>) => setAddress(event.target.value)} defaultValue={address} />
                </Form.Group>
            </div>

            <div key={1}>
                <Form.Group controlId={'question_1'}>
                    <Form.Label className={'question_1'}>Email</Form.Label>
                    <Form.Control as="textarea" rows={1} placeholder="Enter email" onChange={(event: ChangeEvent<HTMLInputElement>) => setEmail(event.target.value)} defaultValue={email} />
                </Form.Group>
            </div>


            {userType.includes("ROLE_STUDENT") && <div key={1}>
                <Form.Group controlId={'question_2'}>
                    <Form.Label className={'question_2'}>Course</Form.Label>
                    <Form.Control as="textarea" rows={1} placeholder="Enter answer" onChange={(event: ChangeEvent<HTMLInputElement>) => setCourse(event.target.value)} defaultValue={course} />
                </Form.Group>
            </div>
            }


            {userType.includes("ROLE_STUDENT") &&
                <div className="button__group">
                    <Button variant="primary" type="submit" onClick={() => new api.StudentControllerApi().updateStudentUsingPUT(updateUser(userDets, userType) as SafeStudentDTO, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (window.location.reload())).catch(
                        (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))}>Save</Button>
                </div >}

            {userType.includes("ROLE_REVIEWER") &&
                <div className="button__group">
                    <Button variant="primary" type="submit" onClick={() => new api.ReviewerControllerApi().updateReviewerUsingPUT(updateUser(userDets, userType) as SafeReviewerDTO, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (window.location.reload())).catch(
                        (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))}>Save</Button>
                </div >}

        </Form >
    )
}


const StudentDetails = ({ details, instName, userType }: { details: api.SafeStudentDTO, instName: string, userType: string }) => {

    const [editPop, seteditPop] = useState(false);
    const [cv, setCV] = useState(undefined as unknown as File);
    const uploadCV = useRef(null)

    const onChangeFile = (event:any) => {
        event.stopPropagation();
        event.preventDefault();
        var file:File = event.target.files[0];
    
        setCV(file); 
    }
  

    return (
        <div className="profile__panel">

            <div className="group_infoAndButtons">
                <div className="info__container">

                    <div>

                        <div className="detail__row" >

                            <div className="info__pair">
                                <h4>Name: </h4>
                                <h6 className="info__field">{details.name}</h6>
                            </div>
                            <div className="info__pair">
                                <h4 >Email: </h4>
                                <h6 className="info__field">{details.email}</h6>
                            </div>

                        </div>

                        <div className="detail__row" >

                            <div className="info__pair">
                                <h4>Birthdate: </h4>
                                <h6 className="info__field">{details.birthDate}</h6>
                            </div>
                            <div className="info__pair">
                                <h4 >Address: </h4>
                                <h6 className="info__field">{details.address}</h6>
                            </div>

                        </div>

                        <div className="detail__row" >
                            <div className="info__pair">
                                <h4>Course: </h4>
                                <h6 className="info__field">{details.course}</h6>
                            </div>
                            <div className="info__pair">
                                <h4>Institution: </h4>
                                <h6 className="info__field">{instName}</h6>
                            </div>
                        </div>
                    </div>

                    <div className="profile__imageContainer">
                        <img alt='' className="profile__picture" src={profileImage} />
                    </div>

                </div>

                <div className="button__group">
                    <div className="cv__group">
                        <input id="myInput"
                            type="file"
                            style={{ display: 'none' }}
                            onChange={(e)=> onChangeFile(e)}
                            ref={uploadCV}
                            accept=".pdf"
                        />
                        <Button variant="primary" onClick={() => { uploadCV.current.click() }}>Upload CV</Button>
                       {cv !== undefined && <label className="cv_desc">{cv.name}</label> }

                    </div>

                    <Button variant="primary" onClick={() => seteditPop(true)}>Edit</Button>


                    <Dialog open={editPop} onClose={() => seteditPop(false)} aria-labelledby="alert-modal-title" aria-describedby="simple-modal-description">

                        <div className="modal_group">
                            <h4 className="form__title">Edit info</h4>

                            <EditInfoForm userDets={details} userType={userType} />
                        </div>

                    </Dialog>
                </div>


            </div>

        </div >

    )
}

const ReviewerDetails = ({ details, instName, userType }: { details: api.SafeReviewerDTO, instName: string, userType: string }) => {

    const [editPop, seteditPop] = useState(false);




    return (
        <div className="profile__panel">

            <div className="group_infoAndButtons">
                <div className="info__container">

                    <div>

                        <div className="detail__row">

                            <div className="info__pair">
                                <h4>Name: </h4>
                                <h6 className="info__field">{details.name}</h6>
                            </div>
                            <div className="info__pair">
                                <h4>Email: </h4>
                                <h6 className="info__field">{details.email}</h6>
                            </div>

                        </div>

                        <div className="detail__row">

                            <div className="info__pair">
                                <h4>Birthdate: </h4>
                                <h6 className="info__field">{details.birthDate}</h6>
                            </div>
                            <div className="info__pair">
                                <h4>Address: </h4>
                                <h6 className="info_field">{details.address}</h6>
                            </div>

                        </div>

                        <div className="detail__row">
                            <div className="info__pair">
                                <h4>Instituition: </h4>
                                <h6 className="info__field">{instName}</h6>
                            </div>
                        </div>
                    </div>

                    <div className="profile__imageContainer">
                        <img alt='' className="profile__picture" src={profileImage} />


                        <Button variant="primary" onClick={() => seteditPop(true)}>Edit</Button>


                        <Dialog open={editPop} onClose={() => seteditPop(false)} aria-labelledby="alert-modal-title" aria-describedby="simple-modal-description">

                            <div className="modal_group">
                                <h4 className="form__title">Edit info</h4>

                                <EditInfoForm userDets={details} userType={userType} />
                            </div>

                        </Dialog>
                    </div>
                </div>
            </div>
        </div>
    )
}

const SponsorDetails = ({ details }: { details: SafeSponsorDTOReq }) => {
    return (
        <div className="profile__panel">

            <div className="group_infoAndButtons">
                <div className="info__container">

                    <div>

                        <div className="detail__row">

                            <div className="info__pair">
                                <h4>Name: </h4>
                                <h6 className="info__field">{details.name}</h6>
                            </div>
                            <div className="info__pair">
                                <h4>Email: </h4>
                                <h6 className="info__field">{details.email}</h6>
                            </div>

                        </div>

                        <div className="detail__row">
                            <div className="info__pair">
                                <h4>Phone Number: </h4>
                                <h6 className="info__field">{details.phoneNumber}</h6>
                            </div>
                        </div>
                    </div>

                    <div className="profile__imageContainer">
                        <img alt='' className="profile__picture" src={profileImage} />
                    </div>
                </div>
            </div>
        </div>
    )
}

function Profile(props: { userId: number, userType: string }) {
    let history = useHistory();

    const [user, setUser] = useState([] as SafeStudentDTO[] | api.SafeReviewerDTO[] | api.SafeSponsorDTORes[])
    const [instName, setInstName] = useState(undefined as unknown as string);

    const loadUserDetails = () => {
        let username = window.localStorage['username']

        if (window.location.pathname === "/profile") {

            if (props.userType.includes("ROLE_STUDENT")) {
                new api.StudentControllerApi().getAllStudentsUsingGET(undefined, undefined, username, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (getInstitutionName(res.data[0].institutionId), setUser(res.data))).catch(
                    (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
            }

            else if (props.userType.includes("ROLE_REVIEWER")) {
                new api.ReviewerControllerApi().getReviewersUsingGET(undefined, undefined, username, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (getInstitutionName(res.data[0].institutionId), setUser(res.data))).catch(
                    (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
            }

            else if (props.userType.includes("ROLE_SPONSOR")) {
                new api.SponsorControllerApi().getSponsorsUsingGET(username, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && setUser(res.data)).catch(
                    (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
            }
        }
    }


    const getInstitutionName = (instId: number) => {
        new api.InstitutionControllerApi().getInstitutionUsingGET((instId), { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (setInstName(res.data.name))).catch(
            (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
    }

    useEffect(() => {
        loadUserDetails()
    }, [])

    return (
        <div className="profilepage__container">
            <Navbar />
            <div>
                {user.length !== 0 && instName !== undefined && <div className="profile__container">
                    {props.userType.includes("ROLE_STUDENT") && <StudentDetails details={user[0] as SafeStudentDTO} instName={instName} userType={props.userType} />}
                    {props.userType.includes("ROLE_REVIEWER") && <ReviewerDetails details={user[0] as SafeReviewerDTO} instName={instName} userType={props.userType} />}
                    {props.userType.includes("ROLE_SPONSOR") && <SponsorDetails details={user[0] as SafeSponsorDTOReq} />}
                </div>}
            </div>
            <div className="footer">
                <Footer />
            </div>

        </div>
    )
}

export default Profile;
