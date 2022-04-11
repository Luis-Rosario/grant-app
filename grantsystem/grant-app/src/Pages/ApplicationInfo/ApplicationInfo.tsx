import React, { useEffect, useState } from 'react';
import Navbar from "../../Navbar";
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import { Button } from 'react-bootstrap';

import './ApplicationInfo.css';
import PanelReviews from './PanelReviews';
import NewReview from './NewReview';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogTitle from '@material-ui/core/DialogTitle';
import { ApplicationDTORes, GrantResponseRes, GrantDTORes, SafeStudentDTO, ApplicationDTO, ApplicationDTOReq, EvaluationDTORes, SafeReviewerDTO, PanelDTO, IdAndName } from '../../typescript-axios-client';
import *  as api from '../../typescript-axios-client/api'
import { useHistory } from 'react-router-dom';
import { ApplicationEntry } from '../Homepage/student/ApplicationTable';
import { Draft, ApplicationForm } from '../GrantInfo/GrantInfo';
import Footer from '../../Footer';



const QuestionComponent = ({ question, val }: { question: GrantResponseRes, val: number }) => {

    return (
        <div className="question__group">
            <div className="question__text">
                <h5> <span className="ans_tag">{val + 1}: </span>{question.question.fieldDescription}</h5>
            </div>

            <div className="question__answear">
                <h6>  <span className="ans_tag">Answer: </span> {question.response}</h6>
            </div >

        </div>

    )
}

const ApplicationQuestions = ({ res }: { res: GrantResponseRes[] }) => {
    return (
        <div className="appQuestion__container">
            <div className="appQuestion__wrapper">
                {res.map(
                    (response: GrantResponseRes, index: number) => (<QuestionComponent val={index} question={response} />)
                )}
            </div>
        </div>
    )
}

const ApplicationDetails = ({ details, grantInfo, student, instName }: { details: ApplicationDTORes, grantInfo: GrantDTORes, student: SafeStudentDTO, instName: String }) => {
    return (
        <div className="appInfo__details">

            <div className="appInfo__wrapper">
                <h3>Application details</h3>

                <div className="appInfo__row">
                    <h5>Application id: </h5>
                    <h6> {details.applicationID}</h6>
                </div>

                <div className="appInfo__row">
                    <h5>Grant: </h5>
                    <h6> {grantInfo.title}</h6>
                </div>
                <div className="appInfo__row">
                    <h5>Description: </h5>
                    <h6> {grantInfo.description}</h6>
                </div>


                <div className="appInfo__row">
                    <h5>Student: </h5>
                    <h6> {student.name}</h6>
                </div>

                <div className="appInfo__row">
                    <h5>Institution: </h5>
                    <h6> {instName} </h6>
                </div>
                <div className="appInfo__row">
                    <h5>Course: </h5>
                    <h6> {student.course}</h6>
                </div>
                <div className="appInfo__row">
                    <h5>email: </h5>
                    <h6>{student.email}</h6>
                </div>
                <div className="appInfo__row">
                    <h5>Submission date: </h5>
                    <h6> {details.submissionDate}</h6>
                </div>
                <div className="appInfo__row">
                    <h5>Status: </h5>
                    <h6>{details.status}</h6>
                </div>
            </div>
            {details.status === api.ApplicationDTOResStatusEnum.Draft && <DraftButtons details={details} grantInfo={grantInfo} />}

        </div>
    )
}

export const ConfirmationDialog = ({ popupSub, handleClose, messageTitle, submitHandler, confirmMessage, refuseMessage }:
    {
        popupSub: boolean, handleClose: () => void, messageTitle: string, submitHandler: () => void,
        confirmMessage: string, refuseMessage: string
    }) => {


    return (

        <Dialog open={popupSub} onClose={handleClose} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
            <DialogTitle id="alert-dialog-title">{messageTitle}</DialogTitle>
            <DialogActions>
                <Button onClick={submitHandler} color="primary" autoFocus>{confirmMessage}</Button>
                <Button onClick={handleClose} color="danger" variant="danger">{refuseMessage}</Button>
            </DialogActions>
        </Dialog>
    )
}

const convertAppDTOResToReq = (appIn: ApplicationDTORes, status: api.ApplicationDTOReqStatusEnum): ApplicationDTOReq => {

    let app: ApplicationDTOReq
    app = {
        applicationID: appIn.applicationID,
        grantID: appIn.grantID,
        responses: appIn.responses,
        reviews: appIn.reviews,
        status: status,
        studentID: appIn.studentID,
        submissionDate: appIn.submissionDate,
    }
    return app;
}

