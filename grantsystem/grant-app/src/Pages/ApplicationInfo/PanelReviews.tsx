import React from 'react';


import { Table } from 'react-bootstrap';

import './PanelReviews.css';
import { useState } from 'react';
import { ApplicationDTORes, EvaluationDTORes, PanelDTO, IdAndName } from '../../typescript-axios-client';






const ReviewComponent = ({ review }: { review: EvaluationDTORes }) => {


    if (review !== undefined) {
        return (
            <div className="evaluation__container">
                { !review.textField.includes("(same institution)") && <h4 className='evaluation__result'>Evaluation: {review.status && 'Approved'} {!review.status && 'Denied'}</h4>}
                { review.textField.includes("(same institution)") && <h4 className='evaluation__result'>No Evaluation</h4>}
                <div className='review__text'> <h6>{review.textField}</h6></div>

            </div>
        )
    }
    else
        return <div className="evaluation__container__empty">
            <div>
                Please select a panel member to see his review
            </div>
        </div>
}

const MemberComponent = ({ revs, onChangeMember, userType, panel, selected }: { revs: EvaluationDTORes[], onChangeMember: (rev: EvaluationDTORes) => void, userType: string, panel: PanelDTO, selected: string }) => {

    let handleSelectPanel = (rev: EvaluationDTORes) => {
        onChangeMember(rev)
    }

    let getPanelmemberName = (id: number): string => {
        let member: IdAndName[] = [];

        if (panel !== undefined) {
            member = panel.reviewer.filter((member: IdAndName) => member.id === id)

            if (member.length > 0) {
                return member[0].name
            }
        }

        return 'Member'

    }


    return (
        <div className="panelMembers__wrapper">

            <Table bordered hover >
                <thead>
                    <tr>

                        {userType.includes("ROLE_STUDENT") && <th>Review Number</th>}
                        {!userType.includes("ROLE_STUDENT") && <th>Panel Member</th>}

                    </tr>
                </thead>
                {revs.length > 0 &&
                    <tbody className="table__body">
                        {userType.includes("ROLE_STUDENT") && revs.map(
                            (rev: EvaluationDTORes, index: number) =>
                                (selected !== undefined && (selected === String(rev.revId))) ?
                                    (<tr className="panelMember_item active" key={index} onClick={() => handleSelectPanel(rev)}><td className="active">Review {rev.id}</td></tr>)
                                    : (<tr className="panelMember_item" key={index} onClick={() => handleSelectPanel(rev)}><td>Review {rev.id}</td></tr>)
                        )}

                        {!userType.includes("ROLE_STUDENT") && revs.map(
                            (rev: EvaluationDTORes, index: number) =>
                                (selected !== undefined && (selected === String(rev.revId))) ?
                                    (<tr className="panelMember_item active" key={index} onClick={() => handleSelectPanel(rev)}><td className="active">{getPanelmemberName(rev.revId)}</td></tr>)
                                    : (<tr className="panelMember_item" key={index} onClick={() => handleSelectPanel(rev)}><td>{getPanelmemberName(rev.revId)}</td></tr>)
                        )}
                    </tbody>}
                {revs.length == 0 &&
                    <tbody className="table__body">
                        <div>No panel member has performed a review so far</div>
                    </tbody>
                }

            </Table>
        </div>

    )




}

function PanelReviews(props: { userId: number, userType: string, details: ApplicationDTORes, reviews: EvaluationDTORes[], panel: PanelDTO }) {


    const [selectedMember, setselectedMember] = useState(undefined as unknown as string);  //user para mudar fundo da div selected
    const [selectedEval, setselectedEval] = useState(undefined as unknown as EvaluationDTORes);

    let selectReviewHandler = (rev: EvaluationDTORes) => {

        setselectedMember(String(rev.revId))

        props.reviews.forEach((review: EvaluationDTORes) => {
            if (review.revId === rev.revId) {
                setselectedEval(review)
            }
        });


    }

 
    return (
        <div className="panelReviews__containter">

            <div>
                <MemberComponent revs={props.reviews} userType={props.userType} onChangeMember={selectReviewHandler} panel={props.panel} selected={selectedMember} />
            </div>
            <div>
                <ReviewComponent review={selectedEval} />

            </div>

        </div>
    )
}

export default PanelReviews;



