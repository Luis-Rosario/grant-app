import React, { ChangeEvent, SyntheticEvent, useEffect, useState } from 'react';
import { Button, Form, Tab, Tabs } from 'react-bootstrap';
import { Dialog } from '@material-ui/core';
import Navbar from '../../Navbar';
import SubmissionTable from './SubmissionTable';
import *  as api from '../../typescript-axios-client/api'
import { GrantDTORes, GrantQuestionRes, ApplicationDTOReq, GrantResponseReq, ApplicationDTO, ApplicationSafeDTO } from '../../typescript-axios-client/api'
import './GrantInfo.css';
import { useHistory } from 'react-router-dom';
import Footer from '../../Footer';


type Answer = {
    answer: string,
}

export type Draft = {
    isDraft: boolean,
    draft: ApplicationDTOReq,
}

const FormRowComponent = ({ question, number, key, mandatory, handleInputChange, savedAns }:
    { question: string, number: number, key: number, mandatory: boolean, handleInputChange: (index: number, event: ChangeEvent<HTMLInputElement>) => void, savedAns: Answer }) =>

    <div key={key}>
        <Form.Group controlId={'question_' + number}>
            <Form.Label className={'question_' + number}>{number}: {question}</Form.Label>  {mandatory && <span className="mandatory__marker">*</span>}
            {mandatory && <Form.Control required as="textarea" rows={3} placeholder="Enter answer" onChange={(event: any) => handleInputChange(number - 1, event)} defaultValue={savedAns.answer} />}
            {!mandatory && <Form.Control as="textarea" rows={3} placeholder="Enter answer" onChange={(event: any) => handleInputChange(number - 1, event)} defaultValue={savedAns.answer} />}
        </Form.Group>
    </div>

