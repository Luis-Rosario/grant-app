import React, { useEffect, useState } from 'react'
import { Table } from 'react-bootstrap';
import *  as api from '../../typescript-axios-client/api'
import Navbar from '../../Navbar';
import { PanelDTORes, GrantDTORes, IdAndNameRes, ApplicationSafeDTO } from '../../typescript-axios-client/api';
import './MyPanels.css'
import { PanelTableEntries } from "../Homepage/reviewer/PanelTable";
import { useHistory } from 'react-router-dom';
import Footer from '../../Footer';





const MyPanelList = ({ panels, onSelectPanel, selected }: { panels: PanelTableEntries[], onSelectPanel: (panel: PanelTableEntries) => void, selected: PanelTableEntries }) => {


    const handleSelectPanel = (panel: PanelTableEntries, event: React.MouseEvent<HTMLElement>) => {
        onSelectPanel(panel)
    }

  
    return (
        <div className="panel__list">

            <Table bordered hover >
                <thead>
                    <tr>
                        <th>My Panels</th>
                    </tr>
                </thead>
                <tbody>
                    {panels.map(
                        (panel: PanelTableEntries, index: number) => 
                        (selected !== undefined && (selected.panel.panelID === panel.panel.panelID)) ?
                        (<tr className="table__row active" key={index} onClick={(event) => handleSelectPanel(panel, event)}><td  className="active">{panel.grant.title}</td></tr>)
                       : (<tr className="table__row" key={index} onClick={(event) => handleSelectPanel(panel, event)}><td>{panel.grant.title}</td></tr>)

                    )}
                </tbody>

            </Table>
        </div>

    )
}


const PanelMemberList = ({ reviewers }: { reviewers: PanelTableEntries }) => {


    return (
        <div className="member__list">

            <Table striped bordered >
                <thead>
                    <tr>
                        <th>Members</th>
                    </tr>
                </thead>
                <tbody>
                    {reviewers !== undefined && reviewers.panel.reviewer.map(
                        (rev: IdAndNameRes, index: number) => <tr key={index}><td>{rev.name}</td></tr>
                    )}
                </tbody>

            </Table>
        </div>
    )
}


const ApplicationsToReview = ({ apps }: { apps: ApplicationSafeDTO[] }) => {
    let history = useHistory();
   
    return (
        <div className="application__list">

            <Table striped bordered  >
                <thead>
                    <tr>
                        <th>Application Id</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    {apps !== undefined && apps.map(

                        (app: ApplicationSafeDTO, index: number) => <tr className="table__row" key={index}
                            onClick={(e: React.MouseEvent<HTMLTableRowElement, MouseEvent>) => { history.push(("/appinfo/" + app.applicationID)) }}
                        ><td>{app.applicationID}</td><td>{app.status}</td></tr>
                    )}
                </tbody>

            </Table>
        </div>
    )
}




function MyPanels(props: { userId: number, userType: string }) {

    let history = useHistory();
    let fromRouting = false;
    let preSelectedPanel: PanelTableEntries = undefined as unknown as PanelTableEntries;
    
    if (history.location.state !== undefined && history.location.state !== null) {
        let loadedPanelFromRouting: PanelTableEntries[] = history.location.state as PanelTableEntries[]
        preSelectedPanel = loadedPanelFromRouting[0]
        fromRouting = true;
    }


    const [panels, setPanels] = useState([] as PanelTableEntries[]);
    const [selectedPanel, setSelectedPanel] = useState(preSelectedPanel as PanelTableEntries)
    const [applicationList, setApplicationList] = useState(undefined as unknown as ApplicationSafeDTO[])

    let tableEntries: PanelTableEntries[] = []
    let panelId: number = +window.location.pathname.split('/')[2]  //Gets the id from the Url

    //GETS REVIEWER PANELS
    const reviewerPanelList = () => {
        new api.ReviewerControllerApi().getReviewerPanelsUsingGET(props.userId, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && getPanelGrants(res.data)).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
    }

    //GETS GRANTS OF ALL PANELS THAT THE REVIEWER BELONG TO
    const getPanelGrants = (panels: PanelDTORes[]) => {
        tableEntries = []
        let panNumb = panels.length
        let preSelected = undefined as unknown as PanelTableEntries; 

        for (let i = 0; i < panNumb; i++) {
            new api.GrantControllerApi().getGrantUsingGET(panels[i].grant, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => {

                if(!isNaN(panelId) && panelId === panels[i].panelID && !fromRouting){
                  
                    preSelected = { panel: panels[i], grant: (res.data as GrantDTORes)} as PanelTableEntries;
                    handlePanelSelect(preSelected)
                }

                addGrantToPanelList(res.data, panels[i], panNumb)

            }).catch(
                (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 

        }

        
        if(!isNaN(panelId) &&  preSelected === undefined && !fromRouting ){ //couldnt find panel the user belongs too
            history.push("/error/", [{status: '404', text:'No panel found', path: ''}])
        }

       
    }

    //PAIR GRANT INFO WITH RESPECTIVE PANEL INFO
    const addGrantToPanelList = (grant: GrantDTORes, panel: PanelDTORes, panNumb: number) => {
        tableEntries.push({ panel: panel, grant: grant } as PanelTableEntries);
        if (tableEntries.length === panNumb) {
            setPanels(tableEntries)
        }

    }

    const handlePanelSelect = (panelInfo: PanelTableEntries) => {
        new api.GrantControllerApi().getGrantApplicationsUsingGET(panelInfo.panel.grant, undefined, { headers: { 'Authorization': localStorage["jwt"] } }).then(res => res && (setApplicationList(res.data))).catch(
            (error) => (history.push("/error/", [{status: error.response.status, text:error.response.statusText, path: error.response.data.path}]) ) ) 
        setSelectedPanel(panelInfo)
    }

    useEffect(() => {
        if (props.userType.includes('ROLE_REVIEWER')) {
            
            reviewerPanelList();
            if((preSelectedPanel !== undefined && applicationList === undefined)){
                handlePanelSelect(preSelectedPanel);
            }
        
        }
        else {
            history.push("/error/", [{status: '403', text:'Forbiden', path: ''}])
        }
    }, [])



    return (
        <div className="page__container">
            <Navbar />
            <div className="table__container">
                <MyPanelList panels={panels} onSelectPanel={handlePanelSelect} selected={selectedPanel} />
                <PanelMemberList reviewers={selectedPanel} />
                <ApplicationsToReview apps={applicationList} />
            </div>
            <div className="footer">
            <Footer />
            </div>
        </div>
    )
}

export default MyPanels;


