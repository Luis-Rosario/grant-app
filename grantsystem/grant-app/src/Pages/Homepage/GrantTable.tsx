import React from 'react'
import { useState } from 'react';
import Table from 'react-bootstrap/Table'
import GrantTableFilter from './GrantTableFilter';

import "./GrantTable.css";

import { useEffect } from 'react';
import * as api from "../../typescript-axios-client/api";
import { GrantDTORes, SafeSponsorDTOReq } from "../../typescript-axios-client/api";
import { useHistory } from 'react-router-dom';



type SponsorSelectEntry = {
    value: string,
    label: string,
}



export type GrantFilters = {
    openDate: Date,
    deadline: Date,
    minFund: number,
    maxFund: number,
    sponsor: SponsorSelectEntry,
}



const GrantComponent = ({ grant, key, sponsors }: { grant: GrantDTORes, key: number, sponsors: SafeSponsorDTOReq[] }) => {
    let history = useHistory();

    const isOpen = ():boolean =>{
       
        return  new Date(grant.deadline) >= new Date()
    }

    return (
        <tr className="grantTable__Row" key={key} onClick={(e: React.MouseEvent<HTMLTableRowElement, MouseEvent>) => { history.push(("/grant/" + grant.grantId), [grant, sponsors]) }}>
            <td >
                {grant.grantId}
            </td >
            <td  >
                {grant.title}
            </td>
            <td  >
                {grant.openingDate}
            </td>
            <td >
                {grant.deadline}
            </td>
            <td >
                {grant.funding}
            </td>
            <td >
                {sponsors.map((sponsor: SafeSponsorDTOReq) => {
                    if (grant.sponsorId === (sponsor.sponsorID))
                        return sponsor.name
                })}
            </td>

            <td >
                {grant.applications}
            </td>

            
            <td >
                {isOpen()? 'Open' : 'Closed'}
            </td>


        </tr>
    )
}

const GrantTabl = ({ grantsArr, sponsors }: { grantsArr: GrantDTORes[], sponsors: SafeSponsorDTOReq[] }) => {
    let initSortOrder: boolean[] = [true, true, true, true, true, true, true]

    const [sortArr, setSortArr] = useState(initSortOrder as boolean[])
    const [sort, setSort] = useState(false)

    const sortCol = (index: number) => {
        let newSortOrder: boolean[] = sortArr


        if (sortArr[index]) {

            switch (index) {
                case 0: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.grantId > b.grantId) ? 1 : -1))
                    break;
                }
                case 1: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.title > b.title) ? 1 : -1))
                    break;
                }
                case 2: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.openingDate > b.openingDate) ? 1 : -1))
                    break;
                }
                case 3: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.deadline > b.deadline) ? 1 : -1))
                    break;
                }
                case 4: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.funding > b.funding) ? 1 : -1))
                    break;
                }
                case 5: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.sponsorId > b.sponsorId) ? 1 : -1))
                    break;
                }
                case 6: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.applications > b.applications) ? 1 : -1))
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
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.grantId > b.grantId) ? -1 : 1))

                    break;
                }
                case 1: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.title > b.title) ? -1 : 1))
                    break;
                }
                case 2: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.openingDate > b.openingDate) ? -1 : 1))
                    break;
                }
                case 3: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.deadline > b.deadline) ? -1 : 1))
                    break;
                }
                case 4: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.funding > b.funding) ? -1 : 1))
                    break;
                }
                case 5: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.sponsorId > b.sponsorId) ? -1 : 1))
                    break;
                }
                case 6: {
                    grantsArr.sort(((a: GrantDTORes, b: GrantDTORes) => (a.applications > b.applications) ? -1 : 1))
                    break;
                }
                
            }

            newSortOrder[index] = true;
            setSortArr(newSortOrder)
            setSort(false)
        }

    }


    return (

        <div className="table__wrapper">
            <Table striped bordered hover  >
                <thead>
                    <tr key={-1}>
                        <th onClick={() => sortCol(0)}>Id</th>
                        <th onClick={() => sortCol(1)}>Grant title</th>
                        <th onClick={() => sortCol(2)}>Opening date</th>
                        <th onClick={() => sortCol(3)}>Aplication deadline</th>
                        <th onClick={() => sortCol(4)}>Funding</th>
                        <th onClick={() => sortCol(5)}>Sponsor</th>
                        <th onClick={() => sortCol(6)}>Nº Applications</th>
                        <th onClick={() => sortCol(3)}>Status</th>
                    </tr>
                </thead>
                <tbody>
                    {grantsArr.map(
                        (grant: GrantDTORes, index: number) => <GrantComponent grant={grant} key={index} sponsors={sponsors} />
                    )}
                </tbody>

            </Table>
        </div>
    )
}




function GrantTable(props: { list: GrantDTORes[] }) {



    const [grantList, setGrantList] = useState([] as GrantDTORes[]);
    const [sponsors, setsponsors] = useState([] as SafeSponsorDTOReq[])
    let history = useHistory();

    const sponsorList = () => {
        new api.SponsorControllerApi().getSponsorsUsingGET(undefined, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && setsponsors(res.data)).catch(
            (error) => (history.push("/error/", [{ status: error.response.status, text: error.response.statusText, path: error.response.data.path }])))
    }

    useEffect(() => {
        setGrantList(props.list);
        sponsorList();
    }, [props.list])


    const handleFilterChange = (filters: GrantFilters) => {

        let filteredGrants = props.list;


        if (filters.openDate !== undefined) {

            filteredGrants = filteredGrants.filter(
                function (g: GrantDTORes) {
                    return (new Date(g.openingDate) >= filters.openDate)
                })

        }

        if (filters.deadline !== undefined) {
            filteredGrants = filteredGrants.filter(
                function (g: GrantDTORes) {
                   
                    return (new Date(g.deadline) <= filters.deadline)
                })

        }

        if (!isNaN(filters.minFund)) {

            filteredGrants = filteredGrants.filter(
                function (g: GrantDTORes) {
                    return (g.funding >= filters.minFund)
                })


        }


        if (filters.sponsor !== null) {
            filteredGrants = filteredGrants.filter(
                function (g: GrantDTORes) {
                    return (g.sponsorId === parseInt(filters.sponsor.value));
                });

        }

        setGrantList(filteredGrants)

    }

    return (
        <div className="view__wrapper">
            <GrantTabl grantsArr={grantList} sponsors={sponsors} />
            <GrantTableFilter grants={props.list} sponsors={sponsors} onChangeFilter={handleFilterChange} />
        </div>

    )
}

export default GrantTable;
