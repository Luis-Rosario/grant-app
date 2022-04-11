import React, { ChangeEvent, SyntheticEvent, useState } from 'react'
import { Button, Form } from 'react-bootstrap'
import './NewReview.css'
import *  as api from '../../typescript-axios-client/api'
import { ApplicationDTORes, EvaluationDTOReq, SafeReviewerDTO } from '../../typescript-axios-client/api';
import { ConfirmationDialog } from './ApplicationInfo';
import { useHistory } from 'react-router-dom';




function NewReview(props: { userId: number, userType: string, details: ApplicationDTORes, revInfo: SafeReviewerDTO, studInst: number }) {

    const [review, setReview] = useState({} as string);
    const [decision, setDecision] = useState({} as boolean);
    const [popup, setPopup] = useState(false);
    
    let history = useHistory();
    let reviewChangeHandler = (e: ChangeEvent<HTMLInputElement>) => { setReview(e.target.value); }
    let popupMessage:string = "Are you sure that you want to submit this review?"
    


    const handleClose = () => {
        setPopup(false);
    };

    const submitReview = () => {
       
        setPopup(false)
        //submit review and decision
       

        let evaluation: EvaluationDTOReq = {
            id: 0,
            status: decision,
            revId: props.userId,
            applicationId: props.details.applicationID,
            grantId: props.details.grantID,
            textField: review,
        }
       
        
        new api.EvaluationControllerApi().createEvaluationUsingPOST(evaluation, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {
            history.push('' + evaluation.applicationId)
            window.location.reload();
        }).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 

    }

    let submitHandler = (decision: Boolean, event: SyntheticEvent) => {

        if (decision)
            setDecision(true)

        else
            setDecision(false)


        setPopup(true)
        event.preventDefault();
  
    }

    return (
        <div className="review__containter">
            <div className="review__text">
                <Form>
                    <Form.Group controlId="review">
                        <Form.Label className="review__label">Evaluation</Form.Label>
                        {
                            (props.revInfo.institutionId === props.studInst)
                                ? (<div>
                                    <Form.Control disabled as="textarea" rows={13} onChange={reviewChangeHandler} />
                                    <span className="error__message">Cannot review users from the same institution</span>
                                </div>) :
                                (<Form.Control required as="textarea" rows={13} onChange={reviewChangeHandler} />)}
                    </Form.Group>
                </Form>
            </div>
            <div className="button__group">
                {
                    (props.revInfo.institutionId !== props.studInst) ?
                        (<>
                            <Button variant="primary" data-txt="accept" type="submit" onClick={(e) => submitHandler(true, e)}>Approved</Button>
                            <Button variant="danger" data-txt="refuse" type="submit" onClick={(e) => submitHandler(false, e)}>Not Approved</Button>
                        </>) :
                        (
                            <>
                                <Button disabled variant="primary" data-txt="accept" type="submit">Approved</Button>
                                <Button disabled variant="danger" data-txt="refuse" type="submit" >Not Approved</Button>
                            </>
                        )
                }

                 <ConfirmationDialog popupSub={popup}
                handleClose={handleClose}
                messageTitle={popupMessage}
                submitHandler={submitReview}
                confirmMessage={'Yes'}
                refuseMessage={'No'} />


        
            </div>
        </div>
    )
}

export default NewReview;