export const ApplicationForm = ({ questions, answersNumb, grantId, draft }: { questions: GrantQuestionRes[], answersNumb: number, grantId: number, draft: Draft }) => {

    let history = useHistory();

    //creates answer array based on how many grant questions there are
    let createAnswerArray = () => {
        let ansArray: Answer[] = []
        for (let i: number = 0; i < answersNumb; i++) {
            if (draft.isDraft) {
                ansArray.push({ answer: (draft.draft.responses[i] as GrantResponseReq).response })
            }
            else {
                ansArray.push({ answer: '' })
            }
        }

        return ansArray;
    };

    const [inputFields, setInputFields] = useState(createAnswerArray);


    const submitHandler = (event: SyntheticEvent, saveAsDraft: boolean) => {
        event.preventDefault();
        var dateObj = new Date();
        let answers: Array<GrantResponseReq> = []
        let application: ApplicationDTOReq = undefined as unknown as ApplicationDTOReq;
        let isValid = true;

        inputFields.map(
            (answer: Answer, index: number) => {
                if (questions[index].mandatory && answer.answer === '') {
                    isValid = false;
                }
                answers.push({ question: questions[index], response: answer.answer } as GrantResponseReq)
            }

        )

        if (isValid) {


            //TO SUBMIT OBJ
            if (!saveAsDraft) {

                //If it wasnt a draft make application object
                if (!draft.isDraft) {
                    application = {
                        applicationID: 0,
                        grantID: grantId,
                        responses: answers,
                        reviews: [],
                        status: api.ApplicationDTOReqStatusEnum.Submitted,
                        studentID: +window.localStorage["id"],
                        submissionDate: dateObj.toISOString(),
                    }
                }

                //if it was a draft update answears and submission date
                else {
                    draft.draft.responses = answers
                    draft.draft.submissionDate = dateObj.toISOString()
                    draft.draft.status = api.ApplicationDTOReqStatusEnum.Submitted
                    application = draft.draft;

                }
            }

            //TO SAVE AS DRAFT OBJ
            else {
                //if it is a new draft just build the application object
                if (!draft.isDraft) {
                    application = {
                        applicationID: 0,
                        grantID: grantId,
                        responses: answers,
                        reviews: [],
                        status: api.ApplicationDTOReqStatusEnum.Draft,
                        studentID: +window.localStorage["id"],
                        submissionDate: dateObj.toISOString(),
                    }
                }

                //if you want to save the draft again just make a put with updated answears and submission date
                else {
                    draft.draft.responses = answers
                    draft.draft.submissionDate = dateObj.toISOString()
                    application = draft.draft;
                }
            }

            //CREATE NEW APPLICATION IN API
            if (!draft.isDraft) {
                new api.ApplicationControllerApi().createApplicationUsingPOST(application, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {
                  

                    //remove history state when editing info in appInfo so that old application info wont be rendered
                    if (window.location.href.includes("appinfo"))
                        history.push('' + application.applicationID)

                    window.location.reload();

                }).catch(
                    (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
            }

            //UPDATE PREVIOUS APPLICATION DRAFT IN API
            else {
                new api.ApplicationControllerApi().updateApplicationUsingPUT(application, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {
                  

                    //remove history state when editing info in appInfo so that old application info wont be rendered
                    if (window.location.href.includes("appinfo"))
                        history.push('' + application.applicationID)

                    window.location.reload();
                }).catch(
                    (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))

            }
        }
    }


    const handleInputChange = (index: number, event: ChangeEvent<HTMLInputElement>) => {

        inputFields[index].answer = event.target.value;
        setInputFields(inputFields);
       
    };


    return (
        <Form className="formModal__container" validated>
            {questions.map(
                (question: GrantQuestionRes, index: number) => <FormRowComponent question={question.fieldDescription} number={index + 1} key={index} mandatory={question.mandatory} handleInputChange={handleInputChange} savedAns={inputFields[index]} />
            )}
            <span className="mandatory__marker">* Mandatory</span>
            <div className="button__group">
                <Button variant="primary" type="submit" onClick={(ev) => submitHandler(ev, false)}>Submit</Button>
                <Button variant="primary" type="submit" onClick={(ev) => submitHandler(ev, true)}>Save as Draft</Button>
            </div>

        </Form>
    )
}



const ColumnGrantPropreties = ({ grant }: { grant: GrantDTORes }) => {


    return (
        <>
            <div className="grant__boxItem">
                <h5>Funding: {grant.funding}</h5>
            </div>

            <div className="grant__boxItem">
                <h5>Submited on: {grant.openingDate}</h5>
            </div>

            <div className="grant__boxItem">
                <h5>Deadline: {grant.deadline}</h5>
            </div>

            <div className="grant__boxItem">
                <h5>Sponsored By: {grant.sponsorName}</h5>
            </div>



        </>


    )
}




function GrantInfo(props: { userId: number, userType: string }) {

    let fromRouting: boolean = false;

    let history = useHistory();

    let preSelectedGrant: GrantDTORes = {} as GrantDTORes;

    //See if we have the grant object in our routing history to avoid making one more request to the api
    if (history.location.state !== undefined) {
        let loadedGrantFromRouting: any[] = history.location.state as any
        preSelectedGrant = loadedGrantFromRouting[0] as GrantDTORes
        fromRouting = true
    }


    const [popup, setPopup] = useState(false);
    const [grantInfo, setgrantInfo] = useState(preSelectedGrant as GrantDTORes);
    const [grantApps, setgrantApps] = useState([] as ApplicationSafeDTO[]);
    const [showApply, setShowApply] = useState(true);
    const [studApps, setStudApps] = useState(undefined as unknown as ApplicationDTO[]);
    const [editDraft, setEditDraft] = useState({ isDraft: false, draft: {} as ApplicationDTOReq } as Draft);

    let grantId: number = +window.location.pathname.split('/')[2]  //Gets the id from the Url

    //Obter a info dos grants
    const loadGrantDetails = () => {
        new api.GrantControllerApi().getGrantUsingGET(grantId, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && setgrantInfo(res.data)).catch(
            (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
    }

    //obter as applicações do grant
    const loadGrantApplications = (status: api.ApplicationDTOReqStatusEnum) => {
        new api.GrantControllerApi().getGrantApplicationsUsingGET(grantId, status, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && setgrantApps(res.data))
  
    }

    //loads the students applications
    const loadStudentApplications = () => {
        new api.StudentControllerApi().getStudentApplicationsUsingGET(props.userId, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (console.log(res), setStudApps(res.data)))
    
    }

    //logic on what to show to the student. If already applied dont show apply button and show all aplications of that grant
    const hasAlreadyApplied = () => {
        if (props.userType.includes("ROLE_STUDENT")) {
            if (studApps !== undefined) {
                let toShowApply: boolean = true;
                studApps.forEach(app => {
                   
                    if (app.grantID === (grantId)) {  //if student has an application for this grant 
                        if (app.status !== 'DRAFT') {
                            loadGrantApplications(undefined as unknown as api.ApplicationDTOReqStatusEnum)
                            setShowApply(false);
                            toShowApply = false;
                        }
                        else {

                            let nApp: ApplicationDTOReq = {
                                applicationID: app.applicationID,
                                grantID: app.grantID,
                                responses: app.responses,
                                reviews: app.reviews,
                                status: api.ApplicationDTOReqStatusEnum.Draft,
                                studentID: app.studentID,
                                submissionDate: app.submissionDate,
                            }

                            setEditDraft({ isDraft: true, draft: nApp })
                        }

                    }

                })
                if (toShowApply && grantInfo !== undefined) {
                    if (Date.parse(grantInfo.deadline) < Date.now()) {
                        setShowApply(false)
                    }
                }
            }


        }

        //logic on what to show to the reviwer
        else if (props.userType.includes("ROLE_REVIEWER")) {

            setShowApply(false);
        }


    }


    useEffect(() => {

        if (!fromRouting) {
            loadGrantDetails();  //get grant info
        }

        if (props.userType.includes("ROLE_STUDENT")) {  //get student info
            if (studApps === undefined && !isNaN(props.userId))
                loadStudentApplications()

            if (grantInfo !== undefined && isGrantClosed(grantInfo)) {
                loadGrantApplications(api.ApplicationDTOReqStatusEnum.Granted);
            }
        }

        else if (props.userType.includes("ANONYMOUS") && grantInfo.grantId !== undefined) {

            let deadline: number = Date.parse(grantInfo.deadline)
            if (deadline < Date.now()) {
                loadGrantApplications(api.ApplicationDTOReqStatusEnum.Granted);
            }
            setShowApply(false)
        }

        else {
            loadGrantApplications(undefined as unknown as api.ApplicationDTOReqStatusEnum)
        }

        //if wont show apply button, show application tab
        if (showApply) {
            hasAlreadyApplied()  //display apply button and submitted grant applications tab or not
        }

    }, [props, studApps, showApply]);




    const handleClose = () => {
        setPopup(false);
    };

    const openApplicationPopup = () => {
        setPopup(true)
    }

    const isGrantClosed = (grantInfo: api.GrantDTORes): boolean => {
        return Date.parse(grantInfo.deadline) < Date.now()
    }

    const canSeeSubmissionTab = (grantInfo: api.GrantDTORes): boolean => {
        if (props.userType.includes("ANONYMOUS") && isGrantClosed(grantInfo)) {
            return true;
        }
        else if (!props.userType.includes("ANONYMOUS")) {
            return !showApply
        }
    }



    return (
        <div>
            <Navbar />
            <div className="grant__container">
                <div className="ContentTab">
                    <Tabs defaultActiveKey="grantInfo" id="uncontrolled-tab-example">
                        <Tab eventKey="grantInfo" title="Grant Info">
                            <div className="grantInfo__container">
                                <div className="grant__title">
                                    <h3>{grantInfo.title}</h3>
                                </div>
                                <div className="grant__information">

                                    <div className="grant__description">
                                        <h6>{grantInfo.description}</h6>
                                    </div>

                                    <div className="grantdescBox__container">
                                        <ColumnGrantPropreties grant={grantInfo} />


                                        {(showApply && !editDraft.isDraft) && <div className="apply__button">
                                            <Button variant="primary" onClick={openApplicationPopup}>Apply</Button>

                                            <Dialog open={popup} onClose={handleClose} aria-labelledby="alert-modal-title" aria-describedby="simple-modal-description">

                                                <div className="modal_group">

                                                    <h4 className="form__title"> Fill your application form</h4>
                                                    {grantInfo.applicationQuestions !== undefined &&
                                                        <ApplicationForm answersNumb={grantInfo.applicationQuestions.length} questions={grantInfo.applicationQuestions} grantId={grantInfo.grantId} draft={editDraft} />}
                                                </div>
                                            </Dialog>
                                        </div>
                                        }

                                        {grantInfo !== undefined && isGrantClosed(grantInfo) &&
                                            <div className="closed__message">
                                                <h4>Grant Applications closed</h4>
                                            </div>
                                        }

                                        {editDraft.isDraft && grantInfo !== undefined && !isGrantClosed(grantInfo) && <div className="editDraft__button">
                                            <Button variant="primary" onClick={openApplicationPopup}>Edit Draft</Button>

                                            <Dialog open={popup} onClose={handleClose} aria-labelledby="alert-modal-title" aria-describedby="simple-modal-description">

                                                <div className="modal_group">

                                                    <h4 className="form__title"> Fill your application form</h4>
                                                    {grantInfo.applicationQuestions !== undefined &&
                                                        <ApplicationForm answersNumb={grantInfo.applicationQuestions.length} questions={grantInfo.applicationQuestions} grantId={grantInfo.grantId} draft={editDraft} />}
                                                </div>
                                            </Dialog>

                                        </div>}

                                    </div>
                                </div>

                            </div>
                        </Tab>

                        {grantInfo !== undefined && canSeeSubmissionTab(grantInfo) && <Tab eventKey="submtApps" title="Submited Applications">
                            <div className="grantInfo__container">
                                <SubmissionTable applications={grantApps} title={grantInfo.title} userType={props.userType} />
                            </div>
                        </Tab>}


                    </Tabs>
                </div>
            </div>

            <div className="footer">
                <Footer />
            </div>
        </div>
    )

}

export default GrantInfo;

