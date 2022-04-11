import React from 'react';
import Navbar from "../../Navbar";
import "./Homepage.css";
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import GrantTable from './GrantTable';
import ApplicationTable from './student/ApplicationTable';
import PanelTable from './reviewer/PanelTable';
import Footer from '../../Footer';

import { useState } from 'react';
import *  as api from '../../typescript-axios-client/api'
import { GrantDTORes, PanelDTORes, ApplicationDTO } from '../../typescript-axios-client/api';
import { useEffect } from 'react';
import { useHistory } from 'react-router-dom';



function Homepage(props: { userId: number, userType: string }) {


    const [grants, setGrants] = useState([] as GrantDTORes[]);
    const [apps, setapps] = useState([] as ApplicationDTO[]);
    const [panels, setpanels] = useState([] as PanelDTORes[]);

    let history = useHistory();

    const grantList = () => {
        new api.GrantControllerApi().getGrantsUsingGET(undefined, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && setGrants(res.data)).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }

    const studentApplicationList = () => {
        new api.StudentControllerApi().getStudentApplicationsUsingGET(props.userId, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && setapps(res.data)).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }


    const reviewerPanelList = () => {
        new api.ReviewerControllerApi().getReviewerPanelsUsingGET(props.userId, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && setpanels(res.data)).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 

    }

    useEffect(() => {
        grantList();

        if (props.userId !== undefined) {
            if (props.userType.includes('ROLE_STUDENT')) {
                studentApplicationList();
            }
            else if (props.userType.includes('ROLE_REVIEWER')) {
                reviewerPanelList();
            }
        }
    }, [])



    return (
        <div className="home__screen">
            <Navbar />

            <div className="home__container">

                <div className="ContentTab">
                    <Tabs defaultActiveKey="grants" id="uncontrolled-tab-example">
                        <Tab eventKey="grants" title="All Grants">
                            <div className="home__content">
                                <GrantTable list={grants}></GrantTable>
                            </div>
                        </Tab>

                        {props.userType.includes('ROLE_STUDENT') && <Tab eventKey="useropt" title="My Applications">
                            <div className="home__content">
                                <ApplicationTable appList={apps} />
                            </div>
                        </Tab>
                        }

                        {props.userType.includes('ROLE_SPONSOR') && <Tab eventKey="useropt" title="My Grants">
                            <h2>Not implemented</h2>
                        </Tab>
                        }

                        {props.userType.includes('ROLE_REVIEWER') && <Tab eventKey="useropt" title="My Panels">
                            <PanelTable panelList={panels} />
                        </Tab>
                        }

                    </Tabs>
                </div>
            </div>
            <div className="footer">
                <Footer />
            </div>

        </div>

    );
}

export default Homepage;

