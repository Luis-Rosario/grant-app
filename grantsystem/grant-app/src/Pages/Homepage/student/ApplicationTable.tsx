import React, { useEffect } from 'react'
import Table from 'react-bootstrap/Table'
import { useState } from 'react';
import { GrantDTORes, ApplicationDTO } from '../../../typescript-axios-client';
import *  as api from '../../../typescript-axios-client/api'
import './ApplicationTable.css'
import { useHistory } from 'react-router-dom';

export type ApplicationEntry = {
    app: ApplicationDTO;
    grant: GrantDTORes
}



const ApplicationComponent = ({ app }: { app: ApplicationEntry }) => {

    let history = useHistory();

    return (
        <tr  onClick={(e: React.MouseEvent<HTMLTableRowElement, MouseEvent>) => { history.push(("/appinfo/" + app.app.applicationID), [app])}} >
            <td>
                {app.app.applicationID}
            </td>
            <td>
                {app.grant.title}
            </td>
            <td>
                {app.grant.sponsorName}
            </td>
            <td>
                {app.grant.funding}
            </td>
            <td>
                {app.grant.deadline}
            </td>
            <td>
                {app.app.status !== 'DRAFT' && app.app.submissionDate}
                {app.app.status === 'DRAFT' && <span></span>}
            </td>
            <td>
                {app.app.status}
            </td>
        </tr>
    )
}

const ApplicationTabl = ({ apps }: { apps: ApplicationEntry[] }) => {

    let initSortOrder: boolean[] = [true, true, true, true, true, true, true]
    
    const [sortArr, setSortArr] = useState(initSortOrder as boolean[])
    const [sort, setSort] = useState(false) 
  
     const sortCol = (index: number) => {
        let newSortOrder: boolean[] = sortArr
      
 
        if (sortArr[index]) {

            switch (index) {
                case 0: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.app.applicationID > b.app.applicationID) ? 1 : -1))
                    break;
                }
                case 1: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.grant.title > b.grant.title) ? 1 : -1))
                    break;
                }
                case 2: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.app.submissionDate > b.app.submissionDate)  ? 1 : -1))
                    break;
                }
                case 3: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.app.status > b.app.status) ? 1 : -1))
                    break;
                }
                case 4: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.grant.sponsorName > b.grant.sponsorName) ? 1 : -1))
                    break;
                }
                case 5: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.grant.funding > b.grant.funding) ? 1 : -1))
                    break;
                }
                case 6: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.grant.deadline > b.grant.deadline) ? 1 : -1))
                    break;
                }

            }

            newSortOrder[index] = false;
            setSortArr(newSortOrder)
            setSort(true)

        }
        else {

            switch (index) {
                case 0: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.app.applicationID > b.app.applicationID) ? -1 : 1))
                    break;
                }
                case 1: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.grant.title > b.grant.title) ? -1 : 1 ))
                    break;
                }
                case 2: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.app.submissionDate > b.app.submissionDate)  ? -1 : 1))
                    break;
                }
                case 3: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.app.status > b.app.status) ? -1 : 1))
                    break;
                }
                case 4: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.grant.sponsorName > b.grant.sponsorName) ? -1 : 1))
                    break;
                }
                case 5: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.grant.funding > b.grant.funding) ? -1 : 1))
                    break;
                }
                case 6: {
                    apps.sort(((a: ApplicationEntry, b: ApplicationEntry) => (a.grant.deadline > b.grant.deadline) ? -1 : 1))
                    break;
                }
            }

            newSortOrder[index] = true;
           setSortArr(newSortOrder)
           setSort(false)
        }

    }
    

    return (
        <Table striped bordered hover  >
            <thead>
                <tr>
                    <th onClick={() => sortCol(0)}>id</th>
                    <th onClick={() => sortCol(1)}>Grant Title</th>
                    <th onClick={() => sortCol(4)}>Sponsor</th>
                    <th onClick={() => sortCol(5)}>Funding</th>
                    <th onClick={() => sortCol(6)}>Deadline</th>
                    <th onClick={() => sortCol(2)}>Submission Date</th>
                    <th onClick={() => sortCol(3)}>Status</th>
                </tr>
            </thead>
            <tbody>
                {apps.map(
                    (app: ApplicationEntry) => <ApplicationComponent app={app} />
                )}
            </tbody>

        </Table>
    )
}


function ApplicationTable(props: {appList: ApplicationDTO[]}) {

    let history = useHistory();
    const [appsEntry, setappsEntry] = useState([] as ApplicationEntry[])

    let tableEntries: ApplicationEntry[] = []
    let appNumb: number = (props.appList as ApplicationDTO[]).length


    const addGrantToAppList = (grant: GrantDTORes, app: ApplicationDTO) => {
        tableEntries.push({ app: app, grant: grant } as ApplicationEntry);

        if (tableEntries.length === appNumb) {
            setappsEntry(tableEntries)
        }
    }

    const appList = () => {
        tableEntries = []

        for (let i = 0; i < appNumb; i++) {
            new api.GrantControllerApi().getGrantUsingGET(props.appList[i].grantID, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {

                addGrantToAppList(res.data, props.appList[i],)

            }).catch(
                (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 

        }
    }

    useEffect(() => {
        if (props.appList !== undefined)
            appList()
    }, [props]);

  
    return (
        <div className="view__wrapper">
            <div className="table__wrapper">
              <ApplicationTabl apps={appsEntry} />
            </div>
        </div>

    )
}

export default ApplicationTable;


