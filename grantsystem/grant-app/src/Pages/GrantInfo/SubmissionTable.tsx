import React from 'react'
import { Table } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import { ApplicationDTORes } from '../../typescript-axios-client';

import './SubmissionTable.css'


const SubmissionComponent = ({sub, userType}:{sub:ApplicationDTORes, userType: string}) => {
    let history = useHistory();
    return (
        <tr onClick={ (e: React.MouseEvent<HTMLTableRowElement, MouseEvent>) =>{ if(!userType.includes("ANONYMOUS")) {history.push("/appinfo/" + sub.applicationID)}} }>
            <td >
                {sub.studentID}
            </td >
            <td  >
                {sub.submissionDate}
            </td>
            <td  >
                {sub.status}
            </td>

        </tr>
    )
}

const ApplicationTable = ({apps, userType}:{apps:ApplicationDTORes[], userType: string}) => {

    return (

        <div className="table__wrapper">
            <Table striped bordered hover  >
                <thead>
                    <tr>
                        <th >Student</th>
                        <th >Submission Date</th>
                        <th >Status</th>

                    </tr>
                </thead>
                <tbody>
                    {apps.map(
                        (submission:ApplicationDTORes) => <SubmissionComponent sub={submission} userType={userType}/>
                    )}
                </tbody>

            </Table>
        </div>
    )
}

function SubmissionTable(props:any) {
    
    return (
        <div className="content__container">
            <div className="grant__title">
                <h3>{props.title}</h3>
            </div>

            <ApplicationTable apps={props.applications} userType={props.userType} />
        </div>

    )

}

export default SubmissionTable;

