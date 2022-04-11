import DatePicker from "react-datepicker";
import Select from "react-select";
import { ChangeEvent } from 'react';
import { Button } from 'react-bootstrap';
import React from 'react'
import { useState } from 'react';
import { GrantDTORes, SafeSponsorDTOReq } from "../../typescript-axios-client/api";
import "./GrantTable.css";


type SponsorSelectEntry = {
    value: string,
    label: string,
}


function GrantTableFilter(props: { grants:GrantDTORes[], sponsors:SafeSponsorDTOReq[], onChangeFilter:(filters: any) => void}) {

    const [filters, setfilters] = useState({ openDate: undefined, deadline: undefined, minFund: NaN, sponsor: null, maxFund: undefined });


    var sponsorList: SponsorSelectEntry[] = [];
    var maxFund: number = 0;

    let getSponsorName = function (id: number): String {
        var toRet = 'null'
        props.sponsors.forEach((element: SafeSponsorDTOReq) => {
       
            if (element.sponsorID === id)
                toRet = element.name
        });
        return toRet;
    }

    let sponsorListContains = function (sponsorList: SponsorSelectEntry[], id: number): Boolean {
        var toRet = false;
        sponsorList.forEach((element: SponsorSelectEntry) => {

            if (+element.value === id)
                toRet = true
        });
        return toRet;
    }

    props.grants.map(
        (grant: GrantDTORes) => {
            if (!sponsorListContains(sponsorList,  grant.sponsorId)) {
                sponsorList.push({ value: grant.sponsorId + '', label: getSponsorName(grant.sponsorId) as string })
            }

            if (grant.funding > maxFund)
                maxFund = grant.funding;
        }
    )

    const options = sponsorList;
    const maxFunding = maxFund;

    let openDateHandler = (openDate: any) => {
        var filter = filters;
        filter.openDate = openDate;
        setfilters(filter);
        props.onChangeFilter(filter)
    }

    let deadlineHandler = (deadline: any) => {
        var filter = filters;
        filter.deadline = deadline;
        setfilters(filter);
        props.onChangeFilter(filter)
    }

    let minFundHandler = (minFund: ChangeEvent<HTMLInputElement>) => {

        var filter = filters;
        filter.minFund = parseInt(minFund.target.value);
        setfilters(filter);
        props.onChangeFilter(filter)
    }

    let selectHandler = (selectedSponsor: any) => {
        var filter = filters;
        filter.sponsor = selectedSponsor;
        setfilters(filter);
        props.onChangeFilter(filter)
    }

    let clearFilters = () => {
        var filter = { openDate: undefined, deadline: undefined, minFund: NaN, sponsor: null, maxFund: undefined }
        setfilters(filter);
        (document.getElementById("minFunding")! as HTMLInputElement).value = "0";

     

        props.onChangeFilter(filter)
    }


    return (
        <div className="filter__container">
            <h3>Filters</h3>
            <div className="filter__content">
                <div className="filter__datePickerGroup">
                    <h6>Grants open from:</h6>
                    <DatePicker
                        selected={filters.openDate}
                        onChange={(date: any) => openDateHandler(date)}
                        selectsStart
                        dateFormat="dd/MM/yyyy"
                        startDate={filters.openDate}
                        endDate={filters.deadline}
                    />
                    <h6>Until deadline:</h6>
                    <DatePicker
                        selected={filters.deadline}
                        onChange={(date: any) => deadlineHandler(date)}
                        selectsEnd
                        dateFormat="dd/MM/yyyy"
                        startDate={filters.openDate}
                        endDate={filters.deadline}
                        minDate={filters.openDate}
                    />
                </div>

                <div className="filter__fundingGroup">
                    <h6>Minimum funding : {!isNaN(filters.minFund) && filters.minFund} {isNaN(filters.minFund) && 0}€</h6>
                    <div className="slider__fundingContainer">
                        <input type="range" min="0" max={maxFunding} className="slider__funding" id="minFunding" onChange={minFundHandler}></input>
                    </div>

                    <div className="range_labels">
                        <label className="left__rangeLabel">0€</label>
                        <label className="right__rangeLabel">{maxFunding}€</label>
                    </div>

                </div>

                <div className="filter__sponsorGroup">
                    <Select className="select__sponsor" id="sponsorSelect" options={options} onChange={selectHandler} isClearable={true} />
                </div>

                <div className="filter__clearButton">
                    <Button variant="primary" onClick={clearFilters} >Clear Filters</Button>
                </div>

            </div>
        </div>
    )
}

export default GrantTableFilter