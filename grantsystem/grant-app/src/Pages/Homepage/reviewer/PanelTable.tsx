import React, { useEffect } from 'react'
import Table from 'react-bootstrap/Table'
import { useState } from 'react';

import './PanelTable.css'
import { GrantDTORes, PanelDTORes } from '../../../typescript-axios-client';
import *  as api from '../../../typescript-axios-client/api'
import { useHistory } from 'react-router-dom';


export interface PanelTableEntries {
    panel: PanelDTORes,
    grant: GrantDTORes,
}

const PanelComponent = ({ panel }: { panel: PanelTableEntries }) => {
    let history = useHistory();
    return (
        <tr onClick={(e: React.MouseEvent<HTMLTableRowElement, MouseEvent>) => { history.push(("/panels/" + panel.panel.panelID), [panel]) }} >
            {panel !== undefined &&
                <>
                    <td>
                        {panel.panel.panelID}
                    </td>
                    <td>
                        {panel.grant.title}
                    </td>
                    <td>
                        {panel.grant.applications}
                    </td>
                    <td>
                        {panel.grant.openingDate}
                    </td>
                    <td>
                        {panel.grant.deadline}
                    </td>
                    <td>
                        {panel.panel.panelChair === (+window.localStorage['id']) && "Yes"}
                        {!(panel.panel.panelChair === (+window.localStorage['id'])) && "No"}
                    </td>
                </>
            }

        </tr>
    )
}

const PanelTabl = ({ entries }: { entries: PanelTableEntries[] }) => {

    let initSortOrder: boolean[] = [true, true, true, true, true, true, true]

    const [sortArr, setSortArr] = useState(initSortOrder as boolean[])
    const [sort, setSort] = useState(false)

    const sortCol = (index: number) => {
        let newSortOrder: boolean[] = sortArr


        if (sortArr[index]) {

            switch (index) {
                case 0: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.panel.panelID > b.panel.panelID) ? 1 : -1))
                    break;
                }
                case 1: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.grant.title > b.grant.title) ? 1 : -1))
                    break;
                }
                case 2: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.grant.openingDate > b.grant.openingDate) ? 1 : -1))
                    break;
                }
                case 3: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.grant.applications > b.grant.applications) ? 1 : -1))
                    break;
                }
                case 4: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.grant.deadline > b.grant.deadline) ? 1 : -1))
                    break;
                }
                case 5: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.panel.panelChair > b.panel.panelChair) ? 1 : -1))
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
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.panel.panelID > b.panel.panelID) ? -1 : 1))
                    break;
                }
                case 1: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.grant.title > b.grant.title) ? -1 : 1))
                    break;
                }
                case 2: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.grant.openingDate > b.grant.openingDate) ? -1 : 1))
                    break;
                }
                case 3: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.grant.applications > b.grant.applications) ? -1 : 1))
                    break;
                }
                case 4: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.grant.deadline > b.grant.deadline) ? -1 : 1))
                    break;
                }
                case 5: {
                    entries.sort(((a: PanelTableEntries, b: PanelTableEntries) => (a.panel.panelChair > b.panel.panelChair) ? -1 : 1))
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
                    <th onClick={() => sortCol(3)}>Applications submited</th>
                    <th onClick={() => sortCol(2)}>Creation Date</th>
                    <th onClick={() => sortCol(4)}>Deadline</th>
                    <th onClick={() => sortCol(5)}>Panel chair</th>
                </tr>
            </thead>
            <tbody>
                {entries.map(
                    (panel: PanelTableEntries, index: number) => <PanelComponent panel={panel} />
                )}
            </tbody>

        </Table >
    )
}


function PanelTable(props: { panelList: PanelDTORes[] }) {

    const [entries, setentries] = useState([] as PanelTableEntries[]);

    let history = useHistory();
    let tableEntries: PanelTableEntries[] = []
    let panNumb: number = (props.panelList as PanelDTORes[]).length

    const addGrantToGrantList = (grant: GrantDTORes, panel: PanelDTORes) => {
        tableEntries.push({ panel: panel, grant: grant } as PanelTableEntries);

        if (tableEntries.length === panNumb) {
            setentries(tableEntries)
        }
    }

    const panelList = () => {
        tableEntries = []

        for (let i = 0; i < panNumb; i++) {
            new api.GrantControllerApi().getGrantUsingGET(props.panelList[i].grant, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {

                addGrantToGrantList(res.data, props.panelList[i],)

            }).catch(
                (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))

        }
    }

    useEffect(() => {
        panelList()
    }, [props]);
 
    return (
        <div className="view__wrapper">
            <div className="table__wrapper">
                <PanelTabl entries={entries} />
            </div>
        </div>

    )
}

export default PanelTable;