const DraftButtons = ({ details, grantInfo }: { details: ApplicationDTORes, grantInfo: GrantDTORes }) => {

    let history = useHistory();
    const [popupSub, setPopupSub] = useState(false);
    const [popupDel, setPopupDel] = useState(false);
    const [popupEdit, setPopupEdit] = useState(false);

    const messageTitleSubmit: string = "Are you sure that you want to submit this application draft?"
    const messageTitleDelete: string = "Are you sure that you want to delete this application draft?"
    const confirmMessage: string = "Yes";
    const refuseMessage: string = "No";


    const handleClose = () => {
        setPopupSub(false);
        setPopupDel(false);
        setPopupEdit(false);
    };


    const submitApp = () => {
        setPopupSub(false)

        let application = convertAppDTOResToReq(details, api.ApplicationDTOReqStatusEnum.Submitted)
      
        new api.ApplicationControllerApi().updateApplicationUsingPUT(application, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {
          
            history.push('' + application.applicationID)
            window.location.reload();
        }).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 

    }


    const deleteDraft = () => {
        if (!popupDel)
            setPopupDel(true)
        else {
            new api.ApplicationControllerApi().deleteApplicationUsingDELETE(details.applicationID, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {
              
                window.location.replace("/home")
            }).catch(
                (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 

            setPopupDel(false)
        }
    }



    return (
        <div className="draft__buttons">
            <Button variant="primary" onClick={() => setPopupSub(true)}  >Submit</Button>
            <Button variant="secondary" onClick={() => setPopupEdit(true)}  >Edit</Button>
            <Button variant="danger" onClick={deleteDraft}>Delete Draft</Button>


            <ConfirmationDialog popupSub={popupSub}
                handleClose={handleClose}
                messageTitle={messageTitleSubmit}
                submitHandler={submitApp}
                confirmMessage={confirmMessage}
                refuseMessage={refuseMessage} />

            <ConfirmationDialog popupSub={popupDel}
                handleClose={handleClose}
                messageTitle={messageTitleDelete}
                submitHandler={deleteDraft}
                confirmMessage={confirmMessage}
                refuseMessage={refuseMessage} />



            <Dialog open={popupEdit} onClose={handleClose} aria-labelledby="alert-modal-title" aria-describedby="simple-modal-description">

                <div className="modal_group">

                    <h4 className="form__title"> Fill your application form</h4>

                    <ApplicationForm answersNumb={details.responses.length}
                        questions={grantInfo.applicationQuestions}
                        grantId={grantInfo.grantId}
                        draft={
                            {
                                isDraft: true,
                                draft: convertAppDTOResToReq(details, api.ApplicationDTOReqStatusEnum.Draft)
                            } as Draft
                        }
                    />

                </div>
            </Dialog>


        </div>
    )


}

const FinalEvalButtons = ({ details }: { details: ApplicationDTORes }) => {
    let history = useHistory();

    const [popupSub, setPopupSub] = useState(false);
    const [popupDel, setPopupDel] = useState(false);

    const messageTitleApprove: string = "Are you sure that you want to approve this application?"
    const messageTitleRefuse: string = "Are you sure that you want to refuse this application?"
    const confirmMessage: string = "Yes";
    const refuseMessage: string = "No";

    const handleClose = () => {
        setPopupSub(false)
        setPopupDel(false)
    }

    const acceptHandler = () => {

        new api.ApplicationControllerApi().updateApplicationUsingPUT(convertAppDTOResToReq(details, api.ApplicationDTOReqStatusEnum.Granted), { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {
            history.push(String(details.applicationID))
            window.location.reload();
        }).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }

    const refuseHandler = () => {
        new api.ApplicationControllerApi().updateApplicationUsingPUT(convertAppDTOResToReq(details, api.ApplicationDTOReqStatusEnum.NotGranted), { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {
            history.push(String(details.applicationID))
            window.location.reload();
        }).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }




    return (
        <div className="finalClass__container">
            <label>Final evaluation:</label>
            <div className="finalClass__buttons">
                <Button variant="primary" onClick={() => setPopupSub(true)}  >Approve</Button>
                <Button variant="danger" onClick={() => setPopupDel(true)}>Refuse</Button>

                <ConfirmationDialog popupSub={popupSub}
                    handleClose={handleClose}
                    messageTitle={messageTitleApprove}
                    submitHandler={acceptHandler}
                    confirmMessage={confirmMessage}
                    refuseMessage={refuseMessage} />

                <ConfirmationDialog popupSub={popupDel}
                    handleClose={handleClose}
                    messageTitle={messageTitleRefuse}
                    submitHandler={refuseHandler}
                    confirmMessage={confirmMessage}
                    refuseMessage={refuseMessage} />

            </div>
        </div>
    )
}


function ApplicationInfo(props: { userId: number, userType: string, username: string }) {

    let fromRouting: boolean = false;

    let history = useHistory();

    let preSelectedApplication: ApplicationDTORes = {} as ApplicationDTORes;

    let convertEnumType = (status: api.ApplicationDTOStatusEnum) => {
        if (status === 'DRAFT') {
            return api.ApplicationDTOResStatusEnum.Draft
        }

        else if (status === 'SUBMITTED') {
            return api.ApplicationDTOResStatusEnum.Submitted
        }

        else if (status === 'GRANTED') {
            return api.ApplicationDTOResStatusEnum.Granted
        }

        else {
            return api.ApplicationDTOResStatusEnum.NotGranted
        }
    }

    //See if we have the application object in our routing history to avoid making one more request to the api
    if (history.location.state !== undefined) {

        let loadedAppFromRouting: any[] = history.location.state as any
        let appFromRouteDTO: ApplicationDTO = (loadedAppFromRouting[0] as ApplicationEntry).app as ApplicationDTO

        //Type conversion from ApplicationDTO to ApplicationDTORes (simple casting doesnt work because of different enum types)
        preSelectedApplication = {
            applicationID: appFromRouteDTO.applicationID,
            grantID: appFromRouteDTO.grantID,
            responses: appFromRouteDTO.responses,
            reviews: appFromRouteDTO.reviews,
            status: convertEnumType(appFromRouteDTO.status),
            studentID: appFromRouteDTO.studentID,
            submissionDate: appFromRouteDTO.submissionDate,
        }
        fromRouting = true
    }

    const [appInfo, setappInfo] = useState(preSelectedApplication as ApplicationDTORes);
    const [grantInfo, setgrantInfo] = useState({} as GrantDTORes);
    const [student, setStudent] = useState({} as SafeStudentDTO)
    const [revInfo, setrevInfo] = useState(undefined as unknown as SafeReviewerDTO[])
    const [instName, setinstName] = useState('' as string);
    const [appReviews, setappReviews] = useState(undefined as unknown as EvaluationDTORes[]);
    const [grantPanel, setgrantPanel] = useState(undefined as unknown as PanelDTO);


    let appId: number = +window.location.pathname.split('/')[2]  //Gets the id from the Url


    //obter a application do grant
    const loadApplication = () => {
        new api.ApplicationControllerApi().getApplicationUsingGET(appId, { headers: { 'Authorization': localStorage["jwt"] } }).then(
            res => res && (setappInfo(res.data), loadGrantDetails(res.data.grantID), loadStudentDetails(res.data.studentID))).catch(
               (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }

    //get corresponding grant details
    const loadGrantDetails = (grantID: number) => {
        new api.GrantControllerApi().getGrantUsingGET(grantID, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (setgrantInfo(res.data))).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }

    //get student details
    const loadStudentDetails = (studID: number) => {
        new api.StudentControllerApi().getStudentUsingGET(studID, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (setStudent(res.data), getInstitutionName(res.data.institutionId))).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }

    //get reviewer details
    const LoadReviwerDetails = (username: string) => {
        new api.ReviewerControllerApi().getReviewersUsingGET(undefined, undefined, username, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (setrevInfo(res.data))).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }

    const getInstitutionName = (instId: number) => {
        new api.InstitutionControllerApi().getInstitutionUsingGET((instId), { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (setinstName(res.data.name))).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }

    const getApplicationEvaluation = (appId: number) => {
        new api.ApplicationControllerApi().getApplicationEvaluationsUsingGET(appId, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (setappReviews(res.data))).catch(
            (error) => {
                if (('' + error).includes('404')) { //if 404 then no evaluations exist then change from undefined to empty array
                    setappReviews([])
                }
                else{
                   history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}])
                }
            })
    }

    const getGrantPanel = (grantId: number) => {
        new api.GrantControllerApi().getPanelUsingGET(grantId, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (setgrantPanel(res.data))).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }


    useEffect(() => {
        if (!fromRouting) {
            if (appInfo.applicationID === undefined || grantInfo.grantId === undefined || student.id === undefined)
                loadApplication()

            getNeededReviewerInfo()
            getNeededStudentInfo()
        }
        else {
            loadGrantDetails(preSelectedApplication.grantID)
            loadStudentDetails(preSelectedApplication.studentID)
            getNeededReviewerInfo()
            getNeededStudentInfo()


        }


    }, [appInfo])

    const getNeededReviewerInfo = () => {

        if (props.userType.includes("ROLE_REVIEWER")) {
            if (revInfo === undefined) {
                LoadReviwerDetails(props.username)
            }

            if (appReviews === undefined) {
                getApplicationEvaluation(appId)
            }

            if (grantPanel === undefined && appInfo.grantID !== undefined) {
                getGrantPanel(appInfo.grantID)
            }
        }
    }

    const getNeededStudentInfo = () => {
        if (props.userType.includes("ROLE_STUDENT") &&
            appReviews === undefined &&
            appInfo.status !== undefined &&
            (appInfo.status === api.ApplicationDTOResStatusEnum.Granted || appInfo.status === api.ApplicationDTOResStatusEnum.NotGranted)) {

            getApplicationEvaluation(appId)
        }
    }

    const userBelongsToPanel = () => {
        if (!props.userType.includes("ROLE_REVIEWER")) {
            return false;
        }

        let belongs: boolean = false;
        grantPanel.reviewer.some(
            (rev: IdAndName) => {
                if (rev.id === props.userId) {
                    belongs = true;
                }
            }
        )

        return belongs;
    }

    const isPanelChairman = (): boolean => {
        if (props.userType.includes("ROLE_REVIEWER") && grantPanel.panelChair === props.userId) {
            return true
        }
        else {
            return false
        }

    }

    const hasReviewedApp = (): boolean => {


        let result = revInfo[0].evals.filter((revId: number) => appReviews.some((rev: EvaluationDTORes) => revId === rev.id));

        return result.length > 0

    }

    const canWriteReview = (): boolean => {

        if (appInfo !== undefined && props.userType.includes("ROLE_REVIEWER") && revInfo !== undefined && appReviews !== undefined) {
            return appInfo.status === api.ApplicationDTOResStatusEnum.Submitted && grantPanel !== undefined && userBelongsToPanel() && !hasReviewedApp()
        }
        else {
            return false
        }

    }

    const canSeeReviews = (): boolean => {

        if (props.userType.includes("ROLE_STUDENT")) {
            return ((appInfo !== undefined) && (appInfo.status === api.ApplicationDTOResStatusEnum.Granted || appInfo.status === api.ApplicationDTOResStatusEnum.NotGranted))
        }

        else if (props.userType.includes("ROLE_REVIEWER")) {
            return ((appInfo !== undefined) && grantPanel !== undefined && userBelongsToPanel())
        }

        else return false;

    }

    const canPerformFinalEval = (): boolean => {
        return isPanelChairman() &&
            appReviews !== undefined &&
            appInfo !== undefined &&
            appInfo.status === api.ApplicationDTOResStatusEnum.Submitted &&
            (appReviews.length >= 4) //only 4 members per panel in our app (who can't perform an eval (same institution as student will have a default empty rev))
    }


    return (
        <div className="home__screen">
            <Navbar />
            <div className="home__container">

                <div className="ContentTab">
                    <Tabs defaultActiveKey="appInfo" id="uncontrolled-tab-example">
                        <Tab eventKey="appInfo" title="Application Info">
                            <div className="appInfo__container">
                                <div className="left__container">


                                    {((appInfo.responses !== undefined) && (grantInfo !== undefined) && (student !== undefined) && (instName !== undefined)) &&
                                        <ApplicationDetails details={appInfo} grantInfo={grantInfo} student={student} instName={instName} />}

                                    {grantPanel !== undefined && canPerformFinalEval() && appInfo !== undefined && <FinalEvalButtons details={appInfo} />}

                                </div>
                                {appInfo.responses !== undefined && <ApplicationQuestions res={appInfo.responses} />}


                            </div>
                        </Tab>
                        {appReviews !== undefined && canSeeReviews() &&
                            <Tab eventKey="panelRevs" title="Panel Reviews">
                                <PanelReviews userId={props.userId} userType={props.userType} details={appInfo} reviews={appReviews} panel={grantPanel} />
                            </Tab>}

                        {revInfo !== undefined && canWriteReview() &&
                            <Tab eventKey="newRev" title="New Review">
                                <NewReview details={appInfo} revInfo={revInfo[0]} userId={props.userId} userType={props.userType} studInst={student.institutionId} />
                            </Tab>}

                    </Tabs>
                </div>
            </div>
            <div className="footer">
                <Footer></Footer>
            </div>
        </div>

    );
}

export default ApplicationInfo;

